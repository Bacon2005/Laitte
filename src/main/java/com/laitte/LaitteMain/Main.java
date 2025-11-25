package com.laitte.LaitteMain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("/FXML/LoginScene.fxml"));

            Scene scene = new Scene(root);

            scene.getStylesheets().add(getClass().getResource("/CSS/stylesheet.css").toExternalForm()); // css styling

            primaryStage.setResizable(false);

            Image icon = new Image(getClass().getResourceAsStream("/Images/LogoCafe.png"));
            primaryStage.getIcons().add(icon);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Laitte Inventory Management System");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
