/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import Model.Country;
import Model.Customer;
import Model.User;
import utils.CustomerDaoImpl;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.CountryDaoImpl;
import utils.DBConnection;

/**
 * FXML Controller class
 *
 * @author Beth Culler
 */
public class AddCustomerController implements Initializable {
    
    User user;

    @FXML
    private Button saveButton;
    @FXML
    private TextField phoneNumber;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private TextField city;
    @FXML
    private TextField postalCode;
    private TextField country;
    @FXML
    private TextField address2;
    @FXML
    private ComboBox<String> countrySelection;
    
    private ObservableList<Customer> Customers= FXCollections.observableArrayList();
    private ObservableList<String> CountryNames= FXCollections.observableArrayList();
    private ObservableList<String> CountryID= FXCollections.observableArrayList();
    
    //Constructor
    
    public AddCustomerController(User user) {
        this.user = user;
    }

       
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            CountryNames.addAll(CountryDaoImpl.getCountryNames());
  
        } catch (Exception ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        countrySelection.setItems(CountryNames);
    }    
 
    private void addCustomer() throws Exception {
        String country = countrySelection.getValue();
        Country countryG = CountryDaoImpl.getCountry(country);
        int countryID = countryG.getCountryID();
        String createdBy = user.getUserName();
        user.setCreatedBy(createdBy);
        String lastUpdateby = user.getUserName();
        user.setLastUpdateBy(lastUpdateby);
        String cityG = city.getText().trim();  
        String customerAddress = address.getText().trim();
        String customerAddress2 = address2.getText().trim(); 
        String postCode = postalCode.getText().trim();
        String phone = phoneNumber.getText().trim(); 
        String customerName = name.getText().trim();  
                
        CustomerDaoImpl.addNewCustomer(countryID, createdBy, lastUpdateby, cityG, customerAddress, customerAddress2, postCode, phone, customerName);
    }
    
    @FXML
    private void addCustomerHandler(MouseEvent event) throws Exception {
        addCustomer();
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
    private void cancelHandler(MouseEvent event) {
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
    
}
