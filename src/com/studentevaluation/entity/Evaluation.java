package com.studentevaluation.entity;

import java.sql.Timestamp;

public class Evaluation {
    private int evaluationId;
    private String studentId;
    private String courseId;
    private double score;
    private String criteriaId;
    private String comment;
    private Timestamp evalTime;

    public Evaluation() {}
    public Evaluation(int evaluationId, String studentId, String courseId, double score, String criteriaId, String comment, Timestamp evalTime) {
        this.evaluationId = evaluationId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.score = score;
        this.criteriaId = criteriaId;
        this.comment = comment;
        this.evalTime = evalTime;
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getCriteriaId() {
        return criteriaId;
    }

    public void setCriteriaId(String criteriaId) {
        this.criteriaId = criteriaId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getEvalTime() {
        return evalTime;
    }

    public void setEvalTime(Timestamp evalTime) {
        this.evalTime = evalTime;
    }
}