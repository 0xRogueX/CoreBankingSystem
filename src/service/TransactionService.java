package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import in.jdbcutil.JDBCUtil;

public class TransactionService {

    // Deposits a specified amount into an account and logs the transaction.
    public boolean deposit(int accountId, double amount) {
        String updateAccount = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
        String insertTransaction = "INSERT INTO transactions (account_id, type, amount) VALUES (?, 'deposit', ?)";
        
        try (Connection conn = JDBCUtil.getJdbcConnection()) {
            conn.setAutoCommit(false);  // Start transaction

            try (PreparedStatement updateStmt = conn.prepareStatement(updateAccount);
                 PreparedStatement insertStmt = conn.prepareStatement(insertTransaction)) {
                 
                // Update account balance
                updateStmt.setDouble(1, amount);
                updateStmt.setInt(2, accountId);
                updateStmt.executeUpdate();

                // Log the deposit transaction
                insertStmt.setInt(1, accountId);
                insertStmt.setDouble(2, amount);
                insertStmt.executeUpdate();

                conn.commit();
                conn.setAutoCommit(true); // Reset auto-commit to default
                return true;
            } catch (SQLException e) {
                conn.rollback();  // Roll back transaction on error
                conn.setAutoCommit(true); // Reset auto-commit after rollback
                throw new RuntimeException("Error during deposit for account " + accountId + ": " + e.getMessage(), e);
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Database error during deposit for account " + accountId + ": " + e.getMessage(), e);
        }
    }

    // Withdraws a specified amount from an account if sufficient balance is available, and logs the transaction.
    public boolean withdraw(int accountId, double amount) throws IOException {
        String checkBalance = "SELECT balance FROM accounts WHERE account_id = ?";
        String updateAccount = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
        String insertTransaction = "INSERT INTO transactions (account_id, type, amount) VALUES (?, 'withdrawal', ?)";
        
        try (Connection conn = JDBCUtil.getJdbcConnection()) {
            conn.setAutoCommit(false);

            // Check for sufficient balance
            try (PreparedStatement checkStmt = conn.prepareStatement(checkBalance)) {
                checkStmt.setInt(1, accountId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getDouble("balance") < amount) {
                        conn.rollback(); // Roll back if insufficient balance.
                        conn.setAutoCommit(true);
                        return false;
                    }
                }
            }

            // Proceed with withdrawal if balance is sufficient.
            try (PreparedStatement updateStmt = conn.prepareStatement(updateAccount);
                 PreparedStatement insertStmt = conn.prepareStatement(insertTransaction)) {
                 
                updateStmt.setDouble(1, amount);
                updateStmt.setInt(2, accountId);
                updateStmt.executeUpdate();

                insertStmt.setInt(1, accountId);
                insertStmt.setDouble(2, amount);
                insertStmt.executeUpdate();

                conn.commit();
                conn.setAutoCommit(true); // Reset auto-commit to default
                return true;
            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true); // Reset auto-commit after rollback
                throw new RuntimeException("Error during withdrawal for account " + accountId + ": " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during withdrawal for account " + accountId + ": " + e.getMessage(), e);
        }
    }
}
