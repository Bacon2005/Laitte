package com.laitte.Managers;

import java.io.IOException;
import java.util.List;

import com.laitte.Managers.Employees.Employee;
import com.laitte.Managers.Employees.EmployeeDAO;
import com.laitte.Managers.Employees.EmployeePaneController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class AccountsController {

    @FXML
    private Button Accounts;
    @FXML
    private Button analytics;
    @FXML
    private Button home;
    @FXML
    private Button inventory;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button menu;
    @FXML
    private Label nameLabel;
    @FXML
    private Button orders;
    @FXML
    private ImageView profilePic;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button settingBtn;
    @FXML
    private AnchorPane slider;
    // --------------------------------------------------------------------------//
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
    // --------------------------------------------------------------------------//

    @FXML
    private VBox employeeVBox;
    @FXML
    private Button addEmployeeBtn;

    // --------------------------------------------------------------------------//
    public void initialize() {
        System.out.println(Session.getUsername());
        nameLabel.setText("Hello, " + Session.getUsername()); // Set username from session
        Circle clip = new Circle(50, 50, 50);
        profilePic.setClip(clip);
        loadEmployees();
    }

    @FXML
    private void logoutBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/LoginScene.fxml", null); // Switch to Login Scene
    }

    @FXML
    private void AccountsBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/StaffMembers.fxml", null); // Switch to Accounts Scene
    }

    @FXML
    private void homeBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/ManagerHomepage.fxml", null); // Switch to Home Scene
    }

    @FXML
    private void onAddEmployee(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DuplicatingPanels/EmployeePane.fxml"));
            AnchorPane pane = loader.load();

            // optionally set data using the child controller

            employeeVBox.getChildren().add(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEmployees() {
        List<Employee> employees = EmployeeDAO.getAllEmployees();

        for (Employee emp : employees) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DuplicatingPanels/EmployeePane.fxml"));
                AnchorPane pane = loader.load();

                EmployeePaneController controller = loader.getController();
                controller.setData(emp.getName(), emp.getId(), emp.getRole(), emp.isManager(), emp.getImagePath());

                employeeVBox.getChildren().add(pane);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
