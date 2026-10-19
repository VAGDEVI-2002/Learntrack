package com.airtribe.learntrack.UI;
import com.airtribe.learntrack.entity.*;   // [Claude] Now used when printing students
import com.airtribe.learntrack.exception.EntityNotFoundException;   // [Claude] Added
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.service.*;
import com.airtribe.learntrack.util.InputValidations;   // [Claude] Added - email validation now happens in the UI

import java.util.ArrayList;
import java.util.List;   // [Claude] Added
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        StudentService studentService = new StudentService();
        CourseService courseService = new CourseService();
        EnrollmentService enrollmentService = new EnrollmentService(studentService, courseService);   // [Claude] Added

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while(running)
        {
            System.out.println("========LearnTrack==========");
            System.out.println("1. Student Management");
            System.out.println("2. Course Management");
            System.out.println("3. Enrollment Management");
            System.out.println("0. Exit");

            int ch = readInt(sc, "Enter your choice: ");

            switch(ch)
            {
                case 1:
                    showStudentMenu(sc, studentService);
                    break;
                case 2:
                    showCourseMenu(sc, courseService);
                    break;   // [Claude] Added missing break (was falling through to case 3)
                case 3:
                    showEnrollmentMenu(sc, enrollmentService);   // [Claude] Replaced "Coming soon..."
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
            System.out.println("**************************************************");
        }
    }


    private static void showStudentMenu(Scanner sc, StudentService studentService)
    {
        boolean flag = true;

        while(flag) {
            System.out.println("*****************Student Management**************");
            System.out.println("1. Add Student");
            System.out.println("2. View all Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Deactivate Student");
            System.out.println("0. Back");


            int ch = readInt(sc, "Enter your choice : ");

            try {
                switch (ch) {
                    case 1:
                        addStudent(sc, studentService);
                        break;
                    case 2:
                        viewAllStudents(studentService);
                        break;
                    case 3:
                        Student student = studentService.findStudentById(readInt(sc, "Enter the student id: "));
                        System.out.println(student.getDisplayName());
                        break;
                    case 4:
                        studentService.deactivateStudent(readInt(sc, "Enter the student id to deactivate: "));
                        System.out.println("Student deactivated successfully!");
                        break;
                    case 0:
                        flag = false;
                        break;
                    default:
                        System.out.println("Choose from the above options");
                }
            } catch (EntityNotFoundException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    private static void addStudent(Scanner sc, StudentService studentService)
    {
        System.out.print("Enter the firstName: ");
        String fname = sc.nextLine().trim();
        System.out.print("Enter the lastName: ");
        String lname = sc.nextLine().trim();
        String email = readEmail(sc);
        System.out.print("Enter the batch: ");
        String batch = sc.nextLine().trim();

        Student student;
        if(email.isEmpty())
        {
            student = studentService.addStudent(fname, lname, batch);
        }
        else
        {
            student = studentService.addStudent(fname, lname, email, batch);
        }
        System.out.println("Student added successfully! Id: " + student.getId());
    }

    private static void viewAllStudents(StudentService studentService)
    {
        List<Student> students = studentService.listStudents();
        if(students.isEmpty())
        {
            System.out.println("No students found.");
            return;
        }
        for(Student stud: students)
        {
            System.out.println(stud.getDisplayName());
        }
    }

    private static String readEmail(Scanner sc)
    {
        while(true)
        {
            System.out.print("Enter the email (leave blank to skip): ");
            String email = sc.nextLine().trim();
            if(email.isEmpty() || InputValidations.validateEmail(email))
            {
                return email;
            }
            System.out.println("Invalid email, please try again.");
        }
    }

    private static int readInt(Scanner sc, String prompt)
    {
        while(true)
        {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static void showCourseMenu(Scanner sc, CourseService courseService)
    {
        boolean running = true;
        while(running)
        {
            System.out.println("*****************Course Management**************");
            System.out.println("1. Add Course");
            System.out.println("2. View all Courses");
            System.out.println("3. Activate Course");
            System.out.println("4. Deactivate Course");
            System.out.println("0. Go back");

            int ch = readInt(sc, "Enter your choice : ");   // [Claude] readInt instead of sc.nextInt() so leftover newlines don't break nextLine()
            try {
                switch(ch)
                {
                    case 1:
                        addCourse(sc, courseService);
                        break;
                    case 2:
                        viewAllCourses(courseService);
                        break;
                    case 3:
                        courseService.activateCourse(readInt(sc, "Enter the course id to activate: "));
                        System.out.println("Course activated successfully!");
                        break;
                    case 4:
                        courseService.deactivateCourse(readInt(sc, "Enter the course id to deactivate: "));
                        System.out.println("Course deactivated successfully!");
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("choose from the above options");
                }
            } catch (EntityNotFoundException | InvalidInputException e) {   // [Claude] Added
                System.out.println(e.getMessage());
            }
        }
    }

    public static void addCourse(Scanner sc, CourseService courseService) throws InvalidInputException
    {
        System.out.print("Enter the course name : ");
        String cname = sc.nextLine().trim();
        InputValidations.checkNoEmpty(cname);
        System.out.print("Enter the description : ");
        String desc = sc.nextLine().trim();
        InputValidations.checkNoEmpty(desc);
        double dur = readDouble(sc, "Enter the duration (in weeks): ");   // [Claude] Replaced sc.nextDouble()
        if(dur<=0)
        {
            throw new InvalidInputException("Duration must be greater than 0.");
        }

        Course course = courseService.addCourse(cname,desc,dur);
        System.out.println("Course added! Id: "+course.getId());
    }

    public static void viewAllCourses(CourseService courseService)
    {
        ArrayList<Course> courses = courseService.listCourses();
        // [Claude] Added: actually print the courses
        if(courses.isEmpty())
        {
            System.out.println("No courses found.");
            return;
        }
        for(Course course: courses)
        {
            String status = course.isActive() ? "ACTIVE" : "INACTIVE";
            System.out.println(course.getId() + " | " + course.getCourseName() + " | " + course.getDescription()
                    + " | " + course.getDurationInWeeks() + " weeks | " + status);
        }
    }

    public static void showEnrollmentMenu(Scanner sc, EnrollmentService enrollmentService)
    {
        boolean running = true;
        while(running)
        {
            System.out.println("*****************Enrollment Management**************");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. View Enrollments for a Student");
            System.out.println("3. Mark Enrollment as Completed");
            System.out.println("4. Mark Enrollment as Cancelled");
            System.out.println("0. Go back");

            int ch = readInt(sc, "Enter your choice : ");
            try {
                switch(ch)
                {
                    case 1:
                        enrollStudent(sc, enrollmentService);
                        break;
                    case 2:
                        viewEnrollmentsForStudent(sc, enrollmentService);
                        break;
                    case 3:
                        markEnrollmentCompleted(sc, enrollmentService);
                        break;
                    case 4:
                        markEnrollmentCancelled(sc, enrollmentService);
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("choose from the above options");
                }
            } catch (EntityNotFoundException | InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void enrollStudent(Scanner sc, EnrollmentService enrollmentService) throws EntityNotFoundException, InvalidInputException
    {
        int sid = readInt(sc, "Enter the student id: ");
        int cid = readInt(sc, "Enter the course id: ");
        Enrollment enrollment = enrollmentService.enrollStudent(sid, cid);
        System.out.println("Student enrolled successfully! Enrollment Id: " + enrollment.getId());
    }

    private static void viewEnrollmentsForStudent(Scanner sc, EnrollmentService enrollmentService) throws EntityNotFoundException
    {
        int sid = readInt(sc, "Enter the student id: ");
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsForStudent(sid);
        if(enrollments.isEmpty())
        {
            System.out.println("No enrollments found.");
            return;
        }
        for(Enrollment enrollment: enrollments)
        {
            System.out.println(enrollment.getId() + " | Student " + enrollment.getStudentId() + " | Course "
                    + enrollment.getCourseId() + " | " + enrollment.getEnrollmentDate() + " | " + enrollment.getStatus());
        }
    }

    private static void markEnrollmentCompleted(Scanner sc, EnrollmentService enrollmentService) throws EntityNotFoundException, InvalidInputException
    {
        int eid = readInt(sc, "Enter the enrollment id: ");
        enrollmentService.updateStatus(eid, EnrollmentStatus.COMPLETED);
        System.out.println("Enrollment marked as completed!");
    }

    private static void markEnrollmentCancelled(Scanner sc, EnrollmentService enrollmentService) throws EntityNotFoundException, InvalidInputException
    {
        int eid = readInt(sc, "Enter the enrollment id: ");
        enrollmentService.updateStatus(eid, EnrollmentStatus.CANCELLED);
        System.out.println("Enrollment marked as cancelled!");
    }

    private static double readDouble(Scanner sc, String prompt)
    {
        while(true)
        {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
