package switcher.pkg2.pkg0;


import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

public class Switcher implements Runnable{

    String exe1=null, exe2=null, path1=null, path2=null, play1=null, play2=null;
    static Process pr, pCheck;
    static int Time1=-1, Time2=-1, Pause=-1, exeCnt=0;
    private AtomicBoolean keepRunning;
    BufferedReader input;
    
    Switcher()
    {
        keepRunning = new AtomicBoolean(true);
        exeCnt=0;
        initVal();
        //System.out.println(exe1+"\n"+exe2+"\n"+path1+"\n"+play1+"\n"+path2);
        System.out.println(play1+"//////////////////"+path2);
    }
    
    public void initVal()
    {
        int i=0;
        try {
		File file = new File("Prog.sys");
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;
		while ((line = bufferedReader.readLine()) != null) {
                      if(i==0)
                         exe1=line+".exe";
                      else if(i==1)
                         path1=line;
                      else if(i==2)
                        {
                             play1=line;
                        }
                      else if(i==3)
                          exe2=line+".exe";
                      else if(i==4)
                          path2=line;
                      else if(i==5)
                        {
                             play2=line;
                        }
                          i++;
                    }
		fileReader.close();
                System.out.println("AFTER THIS LINE!!");
                //System.out.println(exe1+"\n"+path1+"\n"+play1+"\n"+exe2+"\n"+path2+"\n"+play2);
                //System.out.println(path1 + exe1 + " \"" + play1 + "\"");
	} catch (IOException ex) {
		Alert al = new Alert(Alert.AlertType.ERROR);
                al.setTitle("Error!");
                al.setContentText(ex.toString());
                al.setHeaderText(null);
                al.show();
	}
        
        
        try {
		File file = new File("Time.sys");
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;
                i=0;
		while ((line = bufferedReader.readLine()) != null) {
                      if(i==0)
                         Time1=Integer.parseInt(line);
                      else if(i==1)
                         Time2=Integer.parseInt(line);
                      else if(i==2)
                          Pause=Integer.parseInt(line);
                      i++;
                    }
		fileReader.close();
                //System.out.println(Time1);
                //System.out.println(Time2);
                //System.out.println(Pause);
                //System.out.println(path1 + exe1 + " \"" + play1 + "\"");
	} catch (IOException ex) {
		Alert al = new Alert(Alert.AlertType.ERROR);
                al.setTitle("Error!");
                al.setContentText(ex.toString());
                al.setHeaderText(null);
                al.show();
	}
    }
    
    public void openExe2()
    {   System.out.println("Inside openExe2");
        close();
        try {
                Runtime rt = Runtime.getRuntime();
                //pr = rt.exec("\"C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe\" \""+path+"\""); 
                pr = rt.exec(path2 + exe2 + " \"" + play2 + "\"");
                System.out.println("End of openExe2 \n"+path2 + exe2 + " \"" + play2 + "\"");

            } catch(Exception e) {
                Alert al = new Alert(Alert.AlertType.ERROR);
                al.setTitle("Error!");
                al.setContentText(e.toString());
                al.setHeaderText(null);
                al.show();
            }
    }
    
    public void openExe1() {
        System.out.println("Inside openExe1");
        close();
        
         try {
                Runtime rt = Runtime.getRuntime();
                //pr = rt.exec("\"C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe\" \""+path+"\""); 
                pr = rt.exec(path1 + exe1 + " \"" + play1 + "\"");
                System.out.println(path1 + exe1 + " \"" + play1 + "\"");
                exeCnt=1;
                System.out.println("End of openExe1");
                //Thread.sleep(10000);

            } catch(Exception e) {
                Alert al = new Alert(Alert.AlertType.ERROR);
                al.setTitle("Error!");
                al.setContentText(e.toString());
                al.setHeaderText(null);
                al.show();
            }
        
    }


    public void close() {
            //stop();
            if(pr!=null)
                pr.destroy();
        }

    public void stop() {
            pr.destroy();
            pCheck.destroy();
            pr = null;
            pCheck = null;
            exeCnt = 0;
        try {
            keepRunning.set(false);
            Process pkill = Runtime.getRuntime().exec("taskkill /f /im " + exe2);
            pkill = Runtime.getRuntime().exec("taskkill /f /im "+exe1);
            close();
            pkill.destroy();
        } catch (IOException ex) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error!");
            al.setContentText(ex.toString());
            al.setHeaderText(null);
            al.show();
        }
    }
    
    @Override
    public void run() {
        int pause = 0;
        DateFormat t = new SimpleDateFormat("HHmm");
        
        while (keepRunning.get()) {
            int SysTime;
            String line, pidInfo = "";
            Date time = new Date();
            System.out.println(t.format(time));
            SysTime = Integer.parseInt(t.format(time));
            System.out.println("START exeCnt=="+exeCnt);
                if(SysTime==Time1 || SysTime==Time2)
                {
                    System.out.println("2nd EXE");
                    exeCnt=0;
                                                                                //2nd exe to execute
                    try {
                    pCheck = null;
                    pCheck = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
                    input  = new BufferedReader(new InputStreamReader(pCheck.getInputStream()));

                    while ((line = input.readLine()) != null) {
                        pidInfo+=line; 
                    }
                    input.close();
                    
                    if(pidInfo.contains(exe1))
                     {
                        pCheck = null;
                        pCheck =Runtime.getRuntime().exec("taskkill /f /im "+exe1);
                        System.out.println("Stoped! "+exe1+" inside 2nd exe case!!!");
                        exeCnt=0;
                     }
                    else if(pidInfo.contains(exe2))
                     {
                        pCheck = null;
                        pCheck =Runtime.getRuntime().exec("taskkill /f /im "+exe2);
                        System.out.println("Stoped! "+exe2+" inside 2nd exe case!!!");
                     }
                    else
                     {
                        System.out.println("Both exe closed! 2nd case");
                        if(play2.contains("http://") || play2.contains("https://") || play2.contains("www.") || play2.contains(".com") || play2.contains(".html") || play2.contains(".htm") || play2.contains("/"))                //if(path2.length()<3)
                        {
                            close();
                            System.out.println("URL");
                            try {
                                if(Desktop.isDesktopSupported())
                                  {
                                    Desktop.getDesktop().browse(new URI(play2));
                                  }
            
                            } catch (Exception ex) {
                                    Alert al = new Alert(Alert.AlertType.ERROR);
                                    al.setTitle("Error!");
                                    al.setContentText(ex.toString());
                                    al.setHeaderText(null);
                                    al.show();
                            }         
                        }
                        else 
                        {   
                            System.out.println("Not URL!");
                            openExe2();
                        }
                        Thread.sleep(Pause*1000*60);
                     }
                    } catch (Exception ex) {
                        Alert al = new Alert(Alert.AlertType.ERROR);
                        al.setTitle("Error!");
                        al.setContentText(ex.toString());
                        al.setHeaderText(null);
                        al.show();
                    }
                    //Pause
                }
                else
                {
                    System.out.println("1st EXE");
                                                                                //1st exe to execute
                    try {
                    pCheck = null;
                    pCheck =Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
                    input =  new BufferedReader(new InputStreamReader(pCheck.getInputStream()));

                    while ((line = input.readLine()) != null) {
                        pidInfo+=line; 
                    }
                    input.close();
                    
                    if(pidInfo.contains(exe2))
                    {
                        pCheck = null;
                        pCheck =Runtime.getRuntime().exec("taskkill /f /im "+exe2);
                        System.out.println("Stoped! "+exe2+" inside 1st exe case!!!");
                        exeCnt = 0;
                    }
                    else if(pidInfo.contains(exe1) && exeCnt==0)
                    {
                        pCheck = null;
                        pCheck =Runtime.getRuntime().exec("taskkill /f /im "+exe1);
                        System.out.println("Stoped! "+exe1+" inside 2nd exe case!!!");
                        exeCnt=0;
                    }
                    else if(exeCnt==0)
                    {
                        openExe1();
                        exeCnt=1;
                    }
                    else
                        System.out.println("WTF!!!! exeCnt=="+exeCnt);
                    } catch (IOException ex) {
                        Alert al = new Alert(Alert.AlertType.ERROR);
                        al.setTitle("Error!");
                        al.setContentText(ex.toString());
                        al.setHeaderText(null);
                        al.show();
                    }
                    finally {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Switcher.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    
    
    /*@Override
    public void run() {
        int pause = 0;
        DateFormat t = new SimpleDateFormat("HHmm");
        
        while (keepRunning.get()) {
            int SysTime;
            //int Time1, Time2;
            //System.out.println("Thread Running!");
            String line, pidInfo = "";
            Date time = new Date();
            System.out.println(t.format(time));
            SysTime = Integer.parseInt(t.format(time));
            //Time1 = 0000;
            //Time2 = 1243;
            
            if(SysTime<=1200)
            {
                if(SysTime==Time1)
                {
                    //2nd exe to execute
                    try {
                    pCheck = null;
                    pCheck =Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
                    input =  new BufferedReader(new InputStreamReader(pCheck.getInputStream()));

                    while ((line = input.readLine()) != null) {
                        pidInfo+=line; 
                    }
                    input.close();
                    if(pidInfo.contains(exe2))
                    {
                        pCheck = null;
                        pCheck =Runtime.getRuntime().exec("taskkill /f /im "+exe2);
                        System.out.println("Yay! "+exe2+"!!!");
                        continue;
                    }
                    else
                    {
                        close();
                        openExe2();
                        Thread.sleep(Pause*1000*60);
                    }
                    } catch (Exception ex) {
                        Logger.getLogger(Switcher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //Pause
                }
                else
                {
                    //1st exe to execute
                    try {
                    pCheck = null;
                    pCheck =Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
                    input =  new BufferedReader(new InputStreamReader(pCheck.getInputStream()));

                    while ((line = input.readLine()) != null) {
                        pidInfo+=line; 
                    }
                    input.close();
                    if(pidInfo.contains(exe1))
                    {
                        System.out.println("Yay! "+exe1+"!!!");
                        continue;
                    }
                    else
                    {
                        openExe1();
                    }
                    } catch (IOException ex) {
                        Logger.getLogger(Switcher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            else if(SysTime>1200)
            {
                if(SysTime==Time2)
                {
                    //2nd exe to execute
                    try {
                    pCheck = null;
                    pCheck =Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
                    input =  new BufferedReader(new InputStreamReader(pCheck.getInputStream()));

                    while ((line = input.readLine()) != null) {
                        pidInfo+=line; 
                    }
                    input.close();
                    if(pidInfo.contains(exe2))
                    {
                        pCheck = null;
                        pCheck =Runtime.getRuntime().exec("taskkill /f /im "+exe2);
                        System.out.println("Yay! "+exe2+"!!!");
                        continue;
                    }
                    else
                    {
                        close();
                        openExe2();
                        Thread.sleep(Pause*1000*60);
                    }
                    } catch (Exception ex) {
                        Logger.getLogger(Switcher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    close();
                    //pause
                }
                else
                {
                    try {
                    pCheck = null;
                    pCheck =Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
                    input =  new BufferedReader(new InputStreamReader(pCheck.getInputStream()));

                    while ((line = input.readLine()) != null) {
                        pidInfo+=line; 
                    }
                    input.close();
                    if(pidInfo.contains(exe1))
                    {
                        System.out.println("Yay! "+exe1+"!!!");
                        continue;
                    }
                    else
                    {
                        openExe1();
                    }
                    } catch (IOException ex) {
                        Logger.getLogger(Switcher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
}*/
}  

