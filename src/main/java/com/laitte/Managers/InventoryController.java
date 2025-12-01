package com.laitte.Managers;

import java.io.IOException;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class InventoryController {


    public void initialize() {
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
    }

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
    private AnchorPane rootPane;

    @FXML
    private Button settingBtn;

    @FXML
    private AnchorPane slider;

    @FXML
    void logoutBtn(ActionEvent event) {}
    

}
