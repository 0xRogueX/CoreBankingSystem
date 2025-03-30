package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import in.jdbcutil.JDBCUtil;

public class AccountService {

    // Creates a new account for a user and returns the generated account ID.
    public int createAccount(int userId) {
        String sql = "INSERT INTO accounts (user_id, balance) VALUES (?, 0)";
        try (Connection conn = JDBCUtil.getJdbcConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
             
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            
            // Use try-with-resources to manage the ResultSet for generated keys.
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1); // Return the new account ID
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Failed to create account for user " + userId + ": " + e.getMessage(), e);
        }
        return -1; // Indicates failure if no key is generated.
    }

    // Retrieves the balance for a given account.
    public double getBalance(int accountId) {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        try (Connection conn = JDBCUtil.getJdbcConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Failed to get balance for account " + accountId + ": " + e.getMessage(), e);
        }
        return 0.0;
    }
    
    public boolean deleteAccount(int accountId) {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try (Connection conn = JDBCUtil.getJdbcConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, accountId);
            int rows = stmt.executeUpdate();
            return rows > 0; // Returns true if the deletion affected at least one row
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Error deleting account with id " + accountId + ": " + e.getMessage(), e);
        }
    }

}
