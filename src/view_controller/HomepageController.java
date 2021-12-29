/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import Model.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Beth Culler
 */
public class HomepageController implements Initializable {
    
    User user;

    @FXML
    private Button customerButton;
    @FXML
    private Label reminderAlert;
    @FXML
    private Spinner<?> weekOrMonthSelector;
    @FXML
    private Button searchButton;
    @FXML
    private Button calendarButton;
    @FXML
    private Button addApmtButton;
    @FXML
    private Button otherUsersButton;
    @FXML
    private Button dashboardButton;
    
    final String style = "-fx-background-color: yellow; -fx-border-color: gray;";
    
    //Constructor
    
    public HomepageController(User user) {
        this.user = user;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // lambda expressions to improve UX/UI
        
        addApmtButton.setOnMouseEntered(
            event -> {
                System.out.println("Click to add an appointment");
                addApmtButton.setStyle(style);
            }
        );
        
        addApmtButton.setOnMouseExited(
            event -> {
                addApmtButton.setStyle(null);
            }
        );
        
        customerButton.setOnMouseEntered(
            event -> {
                System.out.println("Click to see and/or edit the Customer List");
                customerButton.setStyle(style);
            }
        );
        
        customerButton.setOnMouseExited(
            event -> {
                customerButton.setStyle(null);
            }
        );
        
        otherUsersButton.setOnMouseEntered(
            event -> {
                System.out.println("Click to see the schedule for each consultant");
                otherUsersButton.setStyle(style);
            }
        );
        
        otherUsersButton.setOnMouseExited(
            event -> {
                otherUsersButton.setStyle(null);
            }
        );
        
        dashboardButton.setOnMouseEntered(
            event -> {
                System.out.println("Click to see additional data reports");
                dashboardButton.setStyle(style);
            }
        );
        
        dashboardButton.setOnMouseExited(
            event -> {
                dashboardButton.setStyle(null);
            }
        );
        
        calendarButton.setOnMouseEntered(
            event -> {
                System.out.println("You are here");
            }
        );
    }    


    @FXML
    private void customerHandler(MouseEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Customers.fxml"));
            view_controller.CustomersController controller = new view_controller.CustomersController(user);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
                    
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @FXML
    private void calendarViewHandler(MouseEvent event) {
    }

    
}
