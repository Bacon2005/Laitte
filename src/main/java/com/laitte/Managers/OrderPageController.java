package com.laitte.Managers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.laitte.LaitteMain.Database;
import com.laitte.Managers.Orders.Orders;
import com.laitte.Managers.Orders.OrdersDAO;
import com.laitte.Managers.Orders.OrdersPaneController;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class OrderPageController {
    // ----------Variables-------------//
    @FXML
    private Button Accounts;
    @FXML
    private Button analytics;
    @FXML
    private Button home;
    @FXML
    private Button inventory;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button menu;
    @FXML
    private Label nameLabel;
    @FXML
    private Button orders;
    @FXML
    private ImageView profilePic;
    @FXML
    private Button settingBtn;
    @FXML
    private AnchorPane slider;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label totalOrders;
    @FXML
    private Label pendingOrders;
    @FXML
    private Label completedOrders;
    @FXML
    private Label cancelledOrders;
    @FXML
    private Label ordersMonth;
    @FXML
    private VBox ordersVbox;
    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private AnchorPane notificationPane;
    @FXML
    private Button notificationBtn;
    // -------------------------------//

    public void initialize() {
        notificationPane.setVisible(false);

        if (Session.getManager()) { // if they are manager they can see the accounts
            Accounts.setVisible(true); // if they are not manager they cant see the accounts button
        } else {
            Accounts.setVisible(false);
        }

        // Add months to the ComboBox
        monthComboBox.getItems().addAll(
                "January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December");

        // Listen for month changes
        monthComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() >= 0) {
                ordersThisMonth(); // Updates the label
                reloadOrders(); // Reloads UI and data
            }
        });

        int currentMonth = java.time.LocalDate.now().getMonthValue();
        monthComboBox.getSelectionModel().select(currentMonth - 1);

        loadOrders();

        slider.setVisible(true); // ensures that Slider is visible and can be interacted with

        Circle clip = new Circle(50, 50, 50); // centerX, centerY, radius
        profilePic.setClip(clip);
        Image image = new Image(Session.getProfileImage());
        profilePic.setImage(image);

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
        nameLabel.setText("Hello, " + Session.getFirstname()); // Set username from session

        // notificationPane.setVisible(false);

        totalOrders();
        pendingOrders();
        completedOrders();
        cancelledOrders();
        ordersThisMonth();
    }

    private void totalOrders() {
        String query = "SELECT COUNT(*) AS totalOrders FROM orders";

        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int total = rs.getInt("totalOrders");
                totalOrders.setText(String.valueOf(total));
            } else {
                // No rows returned → display 0
                completedOrders.setText("0");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pendingOrders() {
        String query = """
                select count(ispending) as pendingOrders
                from orders
                where ispending = true;
                """;
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int pending = rs.getInt("pendingOrders");
                pendingOrders.setText(String.valueOf(pending));
            } else {
                // No rows returned → display 0
                completedOrders.setText("0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void completedOrders() {
        String query = """
                SELECT ispending, iscancelled, COUNT(*) AS completedOrders
                FROM orders
                WHERE (ispending IS NULL OR ispending = false)
                  AND (iscancelled IS NULL OR iscancelled = false)
                GROUP BY ispending, iscancelled;
                                                """;
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int completed = rs.getInt("completedOrders");
                completedOrders.setText(String.valueOf(completed));
            } else {
                // No rows returned → display 0
                completedOrders.setText("0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelledOrders() {
        String query = """
                select count(iscancelled) as cancelledOrders
                from orders
                where iscancelled = true;
                                                """;
        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int cancelled = rs.getInt("cancelledOrders");
                cancelledOrders.setText(String.valueOf(cancelled));
            } else {
                // No rows returned → display 0
                completedOrders.setText("0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ordersThisMonth() {
        int selectedMonth = monthComboBox.getSelectionModel().getSelectedIndex() + 1;

        // Ask DAO for count
        int totalOrders = OrdersDAO.countOrdersByMonth(selectedMonth);

        // Update label
        ordersMonth.setText(String.valueOf(totalOrders));
    }

    // ----------------------------Navigation----------------------------//
    @FXML
    private void logoutBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/LoginScene.fxml", null); // Switch to Login Scene
    }

    @FXML
    private void AccountsBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/EmployeePage/StaffMembers.fxml", null); // Switch to Accounts Scene
    }

    @FXML
    private void inventoryBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/Inventory.fxml", null); // Switch to Inventory
    }

    @FXML
    private void ordersBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/OrdersPage/OrderPageManagerView.fxml", null); // Switch to Orders
    }

    @FXML
    private void menuBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/MenuPage.fxml", null); // Switch to Menu
    }

    @FXML
    private void settingsBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/SettingsPage/Settings.fxml", null); // Switch to Settings
    }

    @FXML
    private void homeBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/Homepage/Homepage.fxml", null); // Switch to home
    }
    // -------------------------------------------------------------------//

    private void loadOrders() {
    List<Orders> orders = OrdersDAO.getAllOrders();

    ordersVbox.getChildren().clear();

    // Track which customers already have a panel
    Set<String> loadedCustomers = new HashSet<>();

    for (Orders ord : orders) {

        if (!ord.isPending()) continue; // Only pending orders

        String customerKey = ord.getCustomer();

        // Skip if we've already added a panel for this customer
        if (loadedCustomers.contains(customerKey)) continue;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DuplicatingPanels/OrderList.fxml"));
            AnchorPane pane = loader.load();

            OrdersPaneController controller = loader.getController();
            controller.setData(ord.getCustomer(), ord.getNum(), ord.isPending());

            ordersVbox.getChildren().add(pane);
            controller.setParentController(this);

            loadedCustomers.add(customerKey);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


    public void reloadOrders() {
        ordersVbox.getChildren().clear(); // clear existing order panes
        loadOrders(); // reload from database
        totalOrders();
        pendingOrders();
        completedOrders();
        cancelledOrders();
        ordersThisMonth();
    }

    @FXML
    private void notificationBtn(ActionEvent event) {
        notificationPane.setVisible(!notificationPane.isVisible());
    }

}
