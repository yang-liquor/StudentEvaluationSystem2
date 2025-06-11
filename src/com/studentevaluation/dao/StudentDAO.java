package com.studentevaluation.dao;

import com.studentevaluation.entity.Student;
import com.studentevaluation.util.DBUtil;

import java.sql.*;
import java.util.*;

public class StudentDAO {
    public void addStudent(Student s) throws SQLException {
        String sql = "INSERT INTO STUDENT (STUDENT_ID, NAME, CLASS_ID) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getStudentId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getClassId());
            ps.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM STUDENT";
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student(
                        rs.getString("STUDENT_ID"),
                        rs.getString("NAME"),
                        rs.getString("CLASS_ID")
                );
                list.add(s);
            }
        }
        return list;
    }

    public void updateStudent(Student s) throws SQLException {
        String sql = "UPDATE STUDENT SET NAME=?, CLASS_ID=? WHERE STUDENT_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getClassId());
            ps.setString(3, s.getStudentId());
            ps.executeUpdate();
        }
    }

    public void deleteStudent(String studentId) throws SQLException {
        String sql = "DELETE FROM STUDENT WHERE STUDENT_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.executeUpdate();
        }
    }
}