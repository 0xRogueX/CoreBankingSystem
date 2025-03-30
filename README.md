# Core Banking System

## Overview
The **Core Banking System** is a console-based Java application designed to simulate basic banking operations. It allows users to register, log in, manage accounts, deposit and withdraw funds, check balances, and delete accounts. The system is built using JDBC for database connectivity and follows a clean, modular structure.

## Features
- **User Registration and Login**
- **Account Creation**
- **Deposit and Withdrawal Management**
- **Account Balance Check**
- **Account Deletion**
- **Transaction Management**

## Tech Stack
- **Java** - Core Java for application logic
- **JDBC** - Database connectivity
- **MySQL** - Database Management
- **Eclipse or VS Code** - IDE for development

## Project Structure
```
CoreBankingSystem
├── src
│   ├── app
│   │   └── CoreBankingSystemMain.java
│   ├── service
│   │   ├── AccountService.java
│   │   ├── TransactionService.java
│   │   └── UserService.java
│   └── in.jdbcutil
│       └── JDBCUtil.java
└── README.md
```

## Database Schema
```sql
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100)
);

CREATE TABLE accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    balance DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT,
    type VARCHAR(20),
    amount DECIMAL(10,2),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);
```

## Setup Instructions

1. **Clone the Repository:**
```bash
git clone https://github.com/your_username/CoreBankingSystem.git
cd CoreBankingSystem
```

2. **Configure Database:**
- Install and start MySQL.
- Create a database named `projects`.
- Execute the SQL script above to create tables.

3. **Configure JDBC:**
Update `JDBCUtil.java` with your database credentials:
```java
String url = "jdbc:mysql://localhost/projects";
String username = "your_username";
String password = "your_password";
```

4. **Run the Application:**
- Open the project in your IDE.
- Compile and run `CoreBankingSystemMain.java`.

## Usage
- **Register**: Create a new user account.
- **Login**: Authenticate using your credentials.
- **Manage Account**: Create, delete, or manage transactions.
- **Deposit/Withdraw**: Perform transactions securely.

## Error Handling
The application includes robust error handling for exceptions like:
- SQL errors
- Invalid input
- Insufficient balance

## Future Enhancements
- Implement password hashing for security.
- Add admin functionality for user management.
- Create a web-based front-end.

## Contribution

Contributions are highly appreciated! If you'd like to enhance or extend this project, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Commit your changes with clear, descriptive messages.
4. Open a pull request detailing your changes and improvements.

Please ensure your contributions adhere to the existing code style and include any necessary tests or documentation updates.

If you have any questions, suggestions, or feedback, feel free to reach out! You can contact **Vivek Dadhaniya** via:

- **GitHub:** [https://github.com/your_username](https://github.com/your_username)
- **LinkedIn**: [0xRogueX](https://www.linkedin.com/in/vivekdadhaniya/)
- **Discord**: [0xRogueX](https://discord.com/users/1073565428233801738)
