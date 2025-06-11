package com.studentevaluation.service;

import com.studentevaluation.dao.ClassInfoDAO;
import com.studentevaluation.entity.ClassInfo;

import java.sql.SQLException;
import java.util.List;

public class ClassInfoService {
    private final ClassInfoDAO classInfoDAO = new ClassInfoDAO();

    public void addClassInfo(ClassInfo c) throws SQLException {
        classInfoDAO.addClassInfo(c);
    }

    public List<ClassInfo> getAllClassInfo() throws SQLException {
        return classInfoDAO.getAllClassInfo();
    }

    public void updateClassInfo(ClassInfo c) throws SQLException {
        classInfoDAO.updateClassInfo(c);
    }

    public void deleteClassInfo(String classId) throws SQLException {
        classInfoDAO.deleteClassInfo(classId);
    }
}