/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.Customer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import static utils.TimeFiles.stringToCalendar;

/**
 *
 * @author Beth Culler
 */
public class CustomerDaoImpl {
    static boolean act;
    static int active;
    public static Customer getCustomer(String customerName) throws SQLException, Exception{
        
        DBConnection.openDBConnection();
        String sqlStatement="select * FROM (((customer INNER JOIN address ON customer.addressID=address.addressID) "
                + "INNER JOIN city ON address.cityID=city.cityID) INNER JOIN country ON city.countryid=country.countryid) "
                + "WHERE customerName  = '" + customerName + "';";
         
        Query.makeQuery(sqlStatement);
           Customer customerResult;
           ResultSet result=Query.getResult();
           while(result.next()){
                int customerID=result.getInt("customerID");
                String customerNameG=result.getString("customerName");
                int addressID=result.getInt("addressID");
                int active=result.getInt("active");
                if(active==1) act=true;
                String address=result.getString("address");
                String address2=result.getString("address2");
                int cityID=result.getInt("cityID");
                String postalCode=result.getString("postalCode");
                String phone=result.getString("phone");
                String city=result.getString("city");
                int countryID=result.getInt("countryID");
                String country=result.getString("country");
                String createDate=result.getString("createDate");
                String createdBy=result.getString("createdBy");
                String lastUpdate=result.getString("lastUpdate");
                String lastUpdateby=result.getString("lastUpdateBy");
                Calendar createDateCalendar=stringToCalendar(createDate);
                Calendar lastUpdateCalendar=stringToCalendar(lastUpdate);
                
                customerResult = new Customer(customerID, customerName, addressID, act, address, address2,
                        cityID, postalCode, phone, city, countryID, country, createDateCalendar, createdBy, 
                        lastUpdateCalendar, lastUpdateby); 
             
        
                return customerResult;
           }
             DBConnection.closeDBConnection();
        return null;
    }
  
    public static ObservableList<Customer> getAllCustomers() throws SQLException, Exception{
        ObservableList<Customer> allCustomers=FXCollections.observableArrayList();    
        
        try{
            String sqlStatement="SELECT customerId, customerName, active, customer.createDate, customer.createdBy, customer.lastUpdate, customer.lastUpdateBy, address.addressId, address, address2, postalCode, phone, address.createDate, address.createdBy, address.lastUpdate, address.lastUpdateBy, city.cityId, city, city.createDate, city.createdBy, city.lastUpdate, city.lastUpdateBy, country.countryId, country, country.createDate, country.createdBy, country.lastUpdate, country.lastUpdateBy  "
                    + "FROM customer, address, city, country WHERE customer.addressID=address.addressID AND address.cityID=city.cityID AND city.countryid=country.countryid;";          
            PreparedStatement ps = DBConnection.openDBConnection().prepareStatement(sqlStatement);
            ResultSet result=ps.executeQuery();
             while(result.next()){
                int customerID=result.getInt("customerId");
                String customerName=result.getString("customerName");
                int addressID=result.getInt("addressId");
                int active=result.getInt("active");
                if(active==1) act=true;
                if(active==0) act=false;
                String address=result.getString("address");
                String address2=result.getString("address2");
                int cityID=result.getInt("cityId");
                String postalCode=result.getString("postalCode");
                String phone=result.getString("phone");
                String city=result.getString("city");
                int countryID=result.getInt("countryId");
                String country=result.getString("country");
                String createDate=result.getString("createDate");
                String createdBy=result.getString("createdBy");
                String lastUpdate=result.getString("lastUpdate");
                String lastUpdateby=result.getString("lastUpdateBy");
                Calendar createDateCalendar=stringToCalendar(createDate);
                Calendar lastUpdateCalendar=stringToCalendar(lastUpdate);
                Customer customerResult= new Customer(customerID, customerName, addressID, act, address, address2,
                        cityID, postalCode, phone, city, countryID, country, createDateCalendar, createdBy, 
                        lastUpdateCalendar, lastUpdateby);
                allCustomers.add(customerResult);
                
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return allCustomers;
    } 
    
    //To not insert redundant City info during updates/inserts
    public static int getCityID(String city, int countryId) throws SQLException {
        try{
            String sqlt = "SELECT cityID FROM city WHERE city = ? AND countryId = ?";
            
            PreparedStatement pst = DBConnection.openDBConnection().prepareStatement(sqlt);
            
            pst.setString(1, city);
            pst.setInt(2, countryId);
            //execute query and get results
            ResultSet result=pst.executeQuery();
             while(result.next()){
                int cityID=result.getInt("cityId");
                return cityID;
             }
        } catch(SQLException ex) {     
             ex.printStackTrace();
        }
        
        return 0;
    }
    
    //To not insert redundant Address info during updates/inserts 
    public static int getAddressID(String address, String address2, int cityId, String postalCode, String phone) throws SQLException {
        try{
            String sqlt = "SELECT addressID FROM address WHERE address = ? AND address2 = ? AND cityId = ? AND postalCode = ? AND phone = ?";
            
            PreparedStatement pst = DBConnection.openDBConnection().prepareStatement(sqlt);
            
            pst.setString(1, address);
            pst.setString(2, address2);
            pst.setInt(3, cityId);
            pst.setString(4, postalCode);
            pst.setString(5, phone);
            //execute query and get results
            ResultSet result=pst.executeQuery();
             while(result.next()){
                int addressID=result.getInt("addressId");
                return addressID;
             }
        } catch(SQLException ex) {     
             ex.printStackTrace();
        }
        
        return 0;
    }
    
    public static void addNewCustomer(int countryID, String createdBy, String lastUpdateBy, String city, String address, String address2, String postalCode, String phone, String name) throws SQLException, Exception{
        System.out.println("Inserting records into the table...");
        try{
            //To prevent adding redundant City info
            int existingCityID = getCityID(city, countryID);
            int cityID;
            if(existingCityID == 0){ 
                //the city is a new city, need to add the city to the database
                String sqlt = "INSERT INTO city VALUES(NULL,?,?, NOW(), ?, CURRENT_TIMESTAMP, ?)";
    
                PreparedStatement pst = DBConnection.openDBConnection().prepareStatement(sqlt, Statement.RETURN_GENERATED_KEYS);
        
                pst.setString(1, city);
                pst.setInt(2, countryID);
                pst.setString(3, createdBy);
                pst.setString(4, lastUpdateBy);
                //execute query and get results
                pst.execute();
                //extract new ID
                ResultSet rsi = pst.getGeneratedKeys();
                rsi.next();
                cityID = rsi.getInt(1);
                
                //insert new customer info into Address table
                String sql = "INSERT INTO address VALUES(NULL, ?, ?, ?, ?, ?, NOW(), ?, CURRENT_TIMESTAMP, ?)";
        
                PreparedStatement ps = DBConnection.openDBConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
                ps.setString(1, address);
                ps.setString(2, address2);
                ps.setInt(3, cityID);
                ps.setString(4, postalCode);
                ps.setString(5, phone);
                ps.setString(6, createdBy);
                ps.setString(7, lastUpdateBy);
                //execute query and get results
                ps.execute();
                //extract new ID
                ResultSet rst = ps.getGeneratedKeys();
                rst.next();
                int addressID = rst.getInt(1);
        
                //insert new customer info into Customer table
                String sqlc = "INSERT INTO customer VALUES(NULL, ?, ?, ?, NOW(), ?, CURRENT_TIMESTAMP, ?)";
        
                PreparedStatement psta = DBConnection.openDBConnection().prepareStatement(sqlc);
        
                psta.setString(1, name);
                psta.setInt(2, addressID);
                psta.setBoolean(3, true);
                psta.setString(4, createdBy);
                psta.setString(5, lastUpdateBy);
                //execute query and get results
                psta.execute();
            }
            else {
                //The city is not a new city; Info is already in database
                cityID = existingCityID;
                
                //To prevent adding redudant Address info
                int existingAddressID = getAddressID(address, address2, cityID, postalCode, phone);
                int addressID;
                if(existingAddressID == 0){
                    //The data to be entered is new information, address is not already in the database
                    String sql = "INSERT INTO address VALUES(NULL, ?, ?, ?, ?, ?, NOW(), ?, CURRENT_TIMESTAMP, ?)";
        
                    PreparedStatement ps = DBConnection.openDBConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
                    ps.setString(1, address);
                    ps.setString(2, address2);
                    ps.setInt(3, cityID);
                    ps.setString(4, postalCode);
                    ps.setString(5, phone);
                    ps.setString(6, createdBy);
                    ps.setString(7, lastUpdateBy);
                    //execute query and get results
                    ps.execute();
                    //extract new ID
                    ResultSet rst = ps.getGeneratedKeys();
                    rst.next();
                    addressID = rst.getInt(1);
        
                    //insert new customer info into Customer table
                    String sqlc = "INSERT INTO customer VALUES(NULL, ?, ?, ?, NOW(), ?, CURRENT_TIMESTAMP, ?)";
        
                    PreparedStatement psta = DBConnection.openDBConnection().prepareStatement(sqlc);
        
                    psta.setString(1, name);
                    psta.setInt(2, addressID);
                    psta.setBoolean(3, true);
                    psta.setString(4, createdBy);
                    psta.setString(5, lastUpdateBy);
                    //execute query and get results
                    psta.execute();
                }
                else{
                    //Address to be entered is already in database
                    addressID = existingAddressID;
                    //insert new customer info into Customer table
                    String sqlc = "INSERT INTO customer VALUES(NULL, ?, ?, ?, NOW(), ?, CURRENT_TIMESTAMP, ?)";
        
                    PreparedStatement psta = DBConnection.openDBConnection().prepareStatement(sqlc);
        
                    psta.setString(1, name);
                    psta.setInt(2, addressID);
                    psta.setBoolean(3, true);
                    psta.setString(4, createdBy);
                    psta.setString(5, lastUpdateBy);
                    //execute query and get results
                    psta.execute();
                }
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
              
        System.out.println("New customer successfully inserted"); 
        addConfirmation();
    }
    
     public static boolean addConfirmation(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText("Successful New Entry!");
        alert.setContentText("The new customer has been succesfully added to the database. Congratulations!");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
     
    //To prevent updating an AddressID shared by more than one customer
    public static int getCustomersWithSameAddressID(int addressID) throws SQLException {
        ObservableList<Integer> customersWithSameAddressID=FXCollections.observableArrayList(); 
        try{
            String sqlt = "SELECT customerId FROM customer WHERE addressId = ?";
            
            PreparedStatement pst = DBConnection.openDBConnection().prepareStatement(sqlt);
            
            pst.setInt(1, addressID);
            //execute query and get results
            ResultSet result=pst.executeQuery();
             while(result.next()){
                int num=result.getInt("customerId");
                customersWithSameAddressID.add(num);
             }
        } catch(SQLException ex) {     
             ex.printStackTrace();
        }
        int number = customersWithSameAddressID.size();
        return number;
    }
    
    public static void updateCustomer(int countryID, String createdBy, String lastUpdateBy, int cityID, String city, int addressID, String address, String address2, String postalCode, String phone, int customerID, String name) throws SQLException, Exception{
        System.out.println("Updating the table...");
        try{
            //To prevent adding redundant City info
            int existingCityID = getCityID(city, countryID);
            if(existingCityID == 0){
                //the city is a new city, need to add the city to the database
                String sqlt = "INSERT INTO city VALUES(NULL, ?, ?, NOW(), ?, CURRENT_TIMESTAMP, ?)";
        
                PreparedStatement pst = DBConnection.openDBConnection().prepareStatement(sqlt, Statement.RETURN_GENERATED_KEYS);
        
                pst.setString(1, city);
                pst.setInt(2, countryID);
                pst.setString(3,createdBy);
                pst.setString(4, lastUpdateBy);
                //execute query and get results
                pst.execute();
                //extract new ID
                ResultSet rst = pst.getGeneratedKeys();
                rst.next();
                int newCityID = rst.getInt(1);
                
                //To prevent changing an AddressID shared by more than one customer
                int number = getCustomersWithSameAddressID(addressID);
                if(number == 1){
                    //AddressID is not shared; can safely update
                    String sql = "UPDATE address SET address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE addressId = ?";
        
                    PreparedStatement ps = DBConnection.openDBConnection().prepareStatement(sql);
        
                    ps.setString(1, address);
                    ps.setString(2, address2);
                    ps.setInt(3, newCityID);
                    ps.setString(4, postalCode);
                    ps.setString(5, phone);
                    ps.setString(6, lastUpdateBy);
                    ps.setInt(7, addressID);
                    //execute query and get results
                    ps.execute();
            
                    //update customer info in the Customer table
                    String sqlc = "UPDATE customer SET customerName = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE customerId = ?";
        
                    PreparedStatement psta = DBConnection.openDBConnection().prepareStatement(sqlc);
        
                    psta.setString(1, name);
                    psta.setString(2, lastUpdateBy);
                    psta.setInt(3, customerID);
                    //execute query and get results
                    psta.execute();
                }
                else{
                    //another customer shares this AddressID, cannot update. Enter new information into the database
                    String sql = "INSERT INTO address VALUES(NULL, ?, ?, ?, ?, ?, NOW(), ?, CURRENT_TIMESTAMP, ?)";
        
                    PreparedStatement ps = DBConnection.openDBConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
                    ps.setString(1, address);
                    ps.setString(2, address2);
                    ps.setInt(3, newCityID);
                    ps.setString(4, postalCode);
                    ps.setString(5, phone);
                    ps.setString(6, createdBy);
                    ps.setString(7, lastUpdateBy);
                    //execute query and get results
                    ps.execute();
                    //extract new ID
                    ResultSet rst1 = ps.getGeneratedKeys();
                    rst1.next();
                    addressID = rst1.getInt(1);
                    
                    //update customer info in the Customer table
                    String sqlc = "UPDATE customer SET customerName = ?, addressId = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE customerId = ?";
        
                    PreparedStatement psta = DBConnection.openDBConnection().prepareStatement(sqlc);
        
                    psta.setString(1, name);
                    psta.setInt(2, addressID);
                    psta.setString(3, lastUpdateBy);
                    psta.setInt(4, customerID);
                    //execute query and get results
                    psta.execute();
                }
            }
            else{
                //The city is not a new city; Info is already in database
                //To prevent adding redudant Address info
                int existingAddressID = getAddressID(address, address2, existingCityID, postalCode, phone);
      
                if(existingAddressID == 0){
                    //The data to be entered is new information, address is not already in the database
                    //To prevent changing an AddressID shared by more than one customer
                    int number = getCustomersWithSameAddressID(addressID);
                    if(number == 1){
                        //AddressID is not shared; can safely update
                        String sql = "UPDATE address SET address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE addressId = ?";
        
                        PreparedStatement ps = DBConnection.openDBConnection().prepareStatement(sql);
        
                        ps.setString(1, address);
                        ps.setString(2, address2);
                        ps.setInt(3, existingCityID);
                        ps.setString(4, postalCode);
                        ps.setString(5, phone);
                        ps.setString(6, lastUpdateBy);
                        ps.setInt(7, addressID);
                        //execute query and get results
                        ps.execute();
            
                        //update customer info in the Customer table
                        String sqlc = "UPDATE customer SET customerName = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE customerId = ?";
        
                        PreparedStatement psta = DBConnection.openDBConnection().prepareStatement(sqlc);
        
                        psta.setString(1, name);
                        psta.setString(2, lastUpdateBy);
                        psta.setInt(3, customerID);
                        //execute query and get results
                        psta.execute();
                        }
                    else{
                        //another customer shares this AddressID, cannot update. Enter new information into the database
                        String sql = "INSERT INTO address VALUES(NULL, ?, ?, ?, ?, ?, NOW(), ?, CURRENT_TIMESTAMP, ?)";
        
                        PreparedStatement ps = DBConnection.openDBConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
                        ps.setString(1, address);
                        ps.setString(2, address2);
                        ps.setInt(3, existingCityID);
                        ps.setString(4, postalCode);
                        ps.setString(5, phone);
                        ps.setString(6, createdBy);
                        ps.setString(7, lastUpdateBy);
                        //execute query and get results
                        ps.execute();
                        //extract new ID
                        ResultSet rst1 = ps.getGeneratedKeys();
                        rst1.next();
                        addressID = rst1.getInt(1);
                    
                        //update customer info in the Customer table
                        String sqlc = "UPDATE customer SET customerName = ?, addressId = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE customerId = ?";
        
                        PreparedStatement psta = DBConnection.openDBConnection().prepareStatement(sqlc);
        
                        psta.setString(1, name);
                        psta.setInt(2, addressID);
                        psta.setString(3, lastUpdateBy);
                        psta.setInt(4, customerID);
                        //execute query and get results
                        psta.execute();
                    }
                }
                else{ 
                    //Address to be entered is already in database
                    String sqlc = "UPDATE customer SET customerName = ?, addressId = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE customerId = ?";
        
                    PreparedStatement psta = DBConnection.openDBConnection().prepareStatement(sqlc);
        
                    psta.setString(1, name);
                    psta.setInt(2, existingAddressID);
                    psta.setString(3, lastUpdateBy);
                    psta.setInt(4, customerID);
                    //execute query and get results
                    psta.execute();
                    }
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        System.out.println("Customer successfully updated"); 
        updateConfirmation(customerID);
    }
    
    public static boolean updateConfirmation(int customerID){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText("Successful Update!");
        alert.setContentText("Customer: " + customerID + " has been succesfully updated in the database.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    
    public static void deleteCustomer(Customer customer){
        System.out.println("Updating the table...");
        try{
            //delete customer info from the Customer table
            String sqlc = "DELETE FROM customer WHERE customerId = ?";
        
            PreparedStatement psta = DBConnection.openDBConnection().prepareStatement(sqlc);
        
            psta.setInt(1, customer.getCustomerID());
            //execute query and get results
            psta.execute();
            
            //delete customer info from the Address table
            String sql = "DELETE FROM address WHERE addressId = ?";
        
            PreparedStatement ps = DBConnection.openDBConnection().prepareStatement(sql);
        
            ps.setInt(1, customer.getAddressID());
            //execute query and get results
            ps.execute();
            
            //delete customer info from the City table
            String sqlt = "DELETE FROM city WHERE cityId = ?";
        
            PreparedStatement pst = DBConnection.openDBConnection().prepareStatement(sqlt);
        
            pst.setInt(1, customer.getCityID());
            //execute query and get results
            pst.execute();
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        System.out.println("Customer successfully deleted"); 
    }
    
    public static void updateActiveStatus(Customer customerToEdit){
        System.out.println("Updating the table...");
        try{
            //update customer info in the Customer table
            String sqlc = "UPDATE customer SET active = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE customerId = ?";
        
            PreparedStatement psta = DBConnection.openDBConnection().prepareStatement(sqlc);
            act = customerToEdit.isActive();
            if (act==true) active=1;
            if (act==false) active=0;
            psta.setInt(1, active);
            psta.setString(2, customerToEdit.getLastUpdateBy());
            psta.setInt(3, customerToEdit.getCustomerID());
            //execute query and get results
            psta.execute();
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Customer successfully updated"); 
        updateConfirmation(customerToEdit.getCustomerID());
    }
}