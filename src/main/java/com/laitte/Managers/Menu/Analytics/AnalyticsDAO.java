package com.laitte.Managers.Menu.Analytics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.laitte.LaitteMain.Database;

public class AnalyticsDAO {

    // Insert a new analytics record and return the generated analyticsid
    public static int insertAnalytics(int employeeId, int mealId, LocalDate transactionDate) throws SQLException {
        String sql = "INSERT INTO analytics (employeeid, mealid, transactiondate) VALUES (?, ?, ?) RETURNING analyticsid";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setInt(2, mealId);
            pstmt.setDate(3, java.sql.Date.valueOf(transactionDate));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("analyticsid"); // return the generated PK
                }
            }
        }
        throw new SQLException("Failed to insert analytics");
    }
}


