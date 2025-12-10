package com.laitte.Managers;

import java.io.IOException;
<<<<<<< HEAD
=======
import java.util.function.Consumer;

>>>>>>> origin/master
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {
    // Controller can be null if no data needs to be passed
    public static void switchScene(ActionEvent event, String fxmlPath, Consumer<Object> controllerSetup)
            throws IOException {

        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(fxmlPath));
        Parent root = loader.load();

        // If caller wants to pass data to controller
        if (controllerSetup != null) {
            Object controller = loader.getController();
            controllerSetup.accept(controller);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
<<<<<<< HEAD

    public static void switchToHomepage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/FXML/Homepage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void switchToOrders(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/FXML/Orders.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void switchToInventory(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/FXML/Inventory.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void switchToAnalytics(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/FXML/Analytics.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
=======
>>>>>>> origin/master
}
