/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.Country;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static utils.TimeFiles.stringToCalendar;

/**
 *
 * @author Beth Culler
 */
public class CountryDaoImpl {
    public static Country getCountry(String country) throws SQLException, Exception{
        DBConnection.openDBConnection();
        String sqlStatement="select * FROM country WHERE country  = '" + country + "';";
         
        Query.makeQuery(sqlStatement);
           ResultSet result=Query.getResult();
           while(result.next()){
                int countryID=result.getInt("countryID");
                String countryG=result.getString("country");
                String createDate=result.getString("createDate");
                String createdBy=result.getString("createdBy");
                String lastUpdate=result.getString("lastUpdate");
                String lastUpdateby=result.getString("lastUpdateBy");
                Calendar createDateCalendar=stringToCalendar(createDate);
                Calendar lastUpdateCalendar=stringToCalendar(lastUpdate);
                Country countryResult = new Country(countryID, country,createDateCalendar, createdBy, 
                        lastUpdateCalendar, lastUpdateby); 
               
                return countryResult;
           }
        DBConnection.closeDBConnection();
        return null;
    }
    
     public static ObservableList<Country> getAllCountries() throws SQLException, Exception{
        ObservableList<Country> allCountries=FXCollections.observableArrayList();  
        
        try{
            String sqlStatement="SELECT * FROM country";
            PreparedStatement ps = DBConnection.openDBConnection().prepareStatement(sqlStatement);
            ResultSet result=ps.executeQuery();
             while(result.next()){
                int countryID=result.getInt("countryId");
                String countryName=result.getString("country");
                String createDate=result.getString("createDate");
                String createdBy=result.getString("createdBy");
                String lastUpdate=result.getString("lastUpdate");
                String lastUpdateby=result.getString("lastUpdateBy");
                Calendar createDateCalendar=stringToCalendar(createDate);
                Calendar lastUpdateCalendar=stringToCalendar(lastUpdate);
                Country countryResult= new Country(countryID, countryName, createDateCalendar, createdBy, 
                        lastUpdateCalendar, lastUpdateby);
                allCountries.add(countryResult);
                
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return allCountries;
    } 
     
    public static ObservableList<String> getCountryNames() throws SQLException, Exception{
        ObservableList<String> countryNames=FXCollections.observableArrayList(); 
        
        try{
            String sqlStatement="SELECT country FROM country";
            PreparedStatement ps = DBConnection.openDBConnection().prepareStatement(sqlStatement);
            ResultSet result=ps.executeQuery();
             while(result.next()){
                String countryName=result.getString("country");
                countryNames.add(countryName); 
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return countryNames;
    } 
    
    
}
