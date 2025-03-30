package app;

import java.io.IOException;
import java.util.Scanner;

import service.AccountService;
import service.TransactionService;
import service.UserService;

public class CoreBankingSystemMain {
    public static void main(String[] args) {
    	
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();
        AccountService accountService = new AccountService();
        TransactionService transactionService = new TransactionService();

        System.out.println("Welcome to Core Banking System");

        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;
            }

            try {
                if (choice == 1) {
                    // Registration block
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();

                    boolean registered = userService.registerUser(username, password, email);
                    if (registered) {
                        System.out.println("Registration successful!");
                        // In a real system, you would retrieve and store the user id.
                    } else {
                        System.out.println("Registration failed. Please try again.");
                    }

                } else if (choice == 2) {
                    // Login block
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    if (userService.authenticateUser(username, password)) {
                        System.out.println("Login successful!");
                        
                        // Ask the user for their registered user id
                        System.out.print("Enter your registered user id: ");
                        int userId;
                        try {
                            userId = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid user id. Returning to main menu.");
                            continue;
                        }
                        
                        // Check if the user already has an account
                        System.out.print("Do you already have an account? (yes/no): ");
                        String hasAccount = scanner.nextLine();
                        int accountId = -1;
                        if ("yes".equalsIgnoreCase(hasAccount)) {
                            System.out.print("Enter your account id: ");
                            try {
                                accountId = Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid account id. Returning to main menu.");
                                continue;
                            }
                        } else {
                            // Create an account automatically if the user doesn't have one.
                            accountId = accountService.createAccount(userId);
                            if (accountId != -1) {
                                System.out.println("Account created successfully. Your account id is: " + accountId);
                            } else {
                                System.out.println("Account creation failed. Returning to main menu.");
                                continue;
                            }
                        }
                        
                        boolean loggedIn = true;
                        while (loggedIn) {
                            System.out.println("\nAccount Menu:");
                            System.out.println("1. Deposit");
                            System.out.println("2. Withdraw");
                            System.out.println("3. Check Balance");
                            System.out.println("4. Delete Account");
                            System.out.println("5. Logout");
                            System.out.print("Enter your choice: ");
                            
                            int accountChoice;
                            try {
                                accountChoice = Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a valid number.");
                                continue;
                            }
                            
                            switch (accountChoice) {
                                case 1:
                                    System.out.print("Enter deposit amount: ");
                                    try {
                                        double depositAmount = Double.parseDouble(scanner.nextLine());
                                        if (depositAmount <= 0) {
                                            System.out.println("Deposit amount must be positive.");
                                            break;
                                        }
                                        boolean depositSuccess = transactionService.deposit(accountId, depositAmount);
                                        System.out.println(depositSuccess ? "Deposit successful!" : "Deposit failed.");
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid amount entered.");
                                    }
                                    break;
                                case 2:
                                    System.out.print("Enter withdrawal amount: ");
                                    try {
                                        double withdrawalAmount = Double.parseDouble(scanner.nextLine());
                                        if (withdrawalAmount <= 0) {
                                            System.out.println("Withdrawal amount must be positive.");
                                            break;
                                        }
                                        boolean withdrawSuccess = transactionService.withdraw(accountId, withdrawalAmount);
                                        System.out.println(withdrawSuccess ? "Withdrawal successful!" : "Withdrawal failed or insufficient balance.");
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid amount entered.");
                                    }
                                    break;
                                case 3:
                                    double balance = accountService.getBalance(accountId);
                                    System.out.println("Your current balance is: " + balance);
                                    break;
                                case 4:
                                    System.out.print("Are you sure you want to delete your account? (yes/no): ");
                                    String confirmation = scanner.nextLine();
                                    if ("yes".equalsIgnoreCase(confirmation)) {
                                        boolean deleted = accountService.deleteAccount(accountId);
                                        if (deleted) {
                                            System.out.println("Account deleted successfully.");
                                            loggedIn = false; // Log out after deletion
                                        } else {
                                            System.out.println("Account deletion failed.");
                                        }
                                    } else {
                                        System.out.println("Account deletion cancelled.");
                                    }
                                    break;
                                case 5:
                                    System.out.println("Logging out...");
                                    loggedIn = false;
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please try again.");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("Invalid credentials! Please try again.");
                    }
                } else if (choice == 3) {
                    System.out.println("Exiting Core Banking System. Goodbye!");
                    break;
                } else {
                    System.out.println("Invalid choice. Please select from the menu.");
                }
            } catch (RuntimeException | IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
