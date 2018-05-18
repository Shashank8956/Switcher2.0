package switcher.pkg2.pkg0;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    
    DatabaseConnection()
    {   System.out.println("Hello!");
    }
    
    public static Connection Connect(){
        try {
            // db parameters
            String url = "jdbc:sqlite:C:/sqlite/FileSystem.db";
            Class.forName("org.sqlite.JDBC");
            
            // create a connection to the database
            Connection conn = DriverManager.getConnection(url);
            return conn;
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            return null;
        }
    }
}


