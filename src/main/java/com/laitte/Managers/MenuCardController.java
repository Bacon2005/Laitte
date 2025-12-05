package com.laitte.Managers;

import com.laitte.Model.MenuItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.InputStream;

public class MenuCardController {

    @FXML private HBox root;
    @FXML private ImageView imageView;
    @FXML private ImageView mealTypeIcon;
    @FXML private Label titleLabel;
    @FXML private Label descLabel;
    @FXML private Label priceLabel;
    @FXML private Label categoryLabel;
    @FXML private Button removeButton;

    private MenuItem menuItem;
    private Runnable onRemove;

    @FXML
    public void initialize() {
        if (removeButton != null) {
            removeButton.setVisible(false);
            removeButton.setOnAction(e -> {
                if (onRemove != null) onRemove.run();
            });
        }
    }

    public void setData(MenuItem item) {
        this.menuItem = item;

        if (titleLabel != null) titleLabel.setText(item.getName());
        if (descLabel != null) descLabel.setText(item.getDescription());
        if (priceLabel != null) priceLabel.setText(String.format("₱%.0f", item.getPrice()));
        if (categoryLabel != null) categoryLabel.setText(item.getCategory());

        // Load menu image safely
        if (imageView != null && item.getImagePath() != null && !item.getImagePath().isEmpty()) {
            Image menuImage = loadImage(item.getImagePath());
            imageView.setImage(menuImage);
        }

        // Load meal type icon safely
        if (mealTypeIcon != null && item.getMealType() != null) {
            String iconPath = switch (item.getMealType().toLowerCase()) {
                case "vegan" -> "/Images/Icons/Vegan.png";
                case "non-vegan" -> "/Images/Icons/NonVegan.png";
                case "drinks" -> "/Images/Icons/Drinks.png";
                default -> null;
            };
            mealTypeIcon.setImage(iconPath != null ? loadImage(iconPath) : null);
        }
    }

    /**
     * Safely loads an image from the resources folder.
     * Prints a message if the image is not found.
     */
    private Image loadImage(String path) {
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            System.out.println("Image NOT found: " + path);
            return null;
        }
        return new Image(is);
    }

    public void setOnRemove(Runnable onRemove) {
        this.onRemove = onRemove;
    }

    public void showRemoveButton(boolean show) {
        if (removeButton != null) removeButton.setVisible(show);
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }
}
