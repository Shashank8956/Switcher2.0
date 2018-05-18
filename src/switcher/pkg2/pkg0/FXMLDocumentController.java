package switcher.pkg2.pkg0;

import com.jfoenix.controls.JFXAlert;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.io.BufferedWriter;
import javafx.scene.shape.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;

/**
 *
 * @author Spongebob
 */
public class FXMLDocumentController implements Initializable {
    
    private int cnt =0, selectedIndex = -1;
    private String prog1=null;
    private String time1=null, time2=null, oldPath=null;
    private Switcher s;
    private ArrayList<Model> mod = new ArrayList<Model>();
    public DataModel dm = new DataModel();
    private FileSwitcher fs;
    
    @FXML
    private JFXButton MainBtn, BtnPath, BtnTime;

    @FXML
    private JFXHamburger Ham;

    @FXML
    private JFXTextField Time2, Time1, CustFileChooser;

    @FXML
    private Pane Right_Pane, SettingsPane2;
    
    @FXML
    private Label Logo;
    
    @FXML
    private JFXListView<String> FileList;
    
    @FXML
    private Rectangle Rect1, Rect2;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        prog1=null;
        if(CustFileChooser.getText()!=null){
            prog1 = CustFileChooser.getText();
        }else{
            System.out.println("CustFileChooser is empty! WTF!");
        }
        
        time1=null;
        if(Time1.getText()!=null)
            time1 = Time1.getText();
        if(time1.length()<1)
            time1 = null;
        
        time2=null;
        if(Time2.getText()!=null)
            time2 = Time2.getText();
        if(time2.length()<1)
            time2 = null;
        
        if(prog1==null || time1==null || time2==null)
        {
            Alert al = new Alert(Alert.AlertType.WARNING);
            al.setTitle("Warning!");
            al.setContentText("Please enter all required fields!");
            al.setHeaderText(null);
            al.show();
        }
        else if(mod.size()>=10){
            System.out.println("Size: "+mod.size());
            Alert al = new Alert(Alert.AlertType.WARNING);
            al.setTitle("Warning!");
            al.setContentText("Maximum file limit reached!");
            al.setHeaderText(null);
            al.show();
        } else
        {
            try {
                StringBuilder str = new StringBuilder();
                String temp = prog1;
                int i = temp.length()-1;
                while(temp.charAt(i)!='\\')
                {
                    //str.append(temp.charAt(i-1));
                    i--;
                }
                i++;
                while(i<temp.length())
                {
                    str.append(temp.charAt(i));
                    i++;
                }
                temp=str.toString();
                System.out.println(temp);
                System.out.println(prog1);
                
                
                Model tempMod = new Model(temp, prog1, Integer.parseInt(time1), Integer.parseInt(time2));
//Insert statement below                
                /*if(selectedIndex!=-1){
                    String updateQuery = "UPDATE FileData "+
                             "SET FileName = '"+tempMod.getFileName()+"', "+
                             "FilePath = '"+tempMod.getFilePath()+"', "+
                             "StartTime = '"+tempMod.getStartTime()+"', "+                           
                             "EndTime = '"+tempMod.getEndTime()+"' "+                           
                             "WHERE FilePath = '"+oldPath+"';";
                    System.out.println(updateQuery);
                    dm.updateRow(updateQuery);
                }else{
                    dm.addData(tempMod);
                }*/
                if(selectedIndex!=-1){
                    for(int j=0; j<mod.size(); j++){
                        if(mod.get(j).getFilePath().equals(tempMod.getFilePath())){
                            System.out.println("IF part");
                            mod.get(j).setName(tempMod.getFileName());
                            mod.get(j).setPath(tempMod.getFilePath());
                            mod.get(j).setStart(tempMod.getStartTime());
                            mod.get(j).setEnd(tempMod.getEndTime());
                            break;
                        }
                    }
                    System.out.println("Updated!");
                }else{
                    mod.add(tempMod);
                    System.out.println("Added!");
                }
                
                populateListView();
                JFXSnackbar bar = new JFXSnackbar(SettingsPane2);
                bar.show("Save Successfull!", 2500);
                
            } catch (Exception ex) {
                    Alert al = new Alert(Alert.AlertType.ERROR);
                    al.setTitle("Error!");
                    al.setContentText("Start time or end time already exist for another file!");
                    al.setHeaderText(null);
                    al.showAndWait();
                    Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
                    
            }
        }
        
        Time1.setText(null);
        Time2.setText(null);
        CustFileChooser.setText(null);
        time1 = null;
        time2 = null;
        prog1 = null;
        oldPath = null;
        selectedIndex = -1;
    }
    
    @FXML
    private void mainButtonAction(ActionEvent event) {
        String text;
        
        //text = MainBtn.getText();
        if(MainBtn.getText().equals("START"))
        {
            fs = new FileSwitcher(mod);
            Thread t = new Thread(fs);
            t.start();
            MainBtn.setText("STOP");
        }
        else if(MainBtn.getText().equals("STOP"))
        {
            fs.stop();
            MainBtn.setText("START");
            populateListView();
        }
    }
    
    @FXML
    public void handleFileChooserBtn(MouseEvent event)
    {
        CustFileChooser.setText(null);
        File file;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        Stage stage = new Stage();
        file = fileChooser.showOpenDialog(stage);
        CustFileChooser.setText(file.getAbsolutePath());
    }
    
    public void populateListView(){
        FileList.getItems().clear();
        //mod = dm.getDataList();
        
            for(int i=0; i<mod.size(); i++)
            {
                FileList.getItems().add((i+1)+".  "+mod.get(i).getFileName());
            }
            int i=-1;
            FileList.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent arg0) {
                getSelection(FileList.getSelectionModel().getSelectedIndex());
            }
            });
    }
    
    public void getSelection(int i)
    {
        System.out.println(i);
        selectedIndex = i;
        CustFileChooser.setText(mod.get(selectedIndex).getFilePath());
        Time1.setText(String.valueOf(mod.get(selectedIndex).getStartTime()));
        Time2.setText(String.valueOf(mod.get(selectedIndex).getEndTime()));
        oldPath = mod.get(selectedIndex).getFilePath();
    }
    
    public void deleteSelected()
    {
        if(selectedIndex!=-1){
            mod.remove(selectedIndex);
            populateListView();
            Time1.setText(null);
            Time2.setText(null);
            CustFileChooser.setText(null);
            time1 = null;
            time2 = null;
            prog1 = null;
            oldPath = null;
            selectedIndex = -1;
        }else{
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error!");
            al.setContentText("Select something to delete!");
            al.setHeaderText(null);
            al.showAndWait();
        }
        
        JFXSnackbar bar = new JFXSnackbar(SettingsPane2);
        bar.show("Item Deleted!", 2000);
        /*try {
            dm.delete(mod.get(selectedIndex).getStartTime(), mod.get(selectedIndex).getEndTime());
            JFXSnackbar bar = new JFXSnackbar(SettingsPane2);
            bar.show("Item Deleted!", 2000);
        } catch (SQLException ex) {
            Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error!");
            al.setContentText("Selected file no longer exist!");
            al.setHeaderText(null);
            al.showAndWait();
        } catch(Exception ex)
        {
            Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error!");
            al.setContentText("Select something to delete!");
            al.setHeaderText(null);
            al.showAndWait();
        } finally{
            populateListView();
            Time1.setText(null);
            Time2.setText(null);
            CustFileChooser.setText(null);
            time1 = null;
            time2 = null;
            prog1 = null;
            oldPath = null;
            selectedIndex = -1;
        }*/
    }
    
    public void clearAllBtn()
    {
        mod.clear();
        populateListView();
        /*dm.clearAll();
        
        
        Time1.setText(null);
        Time2.setText(null);
        CustFileChooser.setText(null);
        time1 = null;
        time2 = null;
        prog1 = null;
        oldPath = null;
        selectedIndex = -1;*/
        
        JFXSnackbar bar = new JFXSnackbar(SettingsPane2);
        bar.show("Cleared all!", 2000);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager depth = new JFXDepthManager();
        depth.setDepth(SettingsPane2, 2);
        depth.setDepth(Rect1, 1);
        depth.setDepth(Rect2, 1);
        
        GaussianBlur gaussianBlur = new GaussianBlur();       
                
        if(dm.isDbConnected())
            System.out.println("Connection Established!");
        else
            System.out.println("Connection Failed!");
        
        populateListView();
        
        HamburgerBackArrowBasicTransition hb = new HamburgerBackArrowBasicTransition(Ham);
        hb.setRate(-1);
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), SettingsPane2);
        Ham.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            hb.setRate(hb.getRate()*-1);
            hb.play();
            
            if(cnt==0)
            {
                tt.setByX(400f);     //Initially 530
                //tt.setCycleCount(40);
                tt.setAutoReverse(false);
                gaussianBlur.setRadius(10.5); 
                Right_Pane.setEffect(gaussianBlur);
                tt.play();
                cnt++;
            }else
             {
                tt.setByX(-400f);     //Initially -530
                //tt.setCycleCount(40);
                tt.setAutoReverse(false);
                gaussianBlur.setRadius(0); 
                Right_Pane.setEffect(gaussianBlur);
                tt.play();
                Time1.setText(null);
                Time2.setText(null);
                CustFileChooser.setText(null);
                time1 = null;
                time2 = null;
                prog1 = null;
                oldPath = null;
                selectedIndex = -1;
                cnt--;
             }
        });
        
        //Logo = new Label();
        Logo.setText("Hello");
        DateFormat t = new SimpleDateFormat("HH:mm");
        final Timeline timeline = new Timeline(
            new KeyFrame(
                Duration.millis( 1000 ),
                event -> {
                    int SysTime;
                    String str=null;
                    Date time = new Date();
                    //System.out.println(t.format(time));
                    str = t.format(time);
                    Logo.setText(str);
                }
            )
        );
        timeline.setCycleCount( Animation.INDEFINITE );
        timeline.play();
    }    
    
}
