package com.laitte.Managers;

import com.laitte.Model.MenuItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.InputStream;

public class ConfirmationPopUpController {

    @FXML private Button closeButton;
    @FXML private Button cancelBtn;
    @FXML private Button confirmBtn;
    @FXML private Label messageLabel;
    @FXML private ImageView popupImage;

    private Runnable onConfirm;
    private Runnable onCancel;

    @FXML
    private void initialize() {
        closeButton.setOnAction(e -> doCancel());
        cancelBtn.setOnAction(e -> doCancel());
        confirmBtn.setOnAction(e -> doConfirm());
    }

    public void setMenuItem(MenuItem item) {
        if (item == null) return;
        messageLabel.setText("Add \"" + item.getName() + "\" to order?");
        if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
            try (InputStream is = getClass().getResourceAsStream(item.getImagePath())) {
                if (is != null) popupImage.setImage(new Image(is));
            } catch (Exception ex) {
                System.out.println("Popup image load error: " + ex.getMessage());
            }
        }
    }

    public void setOnConfirm(Runnable r) {
        this.onConfirm = r;
    }

    public void setOnCancel(Runnable r) {
        this.onCancel = r;
    }

    private void doConfirm() {
        if (onConfirm != null) onConfirm.run();
        closeWindow();
    }

    private void doCancel() {
        if (onCancel != null) onCancel.run();
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();
    }
}
