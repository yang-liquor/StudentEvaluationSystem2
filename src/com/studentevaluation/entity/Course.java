package com.studentevaluation.entity;

public class Course {
    private String courseId;
    private String name;
    private String teacherId;

    public Course() {}
    public Course(String courseId, String name, String teacherId) {
        this.courseId = courseId;
        this.name = name;
        this.teacherId = teacherId;
    }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
}