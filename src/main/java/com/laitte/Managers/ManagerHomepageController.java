package com.laitte.Managers;

import java.io.IOException;


import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class ManagerHomepageController {

    // --------------------------- Variables for FXML ----------------------------//

    @FXML
    Label nameLabel; // adding this comment
    @FXML
    Button logoutBtn;
    @FXML
    private Label Menu;
    @FXML
    private Label MenuClose;
    @FXML
    private Button analytics;
    @FXML
    private Button inventory;
    @FXML
    private Button Accounts; // Added Accounts button
    @FXML
    private AnchorPane rootPane;
    @FXML
    private AnchorPane slider;
    @FXML
    private ImageView profilePic;
    @FXML
    private Button notificationBtn;
    @FXML
    private AnchorPane notificationPane;
    @FXML
    private Button ordersBtn;
    @FXML
    private Button settingsBtn;

    // --------------------------------------------------------------------------//

    public void initialize() {
        if (Session.getManager()) {
            Accounts.setVisible(true);
        } else {
            Accounts.setVisible(false);
        }
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

        notificationPane.setVisible(false);
    }

    // ----------------------------- Navigation -----------------------------//

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

    @FXML
    private void analyticsBtn(ActionEvent event) throws IOException{
        SceneController.switchScene(event, "/FXML/AnalyticsPage.fxml", null);
    }

    // -----------------------------------------------------------------------//

    @FXML
    private void notificationBtn(ActionEvent event) {
        notificationPane.setVisible(!notificationPane.isVisible());
    }

}
