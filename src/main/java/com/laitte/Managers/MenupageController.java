package com.laitte.Managers;

import java.io.IOException;
import java.util.*;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.laitte.Model.MenuItem;
import com.laitte.Managers.Menu.MenuCardController;
import com.laitte.Managers.Menu.ConfirmationPopUpController;
import com.laitte.Managers.Menu.EditOrderController;

public class MenupageController {

    @FXML private ScrollPane scrollPaneMenu;
    @FXML private VBox vboxMenuItems;
    @FXML private ComboBox<String> mealType;
    @FXML private ComboBox<String> mealCategory;

    @FXML private VBox vboxSelectedItems;
    @FXML private Button editOrdersButton;
    @FXML private Label totalAmountLabel;

    @FXML private javafx.scene.layout.AnchorPane slider;
    @FXML private javafx.scene.layout.AnchorPane rootPane;
    @FXML private javafx.scene.image.ImageView profilePic;

    private final ObservableList<MenuItem> masterData = FXCollections.observableArrayList();
    private final FilteredList<MenuItem> filteredData = new FilteredList<>(masterData, p -> true);

    private final Map<String, EditOrderController> selectedItemControllers = new LinkedHashMap<>();
    private boolean editingMode = false;

    @FXML
    public void initialize() {
        // Setup meal type/category filters
        mealType.getItems().addAll("All", "Vegan", "Non-Vegan", "Drinks");
        mealCategory.getItems().addAll("All", "Breakfast", "Lunch", "Dinner", "Drinks");
        mealType.getSelectionModel().selectFirst();
        mealCategory.getSelectionModel().selectFirst();

        mealType.setOnAction(e -> applyFilters());
        mealCategory.setOnAction(e -> applyFilters());

        // Add sample menu items
        masterData.addAll(
            new MenuItem(generateId(), "Fruit Oat Pancakes", "Fluffy pancakes with oats, bananas, blueberries, strawberries.", 225, "Breakfast", "Vegan", "/Images/Menu/Fruit Oat Pancakes.jpg", "/Images/Icons/Veg.png", "/Images/Icons/Breakfast.png"),
            new MenuItem(generateId(), "Chicken Breakfast Wrap", "Tortilla wrap with scrambled eggs, chicken strips, and veggies.", 175, "Breakfast", "Non-Vegan", "/Images/Menu/Chicken Breakfast Wrap.jpg", "/Images/Icons/Non Veg.png", "/Images/Icons/Breakfast.png"),
            new MenuItem(generateId(), "Vegan Buddha Bowl", "Quinoa, avocado, roasted veggies, herbs, toasted almonds.", 260, "Lunch", "Vegan", "/Images/Menu/Vegan Buddha Bowl.jpg", "/Images/Icons/Veg.png", "/Images/Icons/Lunch.png"),
            new MenuItem(generateId(), "Grilled Chicken Salad", "Romaine, Caesar dressing, croutons, parmesan, grilled chicken.", 240, "Lunch", "Non-Vegan", "/Images/Menu/Grilled Chicken Salad.jpg", "/Images/Icons/Non Veg.png", "/Images/Icons/Lunch.png"),
            new MenuItem(generateId(), "Vegan Stir-Fry Noodles", "Colorful vegetables and tender noodles tossed in a savory Asian-style sauce with a fresh lime accent.", 220, "Dinner", "Vegan", "/Images/Menu/Vegan Stir Fry Noodles.jpg", "/Images/Icons/Non Veg.png", "/Images/Icons/Dinner.png"),
            new MenuItem(generateId(), "Beef Teriyaki Rice Bowl", "A flavorful bowl of tender beef, fresh veggies, and rice, topped with sesame and pickled ginger.", 260, "Dinner", "Non-Vegan", "/Images/Menu/Beef Teriyaki Rice Bowl.jpg", "/Images/Icons/Veg.png", "/Images/Icons/Dinner.png"),
            new MenuItem(generateId(), "Mineral Water", "Pure, clean, and ice-cold mineral water to keep you refreshed anytime.", 40, "Drinks", "Drinks", "/Images/Menu/Mineral Water.jpg", "/Images/Icons/Drinks.png", "/Images/Icons/Drinks.png"),
            new MenuItem(generateId(), "Iced Lemon Tea", "Refreshing brewed iced tea served with ice and bright lemon slices for a crisp, citrusy sip.", 60, "Drinks", "Drinks", "/Images/Menu/Iced Lemon Tea.jpg", "/Images/Icons/Drinks.png", "/Images/Icons/Drinks.png"),
            new MenuItem(generateId(), "Café Latte", "Smooth espresso blended with creamy steamed milk, topped with delicate latte art.", 120, "Drinks", "Drinks", "/Images/Menu/Café Latte.jpg", "/Images/Icons/Drinks.png", "/Images/Icons/Drinks.png"),
            new MenuItem(generateId(), "Iced Coffee", "Crafted from freshly brewed beans, chilled and poured 0ver crystal-clear ice for crisp, vibrant flavor.", 150, "Drinks", "Drinks", "/Images/Menu/Iced Coffee.jpg", "/Images/Icons/Drinks.png", "/Images/Icons/Drinks.png")
        );

        reloadCards();

        if (editOrdersButton != null) {
            editOrdersButton.setOnAction(e -> toggleEditMode());
        }

        updateTotal();

        // Setup slider animation
        setupSliderAnimation();
    }

    private void setupSliderAnimation() {
        slider.setVisible(true);

        // round profile picture
        Circle clip = new Circle(50, 50, 50);
        profilePic.setClip(clip);

        double hiddenX = -200;
        slider.setTranslateX(hiddenX);

        TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.3), slider);
        slideIn.setToX(0);

        TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.3), slider);
        slideOut.setToX(hiddenX);

        rootPane.setOnMouseMoved(event -> {
            if (event.getX() <= 50) {
                slideIn.play();
            }
        });

        slider.setOnMouseExited(event -> slideOut.play());
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    private void applyFilters() {
        String selectedMealType = mealType.getSelectionModel().getSelectedItem();
        String selectedCategory = mealCategory.getSelectionModel().getSelectedItem();

        filteredData.setPredicate(item -> {
            boolean matchMeal = "All".equals(selectedMealType) || item.getMealType().equalsIgnoreCase(selectedMealType);
            boolean matchCategory = "All".equals(selectedCategory) || item.getCategory().equalsIgnoreCase(selectedCategory);
            return matchMeal && matchCategory;
        });

        reloadCards();
    }

    // ------------------- UPDATED RELOADCARDS ------------------- //
    private void reloadCards() {
        vboxMenuItems.getChildren().clear();
        for (MenuItem mi : filteredData) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Menu/MenuCard.fxml"));
                Node card = loader.load();
                MenuCardController c = loader.getController();

                // Set content dynamically
                c.setMenuCardContent(
                    mi.getName(),                       // Food name
                    mi.getDescription(),                // Description
                    "₱" + mi.getPrice(),                // Price formatted
                    mi.getCategory(),                   // Meal category
                    new Image(getClass().getResourceAsStream(mi.getCategoryIconPath())), // Category icon
                    mi.getMealType(),                   // Vegan / Non-Vegan / Drinks
                    new Image(getClass().getResourceAsStream(mi.getTypeIconPath())),     // Type icon
                    new Image(getClass().getResourceAsStream(mi.getImagePath()))         // Food image
                );

                // Add button functionality
                c.addButton.setOnAction(e -> showConfirmationPopup(mi));

                vboxMenuItems.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showConfirmationPopup(MenuItem menuItem) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Menu/ConfirmationPopUp.fxml"));
            Parent root = loader.load();
            ConfirmationPopUpController popController = loader.getController();
            popController.setMenuItem(menuItem);
            popController.setOnConfirm(() -> addOrIncrementOrder(menuItem));
            popController.setOnCancel(() -> {});

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void addOrIncrementOrder(MenuItem menuItem) {
        String id = menuItem.getId();
        if (selectedItemControllers.containsKey(id)) {
            EditOrderController existing = selectedItemControllers.get(id);
            existing.incrementQuantity(1);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Menu/EditOrder.fxml"));
                Node card = loader.load();
                EditOrderController ctrl = loader.getController();
                ctrl.setData(menuItem, 1);

                ctrl.setOnRemove(() -> {
                    vboxSelectedItems.getChildren().remove(card);
                    selectedItemControllers.remove(menuItem.getId());
                    updateTotal();
                });

                ctrl.setOnQuantityChanged(this::updateTotal);

                selectedItemControllers.put(id, ctrl);
                vboxSelectedItems.getChildren().add(card);
                ctrl.setMode(editingMode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        updateTotal();
    }

    private void toggleEditMode() {
        editingMode = !editingMode;
        selectedItemControllers.values().forEach(c -> c.setMode(editingMode));
        editOrdersButton.setText(editingMode ? "DONE EDITING" : "EDIT ORDERS");
    }

    private void updateTotal() {
        double total = selectedItemControllers.values().stream()
                .mapToDouble(c -> c.getMenuItem().getPrice() * c.getQuantity())
                .sum();
        totalAmountLabel.setText(String.format("₱%.2f", total));
    }

    // ----------------------------Navigation----------------------------//
    @FXML
    private void logoutBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/LoginScene.fxml", null);
    }

    @FXML
    private void AccountsBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/EmployeePage/StaffMembers.fxml", null);
    }

    @FXML
    private void inventoryBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/Inventory.fxml", null);
    }

    @FXML
    private void homeBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/Homepage/Homepage.fxml", null);
    }

    @FXML
    private void menuBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/Menu/MenuPage.fxml", null);
    }
}
