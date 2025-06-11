package com.studentevaluation.dao;

import com.studentevaluation.entity.Evaluation;
import com.studentevaluation.util.DBUtil;

import java.sql.*;
import java.util.*;

public class EvaluationDAO {
    public void addEvaluation(Evaluation e) throws SQLException {
        String sql = "INSERT INTO EVALUATION (STUDENT_ID, COURSE_ID, SCORE, CRITERIA_ID, COMMENT, EVAL_TIME) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getStudentId());
            ps.setString(2, e.getCourseId());
            ps.setDouble(3, e.getScore());
            ps.setString(4, e.getCriteriaId());
            ps.setString(5, e.getComment());
            ps.setTimestamp(6, e.getEvalTime());
            ps.executeUpdate();
        }
    }

    public List<Evaluation> getAllEvaluations() throws SQLException {
        List<Evaluation> list = new ArrayList<>();
        String sql = "SELECT * FROM EVALUATION";
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Evaluation e = new Evaluation(
                        rs.getInt("EVALUATION_ID"),
                        rs.getString("STUDENT_ID"),
                        rs.getString("COURSE_ID"),
                        rs.getDouble("SCORE"),
                        rs.getString("CRITERIA_ID"),
                        rs.getString("COMMENT"),
                        rs.getTimestamp("EVAL_TIME")
                );
                list.add(e);
            }
        }
        return list;
    }

    public void updateEvaluation(Evaluation e) throws SQLException {
        String sql = "UPDATE EVALUATION SET STUDENT_ID=?, COURSE_ID=?, SCORE=?, CRITERIA_ID=?, COMMENT=?, EVAL_TIME=? WHERE EVALUATION_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getStudentId());
            ps.setString(2, e.getCourseId());
            ps.setDouble(3, e.getScore());
            ps.setString(4, e.getCriteriaId());
            ps.setString(5, e.getComment());
            ps.setTimestamp(6, e.getEvalTime());
            ps.setInt(7, e.getEvaluationId());
            ps.executeUpdate();
        }
    }

    public void deleteEvaluation(int evaluationId) throws SQLException {
        String sql = "DELETE FROM EVALUATION WHERE EVALUATION_ID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, evaluationId);
            ps.executeUpdate();
        }
    }
}