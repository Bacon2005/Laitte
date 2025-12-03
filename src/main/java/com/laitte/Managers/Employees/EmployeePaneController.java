package com.laitte.Managers.Employees;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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

    // Call this to populate the pane with database info
    public void setData(String name, int id, String role, boolean isManager, String imagePath) {
        employeeName.setText(name);
        employeeID.setText(String.valueOf(id)); // int to string
        roleName.setText(role);
        managerCheckBox.setSelected(isManager);

        if (imagePath != null && !imagePath.isEmpty()) {
            employeePic.setImage(new Image(imagePath));
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

}