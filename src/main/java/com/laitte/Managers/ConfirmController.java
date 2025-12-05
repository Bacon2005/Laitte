package com.laitte.Managers;

import com.laitte.Managers.Employees.EmployeeDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ConfirmController {
    @FXML
    private Button cancel;

    @FXML
    private Button deleteBtn;

    private int employeeId;
    private AnchorPane paneToRemove;

    public void setupForDelete(int id, AnchorPane pane) {
        this.employeeId = id;
        this.paneToRemove = pane;
    }

    @FXML
    void cancelBtn(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void deleteBtn(ActionEvent event) {
        boolean deleted = EmployeeDAO.deleteEmployee(employeeId);

        if (deleted) {
            System.out.println("Employee deleted!");

            // Remove pane from its parent VBox
            VBox parent = (VBox) paneToRemove.getParent();
            parent.getChildren().remove(paneToRemove);

            // close confirm window
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } else {
            System.out.println("Failed to delete employee!");
        }
    }
}
