package com.studentevaluation.dao;

import com.studentevaluation.entity.User;
import com.studentevaluation.util.DBUtil;

import java.sql.*;
import java.util.*;

public class UserDAO {
    public void addUser(User u) throws SQLException {
        String sql = "INSERT INTO USER (USER_ID, PASSWORD, ROLE, REF_ID) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUserId());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole());
            ps.setString(4, u.getRefId());
            ps.executeUpdate();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM USER";
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                User u = new User(
                        rs.getString("USER_ID"),
                        rs.getString("PASSWORD"),
                        rs.getString("ROLE"),
                        rs.getString("REF_ID")
                );
                list.add(u);
            }
        }
        return list;
    }

    public void updateUser(User u) throws SQLException {
        String sql = "UPDATE USER SET PASSWORD=?, ROLE=?, REF_ID=? WHERE USER_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getPassword());
            ps.setString(2, u.getRole());
            ps.setString(3, u.getRefId());
            ps.setString(4, u.getUserId());
            ps.executeUpdate();
        }
    }

    public void deleteUser(String userId) throws SQLException {
        String sql = "DELETE FROM USER WHERE USER_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        }
    }
}