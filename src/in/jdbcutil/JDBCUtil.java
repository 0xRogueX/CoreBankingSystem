package in.jdbcutil;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtil {

    // Returns a connection to the database by reading from the Application.properties file.
    public static Connection getJdbcConnection() throws IOException, SQLException {
    	
        // Use try-with-resources to ensure the FileInputStream is closed
        try (FileInputStream fis = new FileInputStream("Application.properties")) {
        	
            Properties props = new Properties();
            props.load(fis);
            String url = props.getProperty("url");
            String user = props.getProperty("user");
            String password = props.getProperty("password");
            return DriverManager.getConnection(url, user, password);
        }
    }
}
