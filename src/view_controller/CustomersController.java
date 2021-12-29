/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import Model.Customer;
import Model.User;
import utils.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Beth Culler
 */
public class CustomersController implements Initializable {
    
    User user;

    @FXML
    private Button addCustomerButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button customerButton;
    @FXML
    private Button calendarButton;
    @FXML
    private Label reminderAlert;
    @FXML
    private TableView<Customer> CustomerTable;
    @FXML
    private TableColumn<Customer, Boolean> activeStatus;
    @FXML
    private TableColumn<?, ?> ID;
    @FXML
    private TableColumn<?, ?> address;
    @FXML
    private TableColumn<?, ?> phoneNumber;
    @FXML
    private TableColumn<?, ?> customerName;
    @FXML
    private TableColumn<?, ?> city;
    @FXML
    private TableColumn<?, ?> postalCode;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button addApmtButton;
    @FXML
    private Button otherUsersButton;
    @FXML
    private Button editButton;
    @FXML
    private Label numOfCustomers;
    
    private ObservableList<Customer> Customers= FXCollections.observableArrayList();
    private ObservableList<Boolean> activeStatusList= FXCollections.observableArrayList();
    final String style = "-fx-background-color: yellow; -fx-border-color: gray;";
    
    // Constructor
    
    public CustomersController(User user){
        this.user = user;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        activeStatusList.add(true);
        activeStatusList.add(false);
        
        try {
            Customers.addAll(CustomerDaoImpl.getAllCustomers());
  
        } catch (Exception ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Display the total number of Customers
        int numberOfCustomers = Customers.size();
        numOfCustomers.setText(Integer.toString(numberOfCustomers));
        
        // Make the "Active" Column editable to choose between true (active) or false (inactive)
        Callback<TableColumn<Customer, Boolean>, TableCell<Customer, Boolean>> cbtc = 
            ComboBoxTableCell.forTableColumn(activeStatusList);
        activeStatus.setCellFactory(cbtc);
        activeStatus.setOnEditCommit(edit -> {
            if(edit.getNewValue().equals(true)){
                edit.getRowValue().setActive(true);
            }
            if(edit.getNewValue().equals(false)){
                edit.getRowValue().setActive(false);
            }
            
            CustomerDaoImpl.updateActiveStatus(edit.getRowValue());
            CustomerTable.refresh();
            });
        
        // Populate the Customer table to display the list of Customers
        CustomerTable.setItems(Customers);
        
        
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
                System.out.println("You are here");
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
                System.out.println("Click to return to your Calendar view");
                calendarButton.setStyle(style);
            }
        );
        
        calendarButton.setOnMouseExited(
            event -> {
                calendarButton.setStyle(null);
            }
        );
     
    }    
    
    // Navigate to the "Add Customer" screen to add a new customer to the database
    @FXML
    private void addCustomerHandler(MouseEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddCustomer.fxml"));
            view_controller.AddCustomerController controller = new view_controller.AddCustomerController(user);
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
    
    private boolean confirmationWindow(int customerId){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer Records");
            alert.setHeaderText("Are you sure you want to permanently delete all records of Customer: " + customerId + " from the database?");
            alert.setContentText("Click ok to permanently delete Customer: " + customerId + ". If you wish to only deactivate Customer: " + customerId + " while still preserving their records in the database, click cancel and set 'Active' to FALSE.");
            Optional<ButtonType> result = alert.showAndWait();
            return result.get() == ButtonType.OK;
    }
    
    public static void infoWindow(int code, int customerID){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Action Status");
        alert.setHeaderText("Customer Records");
        switch(code) {
            case 1: {
                alert.setContentText("All records of Customer: " + customerID + " have been permanently deleted from the database.");
                break;
            }
            case 2: {
                alert.setContentText("No changes were made to Customer: " + customerID + ".");
                break;
            }
            default: {
                alert.setContentText("Unknown error!");
                break;
            }
        }
        alert.showAndWait();
    }
    
    private void errorWindow() {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Selection!");
            alert.setContentText("You must select a customer!");
            alert.showAndWait();
        }
    
    //Delete a customer from the database
    @FXML
    private void deleteHandler(MouseEvent event) {
            Customer selected = CustomerTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                errorWindow();
                return;
            } 
          
                boolean remove = confirmationWindow(selected.getCustomerID());
                if (remove) {
                    CustomerDaoImpl.deleteCustomer(selected);
                    infoWindow(1, selected.getCustomerID());
                    Customers.remove(selected);
                    CustomerTable.refresh();
                    
                    //Display the total number of Customers
                    int numberOfCustomers = Customers.size();
                    numOfCustomers.setText(Integer.toString(numberOfCustomers));
                }
                else {
                    infoWindow(2, selected.getCustomerID());
                }
    }

    @FXML
    private void customerHandler(MouseEvent event) {
     
    }

    // Navigate back to the homepage (calendar view of appointments)
    @FXML
    private void calendarHandler(MouseEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Homepage.fxml"));
            view_controller.HomepageController controller = new view_controller.HomepageController(user);
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

    // Update customer information
    @FXML
    private void updateCustomer(MouseEvent event) {
        try{
            Customer selected = CustomerTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                errorWindow();
                return;
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditCustomer.fxml"));
                view_controller.EditCustomerController controller = new view_controller.EditCustomerController(user, selected);
                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
                    
        } catch (IOException e) {
            System.out.println(e);
        }
    }
   
}

    


