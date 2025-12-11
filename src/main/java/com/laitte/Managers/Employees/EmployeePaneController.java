package com.laitte.Managers.Employees;

import com.laitte.Managers.ConfirmController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class EmployeePaneController {

    @FXML
    private AnchorPane employeePaneTemplate;
    @FXML
    private ImageView employeePic;
    @FXML
    private Label namePaneLabel;
    @FXML
    private Label roleName;
    @FXML
    private Label roleLabel;
    @FXML
    private Label employeeID;
    @FXML
    private Label employeeLabel;
    @FXML
    private CheckBox managerCheckBox;
    @FXML
    private Label employeeName;
    @FXML
    private Button deleteBtn;
    @FXML
    private ImageView deleteImage;

    // Call this to populate the pane with database info
    public void initialize() {
        deleteImage.setImage(new Image(getClass().getResourceAsStream("/Images/542724.png")));
    }

    public void setData(String name, int id, String role, boolean isManager, String imagePath) { // fix image
        employeeName.setText(name);
        employeeID.setText(String.valueOf(id)); // int to string
        roleName.setText(role);
        managerCheckBox.setSelected(isManager);

        if (imagePath != null && !imagePath.isEmpty()) {
            Circle clip = new Circle();
            clip.radiusProperty().bind(employeePic.fitWidthProperty().divide(2));
            clip.centerXProperty().bind(employeePic.fitWidthProperty().divide(2));
            clip.centerYProperty().bind(employeePic.fitHeightProperty().divide(2));
            employeePic.setClip(clip);
            employeePic.setImage(new Image("/Images/ProfilePhotos/" + imagePath));
        } else {
            // Use default placeholder
            employeePic.setImage(
                    new Image(getClass().getResourceAsStream("/Images/7982ca49-c790-4282-8b82-ede6a2d33aac.jpg")));
        }
    }

    @FXML
    private void onManagerCheckBoxToggled() {
        boolean selected = managerCheckBox.isSelected();
        try {
            if (selected) {
                System.out.println("Checkbox selected");
                int id = Integer.parseInt(employeeID.getText());
                EmployeeDAO.setManager(id, selected); // updates DB
                roleName.setText("Manager");
            } else {
                System.out.println("Checkbox deselected");
                int id = Integer.parseInt(employeeID.getText());
                EmployeeDAO.setManager(id, selected); // updates DB
                roleName.setText("Worker");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void deleteBtn(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ConfirmPage.fxml"));
            Parent root = loader.load();

            // Get controller of Confirm page
            ConfirmController confirmController = loader.getController();

            // Pass employee ID and pane reference
            confirmController.setupForDelete(
                    Integer.parseInt(employeeID.getText()),
                    employeePaneTemplate);

            // Open confirm window
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.setTitle("Confirm Delete");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}