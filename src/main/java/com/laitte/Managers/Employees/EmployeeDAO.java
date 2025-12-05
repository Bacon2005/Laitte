package com.laitte.Managers.Employees;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.laitte.LaitteMain.Database;

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

    public static boolean deleteEmployee(int employeeId) {
        String getSQL = "SELECT loginid, roleid FROM employee WHERE employeeid = ?";
        String deleteEmployeeSQL = "DELETE FROM employee WHERE employeeid = ?";
        String deleteLoginSQL = "DELETE FROM login WHERE loginid = ?";
        String deleteRoleSQL = "DELETE FROM employeerole WHERE roleid = ?";

        try (Connection conn = Database.connect()) {

            conn.setAutoCommit(false); // BEGIN transaction

            int loginId = 0;
            int roleId = 0;

            // 1. Get loginid + roleid
            try (PreparedStatement stmt = conn.prepareStatement(getSQL)) {
                stmt.setInt(1, employeeId);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    loginId = rs.getInt("loginid");
                    roleId = rs.getInt("roleid");
                } else {
                    return false; // employee not found
                }
            }

            // 2. Delete employee first
            try (PreparedStatement stmt = conn.prepareStatement(deleteEmployeeSQL)) {
                stmt.setInt(1, employeeId);
                stmt.executeUpdate();
            }

            // 3. Delete role row
            try (PreparedStatement stmt = conn.prepareStatement(deleteRoleSQL)) {
                stmt.setInt(1, roleId);
                stmt.executeUpdate();
            }

            // 4. Delete login row
            try (PreparedStatement stmt = conn.prepareStatement(deleteLoginSQL)) {
                stmt.setInt(1, loginId);
                stmt.executeUpdate();
            }

            conn.commit(); // COMMIT transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}