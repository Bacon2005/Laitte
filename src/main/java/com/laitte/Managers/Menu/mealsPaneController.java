package com.laitte.Managers.Menu;

import java.io.IOException;

import com.laitte.Managers.MenupageController;
import com.laitte.Managers.Menu.CurrentOrder.currentOrderPaneController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class mealsPaneController {
    // -------------------------Variables-------------------------------//

    @FXML
    private ImageView mealImage;

    @FXML
    private ImageView mealTypeImage;

    @FXML
    private ImageView categoryImage;

    @FXML
    private Label mealName;

    @FXML
    private Label priceLabel;

    @FXML
    private Label category;

    @FXML
    private Button addToOrder;

    @FXML
    private Button minus;

    @FXML
    private Button add;

    @FXML
    private Label counter;

    @FXML
    private AnchorPane plusMinusPane;

    private int count = 1;

    private currentOrderPaneController currentOrderController; // reference to existing panel
    private AnchorPane currentOrderPane;

    private int mealId;

    public void initialize() {
        plusMinusPane.setVisible(false);
    }

    // ----------------------------- SET DATA -----------------------------

    public void setData(String name, int price, String mealCategory, String imagePath, String mealType, int mealId) {
        this.mealId = mealId; // store meal ID for use in addToOrder
        mealName.setText(name);
        priceLabel.setText(String.valueOf(price));
        category.setText(mealCategory);

        // Load meal image
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(getClass().getResource("/images/" + imagePath).toExternalForm());
            mealImage.setImage(image);
        }

        // Meal type icon (example: Vegan.png)
        if (mealType != null) {
            String typePath = "/Images/Icons/" + mealType + ".png";
            mealTypeImage.setImage(new Image(typePath));
        }

        // Category icon (example: Breakfast.png)
        if (mealCategory != null) {
            String categoryPath = "/Images/Icons/" + mealCategory + ".png";
            categoryImage.setImage(new Image(categoryPath));
        }
    }

    // ----------------------------- LOGIC -----------------------------
    int quantity = 1;
    private VBox currentOrdersVBox;

    public void setCurrentOrdersVBox(VBox vbox) {
        this.currentOrdersVBox = vbox;
    }

    private MenupageController menuPageController;

    public void setMenuPageController(MenupageController controller) {
        this.menuPageController = controller;
    }

   @FXML
private void addToOrderBtn(ActionEvent event) {
    System.out.println("Adding to order: " + mealName.getText() + ", ID: " + mealId + ", Price: " + priceLabel.getText());


    plusMinusPane.setVisible(true);
    addToOrder.setVisible(false);

    if (currentOrderController != null) {
        currentOrderController.changeQuantity(1);
        if (menuPageController != null) menuPageController.updateTotal();
        return;
    }

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DuplicatingPanels/currentOrder.fxml"));
        currentOrderPane = loader.load();
        currentOrderController = loader.getController();

        currentOrderController.setData(
                mealName.getText(),
                Integer.parseInt(priceLabel.getText()),
                0,
                mealImage.getImage(),
                mealId
        );

        // Increment once for the initial add
        currentOrderController.changeQuantity(1);

        // Add to VBox
        if (currentOrdersVBox != null) {
            currentOrdersVBox.getChildren().add(currentOrderPane);
            currentOrderPane.setUserData(currentOrderController);
        }

        // <-- Add this to update subtotal and total
        if (menuPageController != null) {
            menuPageController.updateTotal();
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
private void addMeal(ActionEvent event) {
    if (currentOrderController != null) {
        currentOrderController.changeQuantity(1);
        if (menuPageController != null) {
            menuPageController.updateTotal(); // Update subtotal whenever quantity changes
        }
    }
}

@FXML
private void remove(ActionEvent event) {
    if (currentOrderController != null) {
        currentOrderController.changeQuantity(-1);

        if (currentOrderController.getQuantity() <= 0) {
            currentOrdersVBox.getChildren().remove(currentOrderPane);
            currentOrderController = null;
            currentOrderPane = null;
            plusMinusPane.setVisible(false);
            addToOrder.setVisible(true);
        }

        if (menuPageController != null) {
            menuPageController.updateTotal(); // Update subtotal
        }
    }
}

}
