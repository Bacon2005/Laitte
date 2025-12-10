package com.laitte.Managers.Orders;

import com.laitte.Managers.OrderPageController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class OrdersPaneController {
    @FXML
    private Label customerName;
    @FXML
    private Label orderNumber;
    @FXML
    private ImageView mealImage;

    @FXML
    private Button completeBtn;
    @FXML
    private Button cancelBtn;

    @FXML
    private AnchorPane orderListTemplate;

    public void setData(String customer, int orderNum, boolean isPending) {

        if (isPending) {
            customerName.setText(customer);
            orderNumber.setText(String.valueOf(orderNum)); // int to string
        }

        mealImage.setImage(
                new Image(getClass().getResourceAsStream("/Images/CheeseCake.jpg")));

    }

    @FXML
    private void completeBtn(ActionEvent event) {
        int orderId = Integer.parseInt(orderNumber.getText());

        boolean completed = OrdersDAO.completeOrder(orderId);

        if (completed) {
            System.out.println("Order completed!");

            // Option 1: Remove the pane from UI immediately
            ((Pane) orderListTemplate.getParent()).getChildren().remove(orderListTemplate);

            // Option 2: Or refresh the list if you want to show cancelled orders elsewhere
            // loadOrders();
            if (parentController != null) {
                parentController.reloadOrders(); // reload all orders
                System.out.println("RELOADED!");
            }

        } else {
            System.out.println("Failed to cancel order.");
        }
    }

    @FXML
    private void cancelBtn(ActionEvent event) {
        int orderId = Integer.parseInt(orderNumber.getText());

        boolean cancelled = OrdersDAO.cancelOrder(orderId);

        if (cancelled) {
            System.out.println("Order cancelled!");

            // Option 1: Remove the pane from UI immediately
            ((Pane) orderListTemplate.getParent()).getChildren().remove(orderListTemplate);

            // Option 2: Or refresh the list if you want to show cancelled orders elsewhere
            // loadOrders();
            if (parentController != null) {
                parentController.reloadOrders(); // reload all orders
                System.out.println("RELOADED!");
            }

        } else {
            System.out.println("Failed to cancel order.");
        }
    }

    private OrderPageController parentController;

    public void setParentController(OrderPageController controller) {
        this.parentController = controller;
    }

}
