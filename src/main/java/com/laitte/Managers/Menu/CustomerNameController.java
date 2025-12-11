package com.laitte.Managers.Menu;

import com.laitte.Managers.Session;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CustomerNameController {

    @FXML
    private Button confirm;

    @FXML
    private TextField nameText;

    @FXML
    void confirmBtn(ActionEvent event) {
        String name = nameText.getText();
        Session.setCustomerName(name);
        //close window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
