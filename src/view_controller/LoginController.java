/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import Model.User;
import utils.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Beth Culler
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane loginScreen;
    @FXML
    private TextField userNameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label languageLabel;
    @FXML
    private Label loginLabel;
    @FXML
    private Label applicationLabel;
    @FXML
    private ComboBox<String> languageList;
    
    ResourceBundle rb;
    ResourceBundle xu;
    ResourceBundle xp;
    ResourceBundle ef;
    ObservableList<String> languages = FXCollections.observableArrayList("English", "Français", "Español");
    private ObservableList<User> Users= FXCollections.observableArrayList();
    
    // Constructor
    
    public LoginController() {
        
    }

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(Locale.getDefault());
        
        try{
            rb = ResourceBundle.getBundle("language_files/rb", Locale.getDefault());
            if(Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")
               || Locale.getDefault().getLanguage().equals("es")) {
            applicationLabel.setText(rb.getString("title"));
            loginLabel.setText(rb.getString("login"));
            usernameLabel.setText(rb.getString("username"));
            passwordLabel.setText(rb.getString("password"));
            languageLabel.setText(rb.getString("language"));
            languageList.setPromptText(rb.getString("lang"));
            loginButton.setText(rb.getString("button"));
            languageList.setItems(languages);
            }
        }catch (Exception e) {
            applicationLabel.setText("C195 Appointment Manager");
            loginLabel.setText("Log in");
            usernameLabel.setText("Username");
            passwordLabel.setText("Password");
            languageLabel.setText("Language");
            languageList.setPromptText("Please select");
            loginButton.setText("Log in");
            languageList.setItems(languages);
            langWarning();
        }
    }    
    
    public static boolean langWarning(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Our deepest apologies!");
        alert.setHeaderText("Unsupported Language");
        alert.setContentText("This application does not yet support: " + Locale.getDefault().getLanguage()
           + ". Please choose another language from the dropdown list.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    @FXML
    private void loginHandler(MouseEvent event) throws SQLException, Exception {
        String enteredUserName = userNameField.getText().trim();
        String enteredPassword = passwordField.getText().trim();
        boolean end = false;
        
        if(!userNameField.getText().trim().isEmpty() && !passwordField.getText().trim().isEmpty()){
            try{
            User user = UserDaoImpl.getUser(enteredUserName);
            String password = user.getPassword();
            if(!enteredPassword.equals(password)){
                wrongPasswordWarning();
                end = true;
                return;
            }
            if (end){
                return;
            }
        } catch(java.lang.NullPointerException exception){
            userNameNotFoundWarning();
            return;
        }
        }
        else{
            emptyFieldWarning();
            return;
        }
        try{
                    User user = UserDaoImpl.getUser(enteredUserName); 
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Homepage.fxml"));
                    HomepageController controller = new HomepageController(user);
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
    
    public boolean userNameNotFoundWarning(){
        xu = ResourceBundle.getBundle("language_files/xu", Locale.getDefault());
        ButtonType okay = new ButtonType(xu.getString("okay"), ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.ERROR, "", okay);
        alert.setTitle(xu.getString("error"));
        alert.setHeaderText(xu.getString("username"));
        alert.setContentText(xu.getString("solution"));
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == okay;
        
    }
    
    public boolean wrongPasswordWarning(){
        xp = ResourceBundle.getBundle("language_files/xp", Locale.getDefault());
        ButtonType sure = new ButtonType(xp.getString("okay"), ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.ERROR,"", sure);
        alert.setTitle(xp.getString("error"));
        alert.setHeaderText(xp.getString("password"));
        alert.setContentText(xp.getString("solution"));
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == sure;
    }
    
    public boolean emptyFieldWarning(){
        ef = ResourceBundle.getBundle("language_files/ef", Locale.getDefault());
        ButtonType affirminative = new ButtonType(ef.getString("okay"), ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.ERROR,"", affirminative);
        alert.setTitle(ef.getString("error"));
        alert.setHeaderText(ef.getString("problem"));
        alert.setContentText(ef.getString("solution"));
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == affirminative;
    }

    @FXML
    private void languageHandler(ActionEvent event) {
        String preferredLang = languageList.getSelectionModel().getSelectedItem();
        
        if(preferredLang.equals("Français")){
            Locale.setDefault(new Locale("fr", "FR"));
            applicationLabel.setText("C195 Gestionnaire de Rendez-vous");
            loginLabel.setText("S'identifier");
            usernameLabel.setText("Nom d'utilisateur");
            passwordLabel.setText("Mot de passe");
            languageLabel.setText("Langue");
            languageList.setPromptText("Veuillez sélectionner");
            loginButton.setText("S'identifier");
            languageList.setItems(languages);
        }
        else if(preferredLang.equals("Español")){
            Locale.setDefault(new Locale("es", "ES"));
            applicationLabel.setText("C195 Gerente de citas");
            loginLabel.setText("Iniciar sesión");
            usernameLabel.setText("Nombre de usuario");
            passwordLabel.setText("Contraseña");
            languageLabel.setText("Idioma");
            languageList.setPromptText("Por favor seleccione");
            loginButton.setText("Iniciar sesión");
            languageList.setItems(languages);
        }
        else if(preferredLang.equals("English")){
            Locale.setDefault(new Locale("en", "EN"));
            applicationLabel.setText("C195 Appointment Manager");
            loginLabel.setText("Log in");
            usernameLabel.setText("Username");
            passwordLabel.setText("Password");
            languageLabel.setText("Language");
            languageList.setPromptText("Please select");
            loginButton.setText("Log in");
            languageList.setItems(languages);
        }
    }
}
