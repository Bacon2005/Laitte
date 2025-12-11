package com.laitte.Managers;

import java.io.IOException;
import java.util.List;

import com.laitte.Managers.Menu.Meal;
import com.laitte.Managers.Menu.MealDAO;
import com.laitte.Managers.Menu.mealsPaneController;
import com.laitte.Managers.Menu.CurrentOrder.currentOrderPaneController;

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

public class MenupageController {

    @FXML
    private Button Accounts;
    @FXML
    private Button add;
    @FXML
    private Button addToOrder;
    @FXML
    private Button analytics;
    @FXML
    private Label category;
    @FXML
    private ImageView categoryImage;
    @FXML
    private Label counter;
    @FXML
    private Button home;
    @FXML
    private Button inventory;
    @FXML
    private Button logoutBtn;
    @FXML
    private ComboBox<String> mealCategory;
    @FXML
    private ComboBox<String> mealType;
    @FXML
    private ImageView mealImage;
    @FXML
    private Label mealName;
    @FXML
    private ImageView mealTypeImage;
    @FXML
    private Button menu;
    @FXML
    private Button minus;
    @FXML
    private Label nameLabel;
    @FXML
    private AnchorPane orderPanel;
    @FXML
    private Button orders;
    @FXML
    private Label price;
    @FXML
    private Label totalPrice;
    @FXML
    private Label subtotalPrice;
    @FXML
    private Label serviceFee;
    @FXML
    private Label price112;
    @FXML
    private Label price1121;
    @FXML
    private ImageView profilePic;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button settingBtn;
    @FXML
    private AnchorPane slider;
    @FXML
    private VBox MealsVbox;
    @FXML
    private VBox currentOrdersVbox;

    private final int SERVICE_FEE = 50;
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
        SceneController.switchScene(event, "/FXML/Homepage/Homepage.fxml", null); // Switch to Home Scene
    }
    // -----------------------------------------------------------------------//

    public void initialize() {
        loadMeals();
        totalPrice.setText(String.valueOf(SERVICE_FEE));
        serviceFee.setText(String.valueOf(SERVICE_FEE));
        subtotalPrice.setText("0");
        slider.setVisible(true);
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
    }

    private void loadMeals() {
        List<Meal> meals = MealDAO.getAllMeals();
        MealsVbox.getChildren().clear();

        for (Meal meal : meals) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DuplicatingPanels/meals.fxml"));
                AnchorPane pane = loader.load();

                mealsPaneController controller = loader.getController();

                controller.setData(
                        meal.getName(),
                        meal.getPrice(),
                        meal.getCategory(),
                        meal.getImagePath(),
                        meal.getType());

                controller.setCurrentOrdersVBox(currentOrdersVbox);
                controller.setMenuPageController(this);

                MealsVbox.getChildren().add(pane);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // public void updateTotal() {
    // int total = 0;

    // for (var node : currentOrdersVbox.getChildren()) {
    // if (node.getUserData() instanceof currentOrderPaneController) {
    // currentOrderPaneController controller = (currentOrderPaneController)
    // node.getUserData();
    // total += controller.getTotalPrice();
    // }
    // }

    // totalPrice.setText(String.valueOf(total));
    // serviceFee.setText(String.valueOf(SERVICE_FEE));
    // subtotalPrice.setText(String.valueOf(total + SERVICE_FEE));
    // }
}
