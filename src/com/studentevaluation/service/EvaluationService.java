package com.studentevaluation.service;

import com.studentevaluation.dao.EvaluationDAO;
import com.studentevaluation.entity.Evaluation;

import java.sql.SQLException;
import java.util.List;

public class EvaluationService {
    private final EvaluationDAO evaluationDAO = new EvaluationDAO();

    public void addEvaluation(Evaluation e) throws SQLException {
        evaluationDAO.addEvaluation(e);
    }

    public List<Evaluation> getAllEvaluations() throws SQLException {
        return evaluationDAO.getAllEvaluations();
    }

    public void updateEvaluation(Evaluation e) throws SQLException {
        evaluationDAO.updateEvaluation(e);
    }

    public void deleteEvaluation(int evaluationId) throws SQLException {
        evaluationDAO.deleteEvaluation(evaluationId);
    }
}