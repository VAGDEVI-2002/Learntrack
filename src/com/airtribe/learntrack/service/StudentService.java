package com.airtribe.learntrack.service;
import java.util.*;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.exception.EntityNotFoundException;   // [Claude] Added for custom exception
import com.airtribe.learntrack.util.IdGenerator;

public class StudentService {
    private final ArrayList<Student> students = new ArrayList<>();   // [Claude] Made private (encapsulation)

// ====================Adding a Student============================
    public Student addStudent(String firstName, String lastName, String email, String batch)
    {
        Student student = new Student(IdGenerator.generateStudentId(), firstName, lastName, email, batch);
        students.add(student);
        return student;
    }

    public Student addStudent(String firstName, String lastName, String batch)
    {
        Student student = new Student(IdGenerator.generateStudentId(), firstName, lastName, batch);
        students.add(student);
        return student;
    }

//   ======================List all students=======================
    public List<Student> listStudents()
    {
        return students;
    }

// =========================Search a student===============================
    public Student findStudentById(int id) throws EntityNotFoundException
    {
        for(Student stud: students)
        {
            if(stud.getId() == id)
            {
                return stud;
            }
        }
        throw new EntityNotFoundException("Student with id " + id + " not found");
    }

//   ======================Deactivate Student==========================
    public void deactivateStudent(int id) throws EntityNotFoundException
    {
        findStudentById(id).setActive(false);
    }

}
