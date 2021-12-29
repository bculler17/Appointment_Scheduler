/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195.project.assessment_beth.culler;

import java.sql.SQLException;
import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;

/**
 *
 * @author Beth Culler
 */
public class C195ProjectAssessment_BethCuller extends Application {
    
    static Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        this.stage = stage;
        //Locale.setDefault(new Locale("fr", "FR"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/Login.fxml"));
        view_controller.LoginController controller = new view_controller.LoginController();
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    public static void main(String[] args) throws SQLException {
        DBConnection.openDBConnection();
        launch(args);
        DBConnection.closeDBConnection(); 
    }
   
}
