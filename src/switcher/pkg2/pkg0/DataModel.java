 package switcher.pkg2.pkg0;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DataModel {
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement ps =null;
    ResultSet res = null;
    String user=null;
    String pass=null;
    ArrayList<Model> mod = new ArrayList<Model>();
    DataModel()
    {
        try {
            conn = DatabaseConnection.Connect();
            String sql = "CREATE TABLE IF NOT EXISTS FileData (\n"
                        + "	FileName text,\n"
                        + "	FilePath text,\n"
                        + "	StartTime Number PRIMARY KEY,\n"
                        + "	EndTime Number NOT NULL\n"
                        + ");";
            stmt = conn.createStatement();
            stmt.execute(sql);
            if(conn==null)
                System.out.println("Connection failed!!");
        } catch (SQLException ex) {
            Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isDbConnected() {
        try {
            return !conn.isClosed();
        } catch (SQLException ex) {
            return false;
        }
    }
    
    public void addData(Model tempMod) throws SQLException{
      
            String sql = "INSERT into FileData (FileName, FilePath, StartTime, EndTime) "
                    + "values('"+ tempMod.name + "', '"+ tempMod.path +"',"+ tempMod.start+ ", "+tempMod.end+");";
            System.out.println(sql);
            // create a connection to the database
            ps = conn.prepareStatement(sql);
            ps.execute();
      
    }
    
    public ArrayList<Model> getDataList(){
        try {
            String sql = "SELECT * FROM FileData;";
            ps = conn.prepareStatement(sql);
            res = ps.executeQuery();
            StringBuilder str = new StringBuilder();
            while (res.next()) {
                /*String temp = res.getString("FileName");
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
                System.out.println(temp);*/
                mod.add(new Model(res.getString("FileName"), res.getString("FilePath"), res.getInt("StartTime"), res.getInt("EndTime")));
                str.delete(0, str.length());
            }
            return mod;
        } catch (SQLException ex) {
            Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mod;
    }
    
    public void delete(int start, int end) throws SQLException{
            String sql = "DELETE from FileData "
                    + "where StartTime = '"+start+"' and EndTime = '"+end+"';";
            System.out.println(sql);
            // create a connection to the database
            ps = conn.prepareStatement(sql);
            ps.execute();
    }
    
    public void clearAll(){
        Alert al = new Alert(Alert.AlertType.WARNING);
        al.setTitle("Warning!");
        al.setContentText("Are you sure? Clear all?");
        al.setHeaderText(null);
        al.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            try {              
                String sql = "DROP table FileData;";
                stmt = conn.createStatement();
                stmt.execute(sql);
                
                sql = "CREATE TABLE IF NOT EXISTS FileData (\n"
                        + "	FileName text,\n"
                        + "	FilePath text,\n"
                        + "	StartTime Number PRIMARY KEY,\n"
                        + "	EndTime Number NOT NULL\n"
                        + ");";
                stmt = conn.createStatement();
                stmt.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        });
    }
    
    public void updateRow(String updateQuery){
        try {
            ps = conn.prepareStatement(updateQuery);
            ps.execute();
        } catch (SQLException ex) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Update Error!");
            al.setContentText("Unable to update record!");
            al.setHeaderText(null);
            al.show();
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
