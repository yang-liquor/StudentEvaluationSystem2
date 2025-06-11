package com.studentevaluation.service;

import com.studentevaluation.dao.TeacherDAO;
import com.studentevaluation.entity.Teacher;

import java.sql.SQLException;
import java.util.List;

public class TeacherService {
    private final TeacherDAO teacherDAO = new TeacherDAO();

    public void addTeacher(Teacher t) throws SQLException {
        teacherDAO.addTeacher(t);
    }

    public List<Teacher> getAllTeachers() throws SQLException {
        return teacherDAO.getAllTeachers();
    }

    public void updateTeacher(Teacher t) throws SQLException {
        teacherDAO.updateTeacher(t);
    }

    public void deleteTeacher(String teacherId) throws SQLException {
        teacherDAO.deleteTeacher(teacherId);
    }
    // 新增：通过teacherId查找单个教师
    public Teacher findById(String teacherId) throws SQLException {
        return teacherDAO.findById(teacherId);
    }
}