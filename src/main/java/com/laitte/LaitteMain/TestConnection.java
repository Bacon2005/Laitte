package com.laitte.LaitteMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Simple test class to verify database connection and login query
public class TestConnection {
    public static void main(String[] args) {
        String username = "Admin";
        String password = "1234";

        String query = "SELECT * FROM \"Laitte\".\"users\" WHERE username = ? AND password = ?";

        try (Connection conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Login success!");
            } else {
                System.out.println("Login failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
