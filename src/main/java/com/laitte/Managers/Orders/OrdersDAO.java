package com.laitte.Managers.Orders;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/Laitte";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    public static List<Orders> getAllOrders() {
        List<Orders> list = new ArrayList<>();

        String query = """
                select *
                from orders
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String customerName = rs.getString("customername");
                int orderNumber = rs.getInt("ordersid");
                boolean isPending = rs.getBoolean("ispending");
                list.add(new Orders(customerName, orderNumber, isPending));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean cancelOrder(int ordersid) {
        String sql = "UPDATE orders SET ispending = false, iscancelled = true WHERE ordersid = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ordersid);
            int rows = pstmt.executeUpdate();
            return rows > 0; // true if the order was updated

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean completeOrder(int ordersid) {
        String sql = """
                UPDATE orders SET ispending = false, iscancelled = false WHERE ordersid = ?
                """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ordersid);
            int rows = pstmt.executeUpdate();
            return rows > 0; // true if the order was updated

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int countOrdersByMonth(int month) {
        String sql = "SELECT COUNT(*) AS total FROM analytics WHERE EXTRACT(MONTH FROM transactiondate) = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, month);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0; // Return zero if error
    }

}
