package com.studentevaluation.dao;

import com.studentevaluation.entity.EvaluationCriteria;
import com.studentevaluation.util.DBUtil;

import java.sql.*;
import java.util.*;

public class EvaluationCriteriaDAO {
    public void addCriteria(EvaluationCriteria c) throws SQLException {
        String sql = "INSERT INTO EVALUATION_CRITERIA (CRITERIA_ID, CONTENT) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCriteriaId());
            ps.setString(2, c.getContent());
            ps.executeUpdate();
        }
    }

    public List<EvaluationCriteria> getAllCriteria() throws SQLException {
        List<EvaluationCriteria> list = new ArrayList<>();
        String sql = "SELECT * FROM EVALUATION_CRITERIA";
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                EvaluationCriteria c = new EvaluationCriteria(
                        rs.getString("CRITERIA_ID"),
                        rs.getString("CONTENT")
                );
                list.add(c);
            }
        }
        return list;
    }

    public void updateCriteria(EvaluationCriteria c) throws SQLException {
        String sql = "UPDATE EVALUATION_CRITERIA SET CONTENT=? WHERE CRITERIA_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getContent());
            ps.setString(2, c.getCriteriaId());
            ps.executeUpdate();
        }
    }

    public void deleteCriteria(String criteriaId) throws SQLException {
        String sql = "DELETE FROM EVALUATION_CRITERIA WHERE CRITERIA_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, criteriaId);
            ps.executeUpdate();
        }
    }
}