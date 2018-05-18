package switcher.pkg2.pkg0;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.SwingUtilities;

/**
 *
 * @author Spongebob
 */
public class Switcher20 extends Application {

    
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        //root = FXMLLoader.load(getClass().getResource("Ham.css"));
        String css = Switcher20.class.getResource("Ham.css").toExternalForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
        
    }
    
}
