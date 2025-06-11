package com.studentevaluation.util;

import com.studentevaluation.dao.StudentDAO;
import com.studentevaluation.dao.TeacherDAO;
import com.studentevaluation.entity.Student;
import com.studentevaluation.entity.Teacher;

import java.sql.SQLException;
import java.util.List;

public class NameUtil {
    public static String getStudentNameById(String studentId) {
        try {
            List<Student> students = new StudentDAO().getAllStudents();
            for (Student s : students) {
                if (s.getStudentId().equals(studentId)) {
                    return s.getName();
                }
            }
        } catch (SQLException ignored) {}
        return studentId;
    }

    public static String getTeacherNameById(String teacherId) {
        try {
            List<Teacher> teachers = new TeacherDAO().getAllTeachers();
            for (Teacher t : teachers) {
                if (t.getTeacherId().equals(teacherId)) {
                    return t.getName();
                }
            }
        } catch (SQLException ignored) {}
        return teacherId;
    }
}