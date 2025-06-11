package com.studentevaluation.entity;

public class Teacher {
    private String teacherId;
    private String name;
    private String department;
    private String title;

    public Teacher() {}
    public Teacher(String teacherId, String name, String department, String title) {
        this.teacherId = teacherId;
        this.name = name;
        this.department = department;
        this.title = title;
    }
    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}