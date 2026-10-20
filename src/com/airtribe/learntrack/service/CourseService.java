package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.exception.EntityNotFoundException;   // [Claude] Added
import com.airtribe.learntrack.util.IdGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseService {
    private final ArrayList<Course> courses = new ArrayList<>();

    public Course addCourse(String courseName, String description, double duration)
    {
        Course course = new Course(IdGenerator.generateCourseId(),courseName,description,duration);
        courses.add(course);
        return course;
    }

    public List<Course> listCourses()
    {
        return Collections.unmodifiableList(courses);   // read-only view, so callers can't add/remove courses directly
    }

    // [Claude] Added: search a course by id, reused by activate/deactivate
    public Course findCourseById(int id) throws EntityNotFoundException
    {
        for(Course course: courses)
        {
            if(course.getId() == id)
            {
                return course;
            }
        }
        throw new EntityNotFoundException("Course with id " + id + " not found");
    }

    // [Claude] Added
    public void activateCourse(int id) throws EntityNotFoundException
    {
        findCourseById(id).setActive(true);
    }

    // [Claude] Added
    public void deactivateCourse(int id) throws EntityNotFoundException
    {
        findCourseById(id).setActive(false);
    }
}
