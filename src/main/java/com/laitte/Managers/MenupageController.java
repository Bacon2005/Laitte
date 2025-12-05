package com.laitte.Managers;

import com.laitte.Model.MenuItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.UUID;

public class MenupageController {

    @FXML private ScrollPane scrollPaneMenu;
    @FXML private VBox vboxMenuItems;
    @FXML private ComboBox<String> mealType;
    @FXML private ComboBox<String> mealCategory;
    @FXML private Button minusButton;
    @FXML private Button plusButton;

    private final ObservableList<MenuItem> masterData = FXCollections.observableArrayList();
    private final FilteredList<MenuItem> filteredData = new FilteredList<>(masterData, p -> true);
    private double currentScale = 1.0;

    @FXML
    public void initialize() {

        mealType.getItems().addAll("All", "Vegan", "Non-Vegan", "Drinks");
        mealCategory.getItems().addAll("All", "Breakfast", "Lunch", "Dinner", "Drinks");

        mealType.getSelectionModel().selectFirst();
        mealCategory.getSelectionModel().selectFirst();

        mealType.setOnAction(e -> applyFilters());
        mealCategory.setOnAction(e -> applyFilters());

        plusButton.setOnAction(e -> zoomCards(0.1));
        minusButton.setOnAction(e -> zoomCards(-0.1));

        // Sample menu items
        masterData.addAll(
            new MenuItem(generateId(), "Fruit Oat Pancakes", "Fluffy pancakes made with oats, bananas, blueberries, and strawberries.", 225, "Breakfast", "Vegan", "/Images/Menu/Fruit_Oat_Pancakes.jpg"),
            new MenuItem(generateId(), "Chicken Breakfast Wrap", "Tortilla wrap filled with scrambled eggs, seasoned chicken strips, and veggies.", 175, "Breakfast", "Non-Vegan", "/Images/Menu/Chicken_Breakfast_Wrap.jpg"),
            new MenuItem(generateId(), "Vegan Buddha Bowl", "A nourishing mix of quinoa, avocado, roasted veggies, and herbs—finished with toasted almonds for crunch.", 260, "Lunch", "Vegan", "/Images/Menu/Vegan_Buddha_Bowl.jpg"),
            new MenuItem(generateId(), "Grilled Chicken Salad", "Crispy romaine with creamy Caesar dressing, croutons, parmesan, and juicy grilled chicken.", 240, "Lunch", "Non-Vegan", "/Images/Menu/Grilled_Chicken_Salad.jpg"),
            new MenuItem(generateId(), "Vegan Stir-Fry Noodles", "Colorful vegetables and tender noodles tossed in a savory Asian-style sauce with a fresh lime accent.", 200, "Dinner", "Vegan", "/Images/Menu/Vegan_Stir_Fry_Noodles.jpg"),
            new MenuItem(generateId(), "Beef Teriyaki Rice Bowl", "A flavorful bowl of tender beef, fresh veggies, and rice, topped with sesame and pickled ginger.", 250, "Dinner", "Non-Vegan", "/Images/Menu/Beef_Teriyaki_Rice_Bowl.jpg"),
            new MenuItem(generateId(), "Mineral Water", "Pure, clean, and ice-cold mineral water to keep you refreshed anytime.", 40, "Drinks", "Drinks", "/Images/Menu/Mineral_Water.jpg"),
            new MenuItem(generateId(), "Iced Lemon Tea", "Refreshing brewed iced tea served with ice and bright lemon slices for a crisp, citrusy sip.", 60, "Drinks", "Drinks", "/Images/Menu/Iced_Lemon_Tea.jpg"),
            new MenuItem(generateId(), "Café Latte", "Ice tea with fresh lemon slices.", 150, "Drinks", "Drinks", "/Images/Menu/Café_Latte.jpg"),
            new MenuItem(generateId(), "Iced Coffee", "Crafted from freshly brewed beans, chilled and poured over crystal-clear ice for a crisp, vibrant flavor." , 120, "Drinks", "Drinks", "/Images/Menu/Iced_Coffee.jpg")
        );

        reloadCards();
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

    private void reloadCards() {
        vboxMenuItems.getChildren().clear();

        for (MenuItem mi : filteredData) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MenuCard.fxml"));
                Node card = loader.load();

                MenuCardController c = loader.getController();
                c.setData(mi);
                c.showRemoveButton(false);
                c.setOnRemove(() -> removeMenuItemById(mi.getId()));

                card.setScaleX(currentScale);
                card.setScaleY(currentScale);

                vboxMenuItems.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void zoomCards(double delta) {
        currentScale = Math.max(0.6, Math.min(1.6, currentScale + delta));
        for (Node n : vboxMenuItems.getChildren()) {
            n.setScaleX(currentScale);
            n.setScaleY(currentScale);
        }
    }

    public void setManagerMode(boolean isManager) {
        vboxMenuItems.getChildren().clear();

        for (MenuItem mi : filteredData) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MenuCard.fxml"));
                Node card = loader.load();

                MenuCardController c = loader.getController();
                c.setData(mi);
                c.showRemoveButton(isManager);
                c.setOnRemove(() -> removeMenuItemById(mi.getId()));

                card.setScaleX(currentScale);
                card.setScaleY(currentScale);

                vboxMenuItems.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addMenuItem(MenuItem item) {
        if (item.getId() == null) item.setId(generateId());
        masterData.add(item);
        Platform.runLater(this::reloadCards);
    }

    public boolean removeMenuItemById(String id) {
        boolean removed = masterData.removeIf(mi -> mi.getId().equals(id));
        if (removed) Platform.runLater(this::reloadCards);
        return removed;
    }

    // ============================
    // FXML Button Handlers
    // ============================

    @FXML
    private void goToHomePage(ActionEvent event) {
        try {
            SceneController.switchToHomepage(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToOrdersPage(ActionEvent event) {
        try {
            SceneController.switchToOrders(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToInventoryPage(ActionEvent event) {
        try {
            SceneController.switchToInventory(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAnalyticsPage(ActionEvent event) {
        try {
            SceneController.switchToAnalytics(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logoutBtn(ActionEvent event) {
        try {
            SceneController.switchToLoginScene(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
