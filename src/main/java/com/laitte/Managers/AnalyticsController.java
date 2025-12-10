package com.laitte.Managers;


import com.laitte.LaitteMain.Database;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableRow;

public class AnalyticsController {

    @FXML private Button Accounts;
    @FXML private Button analytics;
    @FXML private AnchorPane ancpnMealImg;
    @FXML private LineChart<?, ?> chLineChart;
    @FXML private ComboBox<?> comboxHLSalesCat;
    @FXML private DatePicker datepicker;
    @FXML private Button home;
    @FXML private Button inventory;
    @FXML private Label labelAvgRevOrder;
    @FXML private Label labelGrossRev;
    @FXML private Label labelMealCategory;
    @FXML private Label labelMealDescription;
    @FXML private Label labelMealID;
    @FXML private Label labelMealName;
    @FXML private Label labelMealPrice;
    @FXML private Label labelMealType;
    @FXML private Label labelNewCustomers;
    @FXML private Label labelOrdersCompleted;
    @FXML private Button logoutBtn;

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
    void AccountsBtn(ActionEvent event) {

    }

    @FXML
    void homeBtn(ActionEvent event) {

    }

    @FXML
    void logoutBtn(ActionEvent event) {

    }

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
}
