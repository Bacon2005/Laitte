package com.laitte.Managers.Menu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.laitte.Managers.Menu.Meal;

public class MealDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/Laitte";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    public static List<Meal> getAllMeals() {
        List<Meal> list = new ArrayList<>();

        String query = """
                select m.mealid, m.mealname, m.mealprice, c.mealcategory, m2.mealtype, i.photopath
                from meal m, category c, mealtype m2, inventory i
                where m.categoryid = c.categoryid
                and m.mealtypeid = m2.mealtypeid
                and m.inventoryid = i.inventoryid
                                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("mealname");
                int price = rs.getInt("mealprice");
                String mealCategory = rs.getString("mealcategory");
                String mealtype = rs.getString("mealtype");
                String imagePath = rs.getString("photopath");
                list.add(new Meal(name, price, mealCategory, mealtype, imagePath));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
