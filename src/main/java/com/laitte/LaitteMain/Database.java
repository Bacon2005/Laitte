package com.laitte.LaitteMain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Database connection class
public class Database {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
