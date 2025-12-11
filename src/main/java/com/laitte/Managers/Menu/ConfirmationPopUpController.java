package com.laitte.Managers.Menu;

import com.laitte.Model.MenuItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.InputStream;

public class ConfirmationPopUpController {

    @FXML private ImageView popupImage;
    @FXML private Label messageLabel;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;
    @FXML private Button closeButton;

    private Runnable onConfirm;
    private Runnable onCancel;
    private MenuItem menuItem;

    public void setMenuItem(MenuItem item) {
        this.menuItem = item;
        messageLabel.setText(String.format("Add '%s' (₱%.2f) to your order?", item.getName(), item.getPrice()));
        try (InputStream is = getClass().getResourceAsStream(item.getImagePath())) {
            if (is != null) popupImage.setImage(new Image(is));
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        if (confirmBtn != null) {
            confirmBtn.setOnAction(e -> {
                if (onConfirm != null) onConfirm.run();
                closeWindow();
            });
        }

        if (cancelBtn != null) {
            cancelBtn.setOnAction(e -> {
                if (onCancel != null) onCancel.run();
                closeWindow();
            });
        }

        if (closeButton != null) {
            closeButton.setOnAction(e -> closeWindow());
        }
    }

    private void closeWindow() {
        Stage s = (Stage) (confirmBtn != null ? confirmBtn.getScene().getWindow() : (cancelBtn != null ? cancelBtn.getScene().getWindow() : null));
        if (s != null) s.close();
    }

    public void setOnConfirm(Runnable r) { this.onConfirm = r; }
    public void setOnCancel(Runnable r) { this.onCancel = r; }
}
