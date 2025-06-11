package com.studentevaluation.service;

import com.studentevaluation.dao.CourseDAO;
import com.studentevaluation.entity.Course;

import java.sql.SQLException;
import java.util.List;

public class CourseService {
    private final CourseDAO courseDAO = new CourseDAO();

    public void addCourse(Course c) throws SQLException {
        courseDAO.addCourse(c);
    }

    public List<Course> getAllCourses() throws SQLException {
        return courseDAO.getAllCourses();
    }

    public void updateCourse(Course c) throws SQLException {
        courseDAO.updateCourse(c);
    }

    public void deleteCourse(String courseId) throws SQLException {
        courseDAO.deleteCourse(courseId);
    }
}