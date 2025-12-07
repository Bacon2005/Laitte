package com.laitte.Managers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.laitte.LaitteMain.Database;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
    // -------------------------------//

    public void initialize() {
        if (Session.getManager()) { // if they are manager they can see the accounts
            Accounts.setVisible(true); // if they are not manager they cant see the accounts button
        } else {
            Accounts.setVisible(false);
        }

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
        nameLabel.setText("Hello, " + Session.getUsername()); // Set username from session

        // notificationPane.setVisible(false);

        totalOrders();
        pendingOrders();
        completedOrders();
        cancelledOrders();
    }

    private void totalOrders() {
        String query = "SELECT COUNT(*) AS totalOrders FROM analytics";

        try (Connection conn = Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int total = rs.getInt("totalOrders");
                totalOrders.setText(String.valueOf(total));
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------------Navigation----------------------------//
    @FXML
    private void logoutBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/LoginScene.fxml", null); // Switch to Login Scene
    }

    @FXML
    private void AccountsBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/StaffMembers.fxml", null); // Switch to Accounts Scene
    }

    @FXML
    private void inventoryBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/Inventory.fxml", null); // Switch to Inventory
    }

    @FXML
    private void homeBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/ManagerHomepage.fxml", null); // Switch to home
    }
}
