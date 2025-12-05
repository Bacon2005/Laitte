package com.laitte.Managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddInventoryController {
    @FXML
    private TextField addMealName;

    @FXML
    private TextField addStockAvailable;

    @FXML
    private TextField addStockDate;

    @FXML
    private TextField addPrice;

    @FXML
    private TextField addCategory;

    @FXML
    private CheckBox isVegetarian;

    @FXML
    private void cancelBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    private InventoryController mainController;

    public void setMainController(InventoryController mainController) {
    this.mainController = mainController;}

    @FXML
    private void confirmBtn(ActionEvent event) {
        String mealName = addMealName.getText(); // meal id
        String stockDate = addStockDate.getText(); //inventory id
        boolean mealStatus = isVegetarian.isSelected();
        String mealtype = mealStatus ? "Vegetarian" : "Non-Vegetarian";  
        String category = addCategory.getText(); //category id


        int stockAvailable = Integer.parseInt(addStockAvailable.getText()); //inventory id
        double price = Double.parseDouble(addPrice.getText()); //meal id

        //mealtype i dont think you insert into it, u jus have to choose if its vege or non
        String selectMealtype = """
        SELECT mealtypeid FROM mealtype WHERE mealtype = ?
        """;
        
        String insertInventory = """
            INSERT INTO inventory (stockavailable, stockdate)
            VALUES (?, ?)
            RETURNING inventoryid
            """;


        String insertCategory = """
                    INSERT INTO category (mealcategory)
                    VALUES (?)
                    RETURNING categoryid
                """;

        String insertMeal = """
                    INSERT INTO meal (inventoryid, categoryid, mealtypeid, mealname, mealprice)
                    VALUES (?, ?, ?, ?, ?)
                    RETURNING mealid
                """;

        try (Connection conn = com.laitte.LaitteMain.Database.connect()) {
            conn.setAutoCommit(false);

        //1. Insert Meal Type 
        int mealTypeID;
        try (PreparedStatement ps = conn.prepareStatement(selectMealtype)) {
        ps.setString(1, mealtype);
        try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            mealTypeID = rs.getInt("mealtypeid");
        } else {
            throw new Exception("Meal type not found in database.");
        }}}

        // 2. Insert Inventory
            int inventoryID;

            //this coverts string into date
            String stockDateStr = addStockDate.getText(); 
            java.sql.Date sqlStockDate = java.sql.Date.valueOf(stockDateStr);
            
            try (PreparedStatement ps = conn.prepareStatement(insertInventory)) {
            ps.setInt(1, stockAvailable);
            ps.setDate(2, sqlStockDate);

            try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    inventoryID = rs.getInt("inventoryid");}}

        // 3. Insert Category
            int categoryID;
            try (PreparedStatement ps = conn.prepareStatement(insertCategory)) { 
            ps.setString(1, category);
            var rs = ps.executeQuery();
            rs.next();
            categoryID = rs.getInt("categoryid");}

        // 4. Insert Meal
            try (var ps = conn.prepareStatement(insertMeal)) {
            ps.setInt(1, inventoryID);
            ps.setInt(2, categoryID);
            ps.setInt(3, mealTypeID);
            ps.setString(4, mealName);
            ps.setDouble(5, price);
            ps.executeQuery();}

            conn.commit();
            System.out.println("Inventory inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mainController != null) {
        mainController.loadTableData();}

        // Close the window after confirming
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
