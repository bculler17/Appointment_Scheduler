/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import Model.Country;
import Model.Customer;
import Model.User;
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
import utils.CustomerDaoImpl;

/**
 * FXML Controller class
 *
 * @author Beth Culler
 */
public class EditCustomerController implements Initializable {
    
    User user;
    Customer customer;

    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private TextField address2;
    @FXML
    private TextField city;
    @FXML
    private TextField postalCode;
    @FXML
    private ComboBox<String> countrySelection;
    @FXML
    private TextField phone;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    
    private ObservableList<String> CountryNames= FXCollections.observableArrayList();

    //Constructor
    
    public EditCustomerController(User user, Customer customer){
        this.user = user;
        this.customer = customer;
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
        setFieldValues();
    }
    
    private void setFieldValues() {
        try {
            this.name.setText(customer.getCustomerName());
            this.address.setText(customer.getAddress());
            this.address2.setText(customer.getAddress2());
            this.city.setText(customer.getCity());
            this.postalCode.setText(customer.getPostalCode());
            String countryToEdit = customer.getCountry();
            this.countrySelection.setValue(countryToEdit);
            this.phone.setText(customer.getPhone());
        }  catch(Exception ex){
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    private void updateCustomer() throws Exception{
        int customerID = customer.getCustomerID();
        String customerName = name.getText().trim(); 
        String country = countrySelection.getValue();
        Country countryG = CountryDaoImpl.getCountry(country);
        int countryID = countryG.getCountryID();
        String lastUpdateBy = user.getUserName();
        user.setLastUpdateBy(lastUpdateBy);
        String createdBy = user.getCreatedBy();
        user.setCreatedBy(createdBy);
        int cityID = customer.getCityID();
        String cityG = city.getText().trim(); 
        int addressID = customer.getAddressID();
        String customerAddress = address.getText().trim();
        String customerAddress2 = address2.getText().trim(); 
        String postCode = postalCode.getText().trim();
        String phoneG = phone.getText().trim();  
        
        CustomerDaoImpl.updateCustomer(countryID, createdBy, lastUpdateBy, cityID, cityG, addressID, customerAddress, customerAddress2, postCode, phoneG, customerID, customerName);
    }

    @FXML
    private void saveHandler(MouseEvent event) throws Exception {
        updateCustomer();
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
