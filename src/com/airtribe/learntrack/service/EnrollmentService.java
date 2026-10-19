// [Claude] New file: enrollment operations; uses StudentService/CourseService to validate ids
package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.EnrollmentStatus;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.util.IdGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentService {
    private final ArrayList<Enrollment> enrollments = new ArrayList<>();
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentService(StudentService studentService, CourseService courseService)
    {
        this.studentService = studentService;
        this.courseService = courseService;
    }

// ====================Enroll a Student in a Course============================
    public Enrollment enrollStudent(int studentId, int courseId) throws EntityNotFoundException, InvalidInputException
    {
        Student student = studentService.findStudentById(studentId);
        Course course = courseService.findCourseById(courseId);

        if(!student.isActive())
        {
            throw new InvalidInputException("Student with id " + studentId + " is inactive");
        }
        if(!course.isActive())
        {
            throw new InvalidInputException("Course with id " + courseId + " is inactive");
        }
        for(Enrollment enrollment: enrollments)
        {
            if(enrollment.getStudentId() == studentId && enrollment.getCourseId() == courseId
                    && enrollment.getStatus() == EnrollmentStatus.ACTIVE)
            {
                throw new InvalidInputException("Student is already enrolled in this course");
            }
        }

        Enrollment enrollment = new Enrollment(IdGenerator.generateEnrollmentId(), studentId, courseId,
                LocalDate.now().toString(), EnrollmentStatus.ACTIVE);
        enrollments.add(enrollment);
        return enrollment;
    }

// ====================View Enrollments of a Student============================
    public List<Enrollment> getEnrollmentsForStudent(int studentId) throws EntityNotFoundException
    {
        studentService.findStudentById(studentId);   // throws if the student doesn't exist
        List<Enrollment> result = new ArrayList<>();
        for(Enrollment enrollment: enrollments)
        {
            if(enrollment.getStudentId() == studentId)
            {
                result.add(enrollment);
            }
        }
        return result;
    }

// ====================Search an Enrollment============================
    public Enrollment findEnrollmentById(int id) throws EntityNotFoundException
    {
        for(Enrollment enrollment: enrollments)
        {
            if(enrollment.getId() == id)
            {
                return enrollment;
            }
        }
        throw new EntityNotFoundException("Enrollment with id " + id + " not found");
    }

// ====================Mark Enrollment Completed / Cancelled============================
    public void updateStatus(int enrollmentId, EnrollmentStatus status) throws EntityNotFoundException, InvalidInputException
    {
        Enrollment enrollment = findEnrollmentById(enrollmentId);
        if(enrollment.getStatus() != EnrollmentStatus.ACTIVE)
        {
            throw new InvalidInputException("Enrollment is already " + enrollment.getStatus());
        }
        enrollment.setStatus(status);
    }
}
