package com.studentevaluation.service;

import com.studentevaluation.dao.EvaluationCriteriaDAO;
import com.studentevaluation.entity.EvaluationCriteria;

import java.sql.SQLException;
import java.util.List;

public class EvaluationCriteriaService {
    private final EvaluationCriteriaDAO evaluationCriteriaDAO = new EvaluationCriteriaDAO();

    public void addCriteria(EvaluationCriteria c) throws SQLException {
        evaluationCriteriaDAO.addCriteria(c);
    }

    public List<EvaluationCriteria> getAllCriteria() throws SQLException {
        return evaluationCriteriaDAO.getAllCriteria();
    }

    public void updateCriteria(EvaluationCriteria c) throws SQLException {
        evaluationCriteriaDAO.updateCriteria(c);
    }

    public void deleteCriteria(String criteriaId) throws SQLException {
        evaluationCriteriaDAO.deleteCriteria(criteriaId);
    }
}