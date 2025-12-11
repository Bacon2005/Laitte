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

public class SettingsController {
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
    private Button ordersBtn;
    @FXML
    private ImageView profilePic;
    @FXML
    private Button settingBtn;
    @FXML
    private AnchorPane slider;
    @FXML
    private ImageView profilePicView;
    @FXML
    private AnchorPane rootPane;

    // -------------------------------------------//
    @FXML
    private Label displayUsername;
    @FXML
    private Label name;
    @FXML
    private Label role;
    // ------------------------------------------//

    public void initialize() {
        slider.setVisible(true); // ensures that Slider is visible and can be interacted with

        if (Session.getManager()) {
            Accounts.setVisible(true);
        } else {
            Accounts.setVisible(false);
        }

        Circle clip = new Circle();
        clip.radiusProperty().bind(profilePicView.fitWidthProperty().divide(2));
        clip.centerXProperty().bind(profilePicView.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(profilePicView.fitHeightProperty().divide(2)); // centerX, centerY, radius
        profilePicView.setClip(clip);

        Image settingsPic = new Image(Session.getProfileImage());
        profilePicView.setImage(settingsPic);

        Circle clip2 = new Circle(50, 50, 50); // centerX, centerY, radius
        profilePic.setClip(clip2);
        profilePic.setImage(settingsPic);

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

        displayUsername.setText(Session.getUsername());
        name.setText(Session.getFirstname() + " " + Session.getLastname());
        role.setText(Session.getRole());
    }

    // ------------------------------------------------------ Navigation Buttons
    // ---------------------//
    @FXML
    void AccountsBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/EmployeePage/StaffMembers.fxml", null); // Switch to Orders
    }

    @FXML
    void inventoryBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/Inventory.fxml", null); // Switch to Orders
    }

    @FXML
    void logoutBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/LoginScene.fxml", null); // Switch to Orders
    }

    @FXML
    void ordersBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/OrdersPage/OrderPageManagerView.fxml", null); // Switch to Orders
    }

    @FXML
    void settingsBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/SettingsPage/Settings.fxml", null); // Switch to Orders
    }

    @FXML
    void homeBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/Homepage/Homepage.fxml", null); // Switch to Orders
    }
    // -----------------------------------------------------------------------//
}