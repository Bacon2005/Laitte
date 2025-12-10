package com.laitte.Managers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEmployeeController {
    @FXML
    private TextField addFirstName;

    @FXML
    private TextField addLastName;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    private CheckBox isManager;

    @FXML
    private TextField profilePicture;

    @FXML
    private void cancelBtn(ActionEvent event) {
        // Close the add employee window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void confirmBtn(ActionEvent event) throws IOException {
        String firstName = addFirstName.getText();
        String lastName = addLastName.getText();
        boolean managerStatus = isManager.isSelected();
        String role = managerStatus ? "Manager" : "Worker";
        String profilePicPath = profilePicture.getText();

        String defaultUsername = firstName.substring(0, 2) + lastName;
        String defaultPassword = defaultUsername;

        String insertRole = """
                    INSERT INTO employeerole (rolename, ismanager)
                    VALUES (?, ?)
                    RETURNING roleid
                """;

        String insertLogin = """
                    INSERT INTO login (username, password)
                    VALUES (?, ?)
                    RETURNING loginid
                """;

        String insertEmployee = """
                    INSERT INTO employee (firstname, lastname, roleid, imagepath, loginid)
                    VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = com.laitte.LaitteMain.Database.connect()) {
            conn.setAutoCommit(false);

            // INSERT Login AND GET roleid
            int loginID;
            try (PreparedStatement pstmtLogin = conn.prepareStatement(insertLogin)) {
                pstmtLogin.setString(1, defaultUsername);
                pstmtLogin.setString(2, defaultPassword);

                ResultSet rs = pstmtLogin.executeQuery();
                rs.next();
                loginID = rs.getInt("loginid");
            }

            // INSERT ROLE AND GET roleid
            int roleID;
            try (PreparedStatement pstmtRole = conn.prepareStatement(insertRole)) {
                pstmtRole.setString(1, role);
                pstmtRole.setBoolean(2, managerStatus);

                ResultSet rs = pstmtRole.executeQuery();
                rs.next();
                roleID = rs.getInt("roleid");
            }

            // INSERT EMPLOYEE WITH roleid
            try (PreparedStatement pstmtEmployee = conn.prepareStatement(insertEmployee)) {
                pstmtEmployee.setString(1, firstName);
                pstmtEmployee.setString(2, lastName);
                pstmtEmployee.setInt(3, roleID);
                if (profilePicPath.isEmpty()) {
                    pstmtEmployee.setString(4, null);
                } else {
                    pstmtEmployee.setString(4, profilePicPath);
                }
                pstmtEmployee.setInt(5, loginID);

                pstmtEmployee.executeUpdate();
            }

            conn.commit();
            System.out.println("Employee + Role + Login inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Close the window after confirming
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
