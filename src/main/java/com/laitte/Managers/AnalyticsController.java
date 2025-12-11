package com.laitte.Managers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.laitte.LaitteMain.Database;
import com.laitte.Managers.MealSales;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.chart.NumberAxis;

public class AnalyticsController {

     // --------------------------- Variables for FXML ----------------------------//
    @FXML private Button Accounts;
    @FXML private Button analytics;
    @FXML private AnchorPane ancpnMealImg;
    @FXML private LineChart<String, Number> chLineChart;
    @FXML private DatePicker datepicker;
    @FXML private Button home;
    @FXML private Button inventory;
    @FXML private Label labelAvgRevOrder;
    @FXML private Label labelGrossRev;
    @FXML private Label labelMealCategory;
    @FXML private Label labelCancelledOrders;
    @FXML private Label labelMealDescription;
    @FXML private Label labelMealID;
    @FXML private Label labelMealName;
    @FXML private Label labelMealPrice;
    @FXML private Label labelMealType;
    @FXML private Label labelNewCustomers;
    @FXML private Label labelOrdersCompleted;
    @FXML private Button logoutBtn;
    @FXML private Button menu;
    @FXML private Label nameLabel;
    @FXML private Button orders;
    @FXML private ImageView profilePic;
    @FXML private Button settingBtn;
    @FXML private AnchorPane slider;
    @FXML private AnchorPane rootPane;
    @FXML private TableView<MealSales> salesTable;
    @FXML private TableColumn<MealSales, String> colMealName;
    @FXML private TableColumn<MealSales, Integer> colMealSales;


    public TableRow<Item> initialize() {
        // ----------------------------- Slide thing -----------------------------//

        slider.setVisible(true); // ensures that Slider is visible and can be interacted with

        Circle clip = new Circle(50, 50, 50); // centerX, centerY, radius
        profilePic.setClip(clip);

        double hiddenX = -200; // sidebar width
        slider.setTranslateX(hiddenX);

        // Slide IN animation
        TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.3), slider);
        slideIn.setToX(0);

        // Slide OUT animation
        TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.3), slider);
        slideOut.setToX(hiddenX);

        // 1. Hover near left edge → slide IN
        rootPane.setOnMouseMoved(event -> {
            if (event.getX() <= 50) { // 10px from left edge
                slideIn.play();
            }
        });

        // 2. Hover OUTSIDE sidebar → slide OUT
        slider.setOnMouseExited(event -> slideOut.play());
        
        // ----------------------------- When you pick date -----------------------------//
        datepicker.setOnAction(event -> {
        LocalDate selectedDate = datepicker.getValue();
        if (selectedDate != null) {
        updateLineChart(selectedDate);
        updateGrossRevenue(selectedDate);
        updateOrdersCompleted(selectedDate);
        updateAvgRevPerOrder(selectedDate);
        updateDeletedOrders(selectedDate);
        updateSalesTable(selectedDate);
        }});

        // ----------------------------- Something for Y Axis -----------------------------//
        NumberAxis yAxis = (NumberAxis) chLineChart.getYAxis();
        yAxis.setTickUnit(1);          // each tick = 1
        yAxis.setMinorTickCount(0);   // no minor ticks


        // ----------------------------- Column highest lowest -----------------------------//

        colMealName.setCellValueFactory(new PropertyValueFactory<>("mealName"));
        colMealSales.setCellValueFactory(new PropertyValueFactory<>("sales"));



        salesTable.setRowFactory(tableView -> {
        TableRow<MealSales> row = new TableRow<>(); // match the TableView's type

        row.setOnMouseClicked(event -> {
        if (!row.isEmpty() && event.getClickCount() == 2) { // double click
            int index = row.getIndex();

            if (row.isSelected()) {
                // clicked again twice to deselect
                salesTable.getSelectionModel().clearSelection(index);
            }
        }
        });

        return row; // returns TableRow<MealSales>
        });
        
        return null;
    }


    // ----------------------------- Navigation -----------------------------//
    // Navigation methods are defined outside initialize()
    @FXML
    private void logoutBtn(ActionEvent event) throws java.io.IOException {
        SceneController.switchScene(event, "/FXML/LoginScene.fxml", null); // Switch to Login Scene
    }

    @FXML
    private void homeBtn(ActionEvent event) throws java.io.IOException {
        SceneController.switchScene(event, "/FXML/Homepage/Homepage.fxml", null); // Switch to Home Scene
    }

    @FXML
    private void AccountsBtn(ActionEvent event) throws java.io.IOException {
        SceneController.switchScene(event, "/FXML/EmployeePage/StaffMembers.fxml", null); // Switch to Accounts Scene
    }

    @FXML
    private void ordersBtn(ActionEvent event) throws java.io.IOException {
        SceneController.switchScene(event, "/FXML/OrdersPage/OrderPageManagerView.fxml", null); // Switch to Orders
    }

    @FXML
    private void inventoryBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/Inventory.fxml", null); // Switch to Inventory
    }

    // -------------------------- for line chart ---------------------------------------------//
    private void updateLineChart(LocalDate date) {
    chLineChart.getData().clear();

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("Items Sold");

    // Join sales with meal to get meal names
    String sql = "SELECT m.mealname, COUNT(a.saleid) AS total " +
                 "FROM analytics a " +
                 "JOIN meal m ON a.mealid = m.mealid " +
                 "WHERE a.transactiondate = ? " +
                 "GROUP BY m.mealname";

    try (Connection conn = Database.connect();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setDate(1, java.sql.Date.valueOf(date));
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            String mealName = rs.getString("mealname");
            int total = rs.getInt("total");
            series.getData().add(new XYChart.Data<>(mealName, total));
        }

        chLineChart.getData().add(series);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // -------------------------- gross revenue ---------------------------------------------//
    private void updateGrossRevenue(LocalDate date) {
    String sql = "SELECT SUM(m.mealprice) AS total_revenue " +
                 "FROM analytics a " +
                 "JOIN meal m ON a.mealid = m.mealid " +
                 "WHERE a.transactiondate = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setDate(1, java.sql.Date.valueOf(date));
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            double total = rs.getDouble("total_revenue");
            labelGrossRev.setText("₱ " + total);
        } else {
            labelGrossRev.setText("₱ 0");
        }

    } catch (Exception e) {
        e.printStackTrace();
        labelGrossRev.setText("₱ 0");
    }}


    // -------------------------------orders completed ----------------------------------------//
    private void updateOrdersCompleted(LocalDate date) {
    String sql = "SELECT COUNT(*) AS total FROM analytics WHERE transactiondate = ?";

    try (Connection conn = Database.connect();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setDate(1, java.sql.Date.valueOf(date));
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            int total = rs.getInt("total");
            labelOrdersCompleted.setText(String.valueOf(total));
        } else {
            labelOrdersCompleted.setText("0");
        }

    } catch (Exception e) {
        e.printStackTrace();
        labelOrdersCompleted.setText("0");
    }}


    // ---------------------------------Ave revenue per order--------------------------------------//
    private void updateAvgRevPerOrder(LocalDate date) {
    String sqlRevenue = "SELECT SUM(m.mealprice) AS total_revenue " +
                        "FROM analytics a " +
                        "JOIN meal m ON a.mealid = m.mealid " +
                        "WHERE a.transactiondate = ?";

    String sqlOrders = "SELECT COUNT(*) AS total FROM analytics WHERE transactiondate = ?";

    try (Connection conn = Database.connect()) {

        double totalRevenue = 0;
        // Total revenue
        try (PreparedStatement pst = conn.prepareStatement(sqlRevenue)) {
            pst.setDate(1, java.sql.Date.valueOf(date));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                totalRevenue = rs.getDouble("total_revenue");
            }
        }

        int totalOrders = 0;
        // Total orders
        try (PreparedStatement pst = conn.prepareStatement(sqlOrders)) {
            pst.setDate(1, java.sql.Date.valueOf(date));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                totalOrders = rs.getInt("total"); // <-- use "total" here
            }
        }

        // Calculate average revenue per order
        double avgRevenue = totalOrders > 0 ? totalRevenue / totalOrders : 0;       //checks ig naay order, if true then yeah if false 0

        labelAvgRevOrder.setText(String.format("%.2f", avgRevenue));        // double decimal places

    } catch (Exception e) {
        e.printStackTrace();
        labelAvgRevOrder.setText("0");}}


    // ----------------------------------Deleted Orders------------------------------------//
        private void updateDeletedOrders(LocalDate date) {
    String sql = "SELECT COUNT(*) AS total_cancelled " +
                 "FROM orders o " +
                 "JOIN analytics a ON o.saleid = a.saleid " +
                 "WHERE a.transactiondate = ? AND o.iscancelled = TRUE";

    try (Connection conn = Database.connect();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setDate(1, java.sql.Date.valueOf(date));
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            int totalCancelled = rs.getInt("total_cancelled");
            labelCancelledOrders.setText(String.valueOf(totalCancelled));
        } else {
            labelCancelledOrders.setText("0");
        }

    } catch (Exception e) {
        e.printStackTrace();
        labelCancelledOrders.setText("0");
    }} 


        // ----------------------------------highest lowest shit thats gon make me sewerslide-----------------------------------//
    private void updateSalesTable(LocalDate date) {
    salesTable.getItems().clear();

    String sql =
        "SELECT m.mealname, COALESCE(COUNT(a.saleid), 0) AS totalSales " +
        "FROM meal m " +
        "LEFT JOIN analytics a ON m.mealid = a.mealid AND a.transactiondate = ? " +
        "GROUP BY m.mealname " +
        "ORDER BY totalSales DESC";

    try (Connection conn = Database.connect();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setDate(1, java.sql.Date.valueOf(date));
        ResultSet rs = pst.executeQuery();

        List<MealSales> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new MealSales(
                rs.getString("mealname"),
                rs.getInt("totalSales")
            ));
        }

        salesTable.getItems().addAll(list);

    } catch (Exception e) {
        e.printStackTrace();
    }}


}