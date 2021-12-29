/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.User;
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
public class UserDaoImpl {
    static boolean act;
     public static User getUser(String userName) throws SQLException, Exception{
        // type is name or phone, value is the name or the phone #
        DBConnection.openDBConnection();
        String sqlStatement="select * FROM user WHERE userName  = '" + userName+ "'";
         //  String sqlStatement="select FROM address";
        Query.makeQuery(sqlStatement);
           User userResult;
           ResultSet result=Query.getResult();
           while(result.next()){
                int userID=result.getInt("userID");
                String userNameG=result.getString("userName");
                String password=result.getString("password");
                int active=result.getInt("active");
                if(active==1) act=true;
                String createDate=result.getString("createDate");
                String createdBy=result.getString("createdBy");
                String lastUpdate=result.getString("lastUpdate");
                String lastUpdateby=result.getString("lastUpdateBy");
                Calendar createDateCalendar=stringToCalendar(createDate);
                Calendar lastUpdateCalendar=stringToCalendar(lastUpdate);
     
                userResult= new User(userID, userName, password, act, createDateCalendar, 
                        createdBy, lastUpdateCalendar, lastUpdateby);
                return userResult;
           }
             DBConnection.closeDBConnection();
        return null;
    }
    public static ObservableList<User> getAllUsers() throws SQLException, Exception{
        ObservableList<User> allUsers=FXCollections.observableArrayList();    
        DBConnection.openDBConnection();
            String sqlStatement="select * from user";          
            Query.makeQuery(sqlStatement);
            ResultSet result=Query.getResult();
             while(result.next()){
                int userID=result.getInt("userID");
                String userName=result.getString("userName");
                String password=result.getString("password");
                int active=result.getInt("active");
                if(active==1) act=true;
                String createDate=result.getString("createDate");
                String createdBy=result.getString("createdBy");
                String lastUpdate=result.getString("lastUpdate");
                String lastUpdateby=result.getString("lastUpdateBy");
                Calendar createDateCalendar=stringToCalendar(createDate);
                Calendar lastUpdateCalendar=stringToCalendar(lastUpdate);
                
                User userResult= new User(userID, userName, password, act, createDateCalendar, 
                        createdBy, lastUpdateCalendar, lastUpdateby);
                allUsers.add(userResult);
                
                
            }
             DBConnection.closeDBConnection();
        return allUsers;
    } 
}

