package com.studentevaluation.dao;

import com.studentevaluation.entity.Course;
import com.studentevaluation.util.DBUtil;

import java.sql.*;
import java.util.*;

public class CourseDAO {
    public void addCourse(Course c) throws SQLException {
        String sql = "INSERT INTO COURSE (COURSE_ID, NAME, TEACHER_ID) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCourseId());
            ps.setString(2, c.getName());
            ps.setString(3, c.getTeacherId());
            ps.executeUpdate();
        }
    }

    public List<Course> getAllCourses() throws SQLException {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM COURSE";
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Course c = new Course(
                        rs.getString("COURSE_ID"),
                        rs.getString("NAME"),
                        rs.getString("TEACHER_ID")
                );
                list.add(c);
            }
        }
        return list;
    }

    public void updateCourse(Course c) throws SQLException {
        String sql = "UPDATE COURSE SET NAME=?, TEACHER_ID=? WHERE COURSE_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getTeacherId());
            ps.setString(3, c.getCourseId());
            ps.executeUpdate();
        }
    }

    public void deleteCourse(String courseId) throws SQLException {
        String sql = "DELETE FROM COURSE WHERE COURSE_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseId);
            ps.executeUpdate();
        }
    }
}