package com.studentevaluation.service;

import com.studentevaluation.dao.StudentDAO;
import com.studentevaluation.entity.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private final StudentDAO studentDAO = new StudentDAO();

    public void addStudent(Student s) throws SQLException {
        studentDAO.addStudent(s);
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public void updateStudent(Student s) throws SQLException {
        studentDAO.updateStudent(s);
    }

    public void deleteStudent(String studentId) throws SQLException {
        studentDAO.deleteStudent(studentId);
    }
}