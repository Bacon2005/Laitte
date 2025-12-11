package com.laitte.Managers.Menu.CurrentOrder;

import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class currentOrderPaneController {

    @FXML
    private Label mealLabel;

    @FXML
    private Label mealPrice;

    @FXML
    private Label totalPrice;

    @FXML
    private Label quantityLabel;

    @FXML
    private ImageView mealImage;

    private Consumer<Void> onQuantityChange;

    private int quantity = 0;
    private int price;
    private int mealId;

    // Called when the FXML is loaded
    public void setData(String name, int price, int quantity, Image mealImage, int mealid) {
        this.price = price;
        this.quantity = quantity;
        this.mealId = mealid;

        mealLabel.setText(name);
        mealPrice.setText(String.valueOf(price));
        totalPrice.setText(String.valueOf(price * quantity));
        quantityLabel.setText("x" + String.valueOf(quantity));

        if (mealImage != null) {
            this.mealImage.setImage(mealImage);
        }
    }

    public void changeQuantity(int delta) {
        System.out.println("Meal: " + getMealName() + ", ID: " + getMealId() + ", Quantity: " + getQuantity() + ", Total: " + getTotalPrice());
        quantity += delta;
        if (quantity < 0)
            quantity = 0;

        quantityLabel.setText("x" + quantity);
        totalPrice.setText(String.valueOf(price * quantity));

        // Notify MenuPageController or whoever is listening
        if (onQuantityChange != null) {
            onQuantityChange.accept(null);
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMealName() {
        return mealLabel.getText();
    }

    public int getTotalPrice() {
        return price * quantity;
    }

    public int getMealId(){
        return mealId;
    }

    public void setOnQuantityChange(Consumer<Void> listener) {
        this.onQuantityChange = listener;
    }
}
