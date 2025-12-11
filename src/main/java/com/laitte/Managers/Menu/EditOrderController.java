package com.laitte.Managers.Menu;

import com.laitte.Model.MenuItem;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.InputStream;

public class EditOrderController {

    @FXML private ImageView selectedImageView;
    @FXML private Label titleLabel;
    @FXML private Label priceLabel;
    @FXML private Label xQuantityLabel;
    @FXML private Label subtotalLabel;

    @FXML private HBox defaultModePane;
    @FXML private HBox editModePane;

    @FXML private Button minusButton;
    @FXML private Label quantityLabel;
    @FXML private Button plusButton;
    @FXML private Button removeButton;

    private MenuItem menuItem;
    private int quantity = 1;
    private Runnable onRemove;
    private Runnable onQuantityChanged;
    private Node rootNode;

    public void setData(MenuItem item, int initialQty) {
        this.menuItem = item;
        this.quantity = Math.max(1, initialQty);

        titleLabel.setText(item.getName());
        priceLabel.setText(String.format("₱%.2f", item.getPrice()));
        quantityLabel.setText(String.valueOf(quantity));
        xQuantityLabel.setText(quantity + "x");
        subtotalLabel.setText(String.format("₱%.2f", item.getPrice() * quantity));

        try (InputStream is = getClass().getResourceAsStream(item.getImagePath())) {
            if (is != null) selectedImageView.setImage(new Image(is));
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        if (minusButton != null) minusButton.setOnAction(e -> changeQuantity(-1));
        if (plusButton != null) plusButton.setOnAction(e -> changeQuantity(1));
        if (removeButton != null) removeButton.setOnAction(e -> {
            if (onRemove != null) onRemove.run();
        });

        updateModeVisibility();
    }

    private void changeQuantity(int delta) {
        int newQty = Math.max(1, quantity + delta);
        if (newQty != quantity) {
            quantity = newQty;
            quantityLabel.setText(String.valueOf(quantity));
            xQuantityLabel.setText(quantity + "x");
            subtotalLabel.setText(String.format("₱%.2f", menuItem.getPrice() * quantity));
            if (onQuantityChanged != null) onQuantityChanged.run();
        }
    }

    public void incrementQuantity(int n) {
        changeQuantity(n);
    }

    public void setOnRemove(Runnable r) { this.onRemove = r; }
    public void setOnQuantityChanged(Runnable r) { this.onQuantityChanged = r; }

    public MenuItem getMenuItem() { return menuItem; }
    public int getQuantity() { return quantity; }

    public void setMode(boolean editingMode) {
        if (defaultModePane != null) defaultModePane.setVisible(!editingMode);
        if (editModePane != null) editModePane.setVisible(editingMode);
        updateModeVisibility();
    }

    private void updateModeVisibility() {
        // handled in setMode
    }
}
