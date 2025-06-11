package com.studentevaluation.dao;

import com.studentevaluation.entity.ClassInfo;
import com.studentevaluation.util.DBUtil;

import java.sql.*;
import java.util.*;

public class ClassInfoDAO {
    public void addClassInfo(ClassInfo c) throws SQLException {
        String sql = "INSERT INTO CLASS_INFO (CLASS_ID, CLASS_NAME) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getClassId());
            ps.setString(2, c.getClassName());
            ps.executeUpdate();
        }
    }

    public List<ClassInfo> getAllClassInfo() throws SQLException {
        List<ClassInfo> list = new ArrayList<>();
        String sql = "SELECT * FROM CLASS_INFO";
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ClassInfo c = new ClassInfo(
                        rs.getString("CLASS_ID"),
                        rs.getString("CLASS_NAME")
                );
                list.add(c);
            }
        }
        return list;
    }

    public void updateClassInfo(ClassInfo c) throws SQLException {
        String sql = "UPDATE CLASS_INFO SET CLASS_NAME=? WHERE CLASS_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getClassName());
            ps.setString(2, c.getClassId());
            ps.executeUpdate();
        }
    }

    public void deleteClassInfo(String classId) throws SQLException {
        String sql = "DELETE FROM CLASS_INFO WHERE CLASS_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, classId);
            ps.executeUpdate();
        }
    }
}