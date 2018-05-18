package switcher.pkg2.pkg0;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DataBase {
    
    DataBase()
    {   System.out.println("Hello!");
    }
    
    public static Connection Connect(){
        try {
            // db parameters
            String url = "jdbc:sqlite:C:/sqlite/FileSystem.db";
            Class.forName("org.sqlite.JDBC");
            
            // create a connection to the database
            Connection conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            return conn;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}

