/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package switcher.pkg2.pkg0;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Spongebob
 */
public class LoginfxmlController implements Initializable {

    @FXML
    private JFXTextField UserTxt;
    @FXML
    private JFXPasswordField PassTxt;
    @FXML
    private JFXButton LoginBtn;
    DataModel dm;
    
    
    @FXML
    public void onEnter(ActionEvent ae){
        onLogin(ae);
    }
    
    public void onLogin(ActionEvent event){
        try {
            String username = null, password = null;
            username = UserTxt.getText();
            password = PassTxt.getText();
            
            if(username.isEmpty() || password.isEmpty()){
                Alert al = new Alert(Alert.AlertType.ERROR);
                al.setTitle("Empty fields");
                al.setContentText("Enter your username and password to proceed!");
                al.setHeaderText(null);
                al.show();
                return;
            }   
            
            ResultSet res = dm.getLoginCredentials(username);
            
            if (!res.next() ) {
                System.out.println("Invalid Username");
                Alert al = new Alert(Alert.AlertType.ERROR);
                al.setTitle(null);
                al.setContentText("Invalid username or password!");
                al.setHeaderText(null);
                al.show();
                PassTxt.setText("");
                return;
            } 
            
            if(username.equals(res.getString("Username")) && password.equals(res.getString("Password"))){
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    String css = Switcher20.class.getResource("Ham.css").toExternalForm();
                    Scene scene = new Scene(root1);
                    scene.getStylesheets().add(css);
                    
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    //stage.setScene(scene);
                    stage.setScene(scene);
                    stage.show();
                    
                    stage = (Stage) LoginBtn.getScene().getWindow();
                    stage.close();
                } catch (Exception ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                Alert al = new Alert(Alert.AlertType.ERROR);
                al.setTitle(null);
                al.setContentText("Invalid username or password!");
                al.setHeaderText(null);
                al.show();
                PassTxt.setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginfxmlController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dm = new DataModel();
    }    
    
}
