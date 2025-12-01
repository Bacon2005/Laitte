package com.laitte.Managers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class SceneController {

    public static void switchToLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/FXML/LoginScene.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void switchToHomepage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/FXML/Homepage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void switchToInventory(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/FXML/Inventory.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
