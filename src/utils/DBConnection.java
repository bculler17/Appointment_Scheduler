/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Beth Culler
 */
public class DBConnection {
    //Connection String
    //Server name:  3.227.166.251
    //Database name:  U062FR
    //Username:  U062FR
    //Password:  53688673580
    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://3.227.166.251/U062FR";

    //  Database credentials
    private static final String DBUSER = "U062FR";
    private static final String DBPASS = "53688673580";

    static Connection conn = null;
        
    public static Connection openDBConnection(){
        
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);
            System.out.println(conn);
           
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return conn;
    }
     
    public static void closeDBConnection() throws SQLException{
        conn.close();
        System.out.println("Database connection closed!");
        
    }
}
