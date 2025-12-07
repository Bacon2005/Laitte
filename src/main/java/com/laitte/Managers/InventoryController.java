
package com.laitte.Managers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.laitte.LaitteMain.Database;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableRow;

public class InventoryController {
    @FXML
    private TableView<Item> inventoryTable;
    @FXML
    private TableColumn<Item, String> mealId;
    @FXML
    private TableColumn<Item, String> mealName;
    @FXML
    private TableColumn<Item, Integer> stockAvailable;
    @FXML
    private TableColumn<Item, String> stockDate;
    @FXML
    private TableColumn<Item, String> Category;
    @FXML
    private TableColumn<Item, String> mealType;
    @FXML
    private TableColumn<Item, Double> price;

    public void initialize() {

        if (Session.getManager()) {
            Accounts.setVisible(true);
        } else {
            Accounts.setVisible(false);
        }

        mealId.setCellValueFactory(new PropertyValueFactory<>("mealId"));
        mealName.setCellValueFactory(new PropertyValueFactory<>("mealName"));
        stockAvailable.setCellValueFactory(new PropertyValueFactory<>("stockAvailable"));
        stockDate.setCellValueFactory(new PropertyValueFactory<>("stockDate"));
        Category.setCellValueFactory(new PropertyValueFactory<>("category"));
        mealType.setCellValueFactory(new PropertyValueFactory<>("mealType"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadTableData();

        slider.setVisible(true); // ensures that Slider is visible and can be interacted with

        Circle clip = new Circle(50, 50, 50); // centerX, centerY, radius
        profilePic.setClip(clip);

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

        // for select and deselect
        inventoryTable.setRowFactory(tableView -> { // setRowFactory are basically additional istruction for the rows,
                                                    // allows for customization (hey give me a row to display)
            TableRow<Item> row = new TableRow<>(); // creating single row object (heres a row object i made)

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) { // single click only
                    int index = row.getIndex(); /*
                                                 * (Hey TableView, I clicked the row at position 3 — do something with
                                                 * that row)
                                                 */
                    if (row.isSelected()) {
                        // clicked again twice to deselect
                        inventoryTable.getSelectionModel().clearSelection(index);
                    }
                }
            });
            return row;
        }); // (this is the row you actually use )

    }

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

    @FXML
    private Button Accounts;

    // for adding items
    @FXML
    void m_inventory_add(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddInventory.fxml"));
        Parent root = loader.load(); // load the FXML

        AddInventoryController addInventoryController = loader.getController();
        addInventoryController.setMainController(this); // pass reference

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Add Item");
        stage.show();
    }

    // for deleting
    @FXML
    void m_inventory_delete(ActionEvent event) {
        Item selectedItem = inventoryTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            int mealId = selectedItem.getMealId();

            try (Connection conn = Database.connect()) {
                String sql = "DELETE FROM meal WHERE mealid = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, mealId);
                ps.executeUpdate();
                System.out.println("Row deleted from database!");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Remove from TableView
            inventoryTable.getItems().remove(selectedItem);
        } else {
            System.out.println("No row selected!");
        }
    }

    @FXML
    private void logoutBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/LoginScene.fxml", null); // Switch to Login Scene
    }

    @FXML
    private void homeBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/ManagerHomepage.fxml", null); // Switch to Home Scene
    }

    @FXML
    private void AccountsBtn(ActionEvent event) throws IOException {
        SceneController.switchScene(event, "/FXML/StaffMembers.fxml", null); // Switch to Accounts Scene
    }

    // loading the data in
    public void loadTableData() {
        ObservableList<Item> list = FXCollections.observableArrayList();

        String sql = "SELECT m.mealid, m.mealname, i.stockavailable, i.stockdate, " +
                "c.mealcategory, m2.mealtype, m.mealprice " +
                "FROM meal m " +
                "JOIN inventory i ON m.inventoryid = i.inventoryid " +
                "JOIN category c ON m.categoryid = c.categoryid " +
                "LEFT JOIN mealtype m2 ON m.mealtypeid = m2.mealtypeid " +
                "ORDER BY m.mealid";

        try (Connection con = Database.connect();
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(new Item(
                        rs.getInt("mealid"),
                        rs.getString("mealname"),
                        rs.getInt("stockavailable"),
                        rs.getString("stockdate"),
                        rs.getString("mealcategory"),
                        rs.getString("mealtype"),
                        rs.getDouble("mealprice")));
            }

            inventoryTable.setItems(list); // this will work if tableView is properly initialized

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
