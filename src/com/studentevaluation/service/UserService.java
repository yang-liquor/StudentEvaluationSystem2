package com.studentevaluation.service;

import com.studentevaluation.dao.UserDAO;
import com.studentevaluation.entity.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public void addUser(User u) throws SQLException {
        userDAO.addUser(u);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    public void updateUser(User u) throws SQLException {
        userDAO.updateUser(u);
    }

    public void deleteUser(String userId) throws SQLException {
        userDAO.deleteUser(userId);
    }
}