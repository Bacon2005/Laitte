package com.laitte.Managers.Employees;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/Laitte";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    public static List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();

        String query = """
                SELECT
                e.employeeid AS employeeid,
                e.firstname AS firstname,
                e.lastname AS lastname,
                e2.rolename AS rolename,
                e2.ismanager AS ismanager,
                e.imagepath AS image_path
                FROM employee e
                JOIN employeerole e2 ON e.roleid = e2.roleid
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("firstname") + " " + rs.getString("lastname");
                int id = rs.getInt("employeeid");
                String role = rs.getString("rolename");
                boolean manager = rs.getBoolean("ismanager");
                String imagePath = rs.getString("image_path");
                list.add(new Employee(name, id, role, manager, imagePath));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void setManager(int employeeID, boolean isManager) {
        String query = """
                UPDATE employeerole
                SET ismanager = ?, rolename = ?
                WHERE roleid = (SELECT roleid FROM employee WHERE employeeid = ?)
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setBoolean(1, isManager);
            pstmt.setString(2, isManager ? "Manager" : "Worker");
            pstmt.setInt(3, employeeID);

            int rows = pstmt.executeUpdate();
            System.out.println("Rows updated: " + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}