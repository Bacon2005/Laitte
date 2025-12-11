package com.laitte.Managers.Menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MenuCardController {

    @FXML
    private ImageView foodImage;
    @FXML
    private ImageView foodTypeIcon;
    @FXML
    private ImageView categoryIcon;

    @FXML
    private Label foodName;
    @FXML
    private Label foodDescription;
    @FXML
    private Label foodPrice;
    @FXML
    private Label foodCategory;

    @FXML
    public Button addButton;  // public for MenupageController access

    public void setMenuCardContent(String name, String description, String price,
                                   String category, Image categoryIconImage,
                                   String type, Image typeIconImage, Image foodImg) {

        foodName.setText(name);
        foodDescription.setText(description);
        foodPrice.setText(price);
        foodCategory.setText(category);

        if (foodImg != null) foodImage.setImage(foodImg);
        if (typeIconImage != null) foodTypeIcon.setImage(typeIconImage);
        if (categoryIconImage != null) categoryIcon.setImage(categoryIconImage);
    }

    @FXML
    public void initialize() {
        // optional default action
        addButton.setOnAction(e -> System.out.println(foodName.getText() + " added!"));
    }
}
