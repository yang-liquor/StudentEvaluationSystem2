package com.studentevaluation.dao;

import com.studentevaluation.entity.Teacher;
import com.studentevaluation.util.DBUtil;

import java.sql.*;
import java.util.*;

public class TeacherDAO {
    public void addTeacher(Teacher t) throws SQLException {
        String sql = "INSERT INTO TEACHER (TEACHER_ID, NAME, DEPARTMENT, TITLE) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTeacherId());
            ps.setString(2, t.getName());
            ps.setString(3, t.getDepartment());
            ps.setString(4, t.getTitle());
            ps.executeUpdate();
        }
    }

    public List<Teacher> getAllTeachers() throws SQLException {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT * FROM TEACHER";
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Teacher t = new Teacher(
                        rs.getString("TEACHER_ID"),
                        rs.getString("NAME"),
                        rs.getString("DEPARTMENT"),
                        rs.getString("TITLE")
                );
                list.add(t);
            }
        }
        return list;
    }

    public void updateTeacher(Teacher t) throws SQLException {
        String sql = "UPDATE TEACHER SET NAME=?, DEPARTMENT=?, TITLE=? WHERE TEACHER_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getDepartment());
            ps.setString(3, t.getTitle());
            ps.setString(4, t.getTeacherId());
            ps.executeUpdate();
        }
    }

    public void deleteTeacher(String teacherId) throws SQLException {
        String sql = "DELETE FROM TEACHER WHERE TEACHER_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, teacherId);
            ps.executeUpdate();
        }
    }
    // 新增：通过teacherId查找单个教师
    public Teacher findById(String teacherId) throws SQLException {
        String sql = "SELECT * FROM TEACHER WHERE TEACHER_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Teacher(
                            rs.getString("TEACHER_ID"),
                            rs.getString("NAME"),
                            rs.getString("DEPARTMENT"),
                            rs.getString("TITLE")
                    );
                }
            }
        }
        return null;
    }
}