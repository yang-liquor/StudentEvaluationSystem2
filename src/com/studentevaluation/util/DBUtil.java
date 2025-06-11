package com.studentevaluation.util;

import java.sql.*;

public class DBUtil {
    private static final String URL ="jdbc:hsqldb:hsql://localhost/evaldb";
    private static final String USER = "SA";
    private static final String PASS = "";

    static {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("HSQLDB驱动加载失败", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER,PASS);
    }
}