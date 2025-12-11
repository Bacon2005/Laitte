package com.laitte.Managers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.scene.control.CheckBox;

public class LoginController implements Initializable {
    // ---------------- Hardcoded Username and Password for admin ----------------//
    public String username;
    public String password;
    // ---------------------------------------------------------------------------//

    // --------------------------- Variables for FXML ----------------------------//

    @FXML
    private CheckBox checkBox;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordVisible;
    @FXML
    private ImageView coffeeImage;
    @FXML
    private ImageView leavesImage;
    @FXML
    private AnchorPane invalid;
    // --------------------------------------------------------------------------//

    // --------------------------- Animation ----------------------------//
    public void slideInFromLeft(ImageView img, Double delaySeconds) {
        double imageWidth = img.getBoundsInParent().getWidth();
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), img);
        img.setTranslateX(-imageWidth - 20);
        img.setVisible(true);
        t.setFromX(-313); // start position (off-screen to the left)
        t.setByX(313); // slide right by 200 pixels
        t.setInterpolator(javafx.animation.Interpolator.SPLINE(0.25, 0.46, 0.45, 0.94));

        t.setDelay(Duration.seconds(delaySeconds));
        t.play();
    }

    // --------------------------------------------------------------------------//
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("Injected? " + checkBox); // CheckBox is injected properly
        passwordVisible.textProperty().bindBidirectional(passwordField.textProperty()); // TextField and PasswordField
                                                                                        // have the same Text

        Platform.runLater(() -> {
            slideInFromLeft(coffeeImage, 0.0);
            slideInFromLeft(leavesImage, 0.5);
        });
    }

    public boolean validateLogin(String username, String password) {

        String query = """
                SELECT e.firstname, l.loginid, l.username, l.password, e2.ismanager, e.imagepath, e.lastname, e2.rolename
                FROM employee e
                JOIN login l ON e.loginid = l.loginid
                join employeerole e2 on e.roleid = e2.roleid
                WHERE l.username = ? AND l.password = ?
                                            """;

        try (Connection conn = com.laitte.LaitteMain.Database.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("firstname");
                boolean manager = rs.getBoolean("ismanager");
                String imagePath = rs.getString("imagepath");
                String lastname = rs.getString("lastname");
                String user = rs.getString("username");
                String role = rs.getString("rolename");
                Session.setUsername(user);
                Session.setFirstname(firstName); // gets first name
                Session.setLastname(lastname);
                Session.setManager(manager); // checks if manager or not
                Session.setRole(role);
                Session.setProfileImagePath("/Images/ProfilePhotos/" + imagePath); // gets image path
                return true; // Valid credentials
            } else {
                return false; // Invalid credentials
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void loginBtn(ActionEvent event) throws IOException {
        username = usernameField.getText();
        password = passwordField.getText();

        if (validateLogin(username, password)) {
            SceneController.switchScene(event, "/FXML/Homepage/Homepage.fxml", null);

        } else {
            System.out.println("Invalid credentials.");
            invalid.setVisible(true);
        }
    }

    @FXML
    public void toggleShowPassword(ActionEvent event) {
        if (checkBox.isSelected()) {
            System.out.println("Show password");
            // Show password
            passwordField.setVisible(false);
            passwordVisible.setVisible(true);

        } else {
            System.out.println("Hide password");
            // Hide password
            passwordField.setVisible(true);
            passwordVisible.setVisible(false);

        }
    }

}
