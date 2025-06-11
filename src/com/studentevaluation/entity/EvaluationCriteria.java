package com.studentevaluation.entity;

public class EvaluationCriteria {
    private String criteriaId;
    private String content;

    public EvaluationCriteria() {}
    public EvaluationCriteria(String criteriaId, String content) {
        this.criteriaId = criteriaId;
        this.content = content;
    }
    public String getCriteriaId() { return criteriaId; }
    public void setCriteriaId(String criteriaId) { this.criteriaId = criteriaId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}