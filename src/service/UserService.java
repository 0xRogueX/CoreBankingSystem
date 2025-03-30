package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import in.jdbcutil.JDBCUtil;

public class UserService {

    // Registers a new user. Returns true if registration is successful.
    public boolean registerUser(String username, String password, String email) {
    	
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = JDBCUtil.getJdbcConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException | IOException e) {
            // Throw a runtime exception with context for better debugging
            throw new RuntimeException("Error registering user: " + e.getMessage(), e);
        }
    }

    // Authenticates a user by comparing username and password.
    public boolean authenticateUser(String username, String password) {
    	
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = JDBCUtil.getJdbcConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            // Wrap ResultSet in try-with-resources to ensure it is closed
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Returns true if a matching record is found
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Error authenticating user: " + e.getMessage(), e);
        }
    }
}
