package com.laitte.Managers;

import java.io.IOException;
import java.util.List;

import com.laitte.Managers.Employees.Employee;
import com.laitte.Managers.Employees.EmployeeDAO;
import com.laitte.Managers.Employees.EmployeePaneController;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    @FXML
    private Button refreshBtn;

    // --------------------------------------------------------------------------//
    public void initialize() {
        slider.setVisible(true);
        System.out.println(Session.getUsername());
        nameLabel.setText("Hello, " + Session.getUsername()); // Set username from session
        Circle clip = new Circle(50, 50, 50);
        profilePic.setClip(clip);

        loadEmployees();

        double hiddenX = -200; // sidebar width
        slider.setTranslateX(hiddenX);

        // Slide IN animation
        TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.3), slider);
        slideIn.setToX(0);

        // Slide OUT animation
        TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.3), slider);
        slideOut.setToX(hiddenX);

        // 1. Hover near left edge → slide IN
        rootPane.setOnMouseMoved(event -> {
            if (event.getX() <= 50) { // 10px from left edge
                slideIn.play();
            }
        });

        // 2. Hover OUTSIDE sidebar → slide OUT
        slider.setOnMouseExited(event -> slideOut.play());
    }

    // ------------------------------------------------------ Navigation
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
        SceneController.switchScene(event, "/FXML/Homepage/Homepage.fxml", null); // Switch to Home Scene
    }

    @FXML
    private void inventoryBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/Inventory.fxml", null); // Switch to Inventory Scene
    }

    @FXML
    private void ordersBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/OrdersPage/OrderPageManagerView.fxml", null); // Switch to Orders
    }

    // -----------------------------------------------------------------------------------------------------------------------------------//
    @FXML
    private void onAddEmployee(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/EmployeePage/AddEmployee.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Add Employee");
        stage.show();
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

    @FXML
    private void refreshPage(ActionEvent event) {
        employeeVBox.getChildren().clear();
        loadEmployees();
    }
}
