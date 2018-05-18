package switcher.pkg2.pkg0;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import static switcher.pkg2.pkg0.Switcher.pCheck;

public class FileSwitcher implements Runnable{
    private ArrayList<Model> mod = new ArrayList<>();
    private AtomicBoolean keepRunning;
    private int LOCK = -1, oldLock = -1, isAlreadyOpen = 0;
    private int SIZE;
    private Process pr = null;
    
    FileSwitcher(ArrayList<Model> mod) {
        keepRunning = new AtomicBoolean(true);
        this.mod = mod;
        System.out.println("Mod size: "+mod.size());
        SIZE = mod.size();
    }
    
    public void start(String filepath)
    {
        try {
            if(filepath.contains(".html") || filepath.contains(".htm"))
            {
                String path = "C:\\Program Files\\Internet Explorer\\iexplore.exe";
                Runtime rt = Runtime.getRuntime();
                //pr = rt.exec("\"C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe\" \""+path+"\""); 
                pr = rt.exec(path + " \"" + filepath + "\"");
                
            }
            else {
                File file = new File(filepath);
            
                if(!Desktop.isDesktopSupported()){
                    System.out.println("Desktop is not supported");
                    return;
                }
                
                Desktop desktop = Desktop.getDesktop();
                //System.out.println(desktop.toString());
                if(file.exists())
                    desktop.open(file);
                } 
        }catch (Exception ex) {
            Logger.getLogger(FileSwitcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close()
    {
        
        try {
            if(pr!=null){
                //if(false)
                    pr.destroy();
            }
            /*Process pkill = Runtime.getRuntime().exec("taskkill /f /im " + oldL);
            pkill = Runtime.getRuntime().exec("taskkill /f /im "+oldL);
            pkill.destroy();*/
        } catch (Exception ex) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error!");
            al.setContentText(ex.toString());
            al.setHeaderText(null);
            al.show();
        }
    }
    
    public void closeSlideShow()
    {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
        } catch (AWTException ex) {
            Logger.getLogger(FileSwitcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if(pr!=null){
                pr.destroy();
            }
        } catch (Exception ex) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error!");
            al.setContentText(ex.toString());
            al.setHeaderText(null);
            al.show();
        }
    }
    
    public void stop()
    {
        try {
            keepRunning.set(false);
            /*Process pkill = Runtime.getRuntime().exec("taskkill /f /im " + mod.get(oldLock).getFileName());
            pkill = Runtime.getRuntime().exec("taskkill /f /im " + mod.get(LOCK).getFileName());
            pkill.destroy();*/
        } catch (Exception ex) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error!");
            al.setContentText(ex.toString());
            al.setHeaderText(null);
            al.show();
        }finally{
            LOCK = -1;
            oldLock = -1;
            mod.clear();
        }
    }
    
    @Override
    public void run() {
        int oldTime=0;
        DateFormat t = new SimpleDateFormat("HHmm");
        while (keepRunning.get()) {
            int SysTime, i=0;
            Date time = new Date();
            SysTime = Integer.parseInt(t.format(time));
            if((SysTime - oldTime)!=0)
                System.out.println(SysTime);
            oldTime = SysTime;
            
            
            for(i=1; i<SIZE; i++)
            {   //System.out.println("Time1: "+mod.get(i).getTime1());
                //System.out.println("Time2: "+mod.get(i).getTime2());
                if((mod.get(i).getStartTime()<=SysTime) && (SysTime<=mod.get(i).getEndTime()))
                {   
                    if(LOCK!=i)
                    {
                        //if(oldLock!=-1)
                        //    close(mod.get(oldLock).getFileName());
                        System.out.println("OldLock: "+ oldLock);
                        if(mod.get(oldLock).getFileName().contains(".ppsx"))
                            closeSlideShow();
                        else
                            close();
                        start(mod.get(i).getFilePath());
                        System.out.println("*************File no: "+i);
                        LOCK=i;
                        oldLock = LOCK;
                    }
                    break;
                }
                
            }
            
            System.out.println("Loop ends at: "+i);
            System.out.println("New LOCK: " + LOCK);
            if((i==SIZE) && (LOCK!=0))
                {
                    if(oldLock>-1 && mod.get(oldLock).getFileName().contains(".ppsx"))
                            closeSlideShow();
                        else
                            close();
                    start(mod.get(0).getFilePath());
                    /*if(oldLock!=-1)
                        close(mod.get(oldLock).getFileName());*/
                    System.out.println("***************Running Default");
                    LOCK = 0;
                    oldLock = LOCK;
                }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(FileSwitcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }       
    }
}
