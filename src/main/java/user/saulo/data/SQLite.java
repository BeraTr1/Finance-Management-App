package user.saulo.data;

import user.saulo.Account;
import user.saulo.FinancesManagementApp;
import user.saulo.Transaction;
import user.saulo.managers.AccountManager;
import user.saulo.managers.TransactionManager;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLite implements Data {
    private final File databaseFile;

    private final AccountManager accountManager;
    private final TransactionManager transactionManager;

    public SQLite(String dataFilePath, String dataFileName) {
        this.databaseFile = new File(dataFilePath + File.separator + dataFileName);
        this.accountManager = FinancesManagementApp.accountManager;
        this.transactionManager = FinancesManagementApp.transactionManager;
    }

    private Connection connect() {
        try {
            String url = "jdbc:sqlite:" + this.databaseFile.getAbsolutePath();
            Connection connection = DriverManager.getConnection(url);

            return connection;
        } catch (SQLException e) {
            System.out.println("Error while trying to create a connection: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void loadAll() throws Exception {
        Connection connection = connect();

        if (connection == null) {
            return;
        }

        loadAccounts(connection);
        loadTransactions(connection);
    }

    @Override
    public void saveAll() throws Exception {
        Connection connection = connect();

        if (connection == null) {
            return;
        }

        createTables(connection);
        saveAccounts(connection);

        deleteOldAccounts(connection);
        deleteOldTransactions(connection);

        connection.close();
    }

    private void createTables(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Accounts(name varchar(255) primary key, description varchar(255), balance real, credit real, debt real, goal real)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Transactions(id varchar(255) primary key, account_name varchar(255), to_account_name varchar(255), amount real, date varchar(255), description varchar(255), notes varchar(255))");
        } catch (SQLException e) {
            System.out.println("Error while trying to create a table: " + e.getMessage());
        }
    }

    private void saveAccounts(Connection connection) {
        List<Account> accounts = accountManager.getAccounts();
        for (Account account : accounts) {
            String accountName = account.getName();
            String accountDescription = account.getDescription();
            double accountBalance = account.getBalance();
            double accountCredit = account.getCredit();
            double accountDebt = account.getDebt();
            double accountGoal = account.getGoal();

            insertAccount(connection, accountName, accountDescription, accountBalance, accountCredit, accountDebt, accountGoal);

            List<Transaction> transactions = account.getTransactions();

            for (Transaction transaction : transactions) {
                String transactionId = transaction.getId().toString();
                String transactionAccountName = transaction.getAccount().getName();
                String transactionToAccountName = transaction.getToAccount() == null ? null : transaction.getToAccount().getName();
                double transactionAmount = transaction.getAmount();
                String transactionDate = transaction.getDate();
                String transactionDescription = transaction.getDescription();
                String transactionNotes = transaction.getNotes();

                insertTransaction(connection, transactionId, transactionAccountName, transactionToAccountName, transactionAmount, transactionDate, transactionDescription, transactionNotes);
            }
        }
    }

    private void deleteOldAccounts(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Accounts");

            if (resultSet == null) {
                return;
            }

            List<String> accountsToDelete = new ArrayList<>();

            while (resultSet.next()) {
                String accountName = resultSet.getString("name");

                if (accountManager.accountExists(accountName)) {
                    continue;
                }

                accountsToDelete.add(accountName);
            }

            for (String accountName : accountsToDelete) {
                statement.executeUpdate("DELETE FROM Accounts WHERE name='" + accountName + "'");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteOldTransactions(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Transactions");

            if (resultSet == null) {
                return;
            }

            List<String> transactionsToDelete = new ArrayList<>();

            while (resultSet.next()) {
                String accountName = resultSet.getString("account_name");

                if (accountManager.accountExists(accountName)) {
                    continue;
                }

                transactionsToDelete.add(accountName);
            }

            for (String accountName : transactionsToDelete) {
                statement.executeUpdate("DELETE FROM Transactions WHERE account_name='" + accountName + "'");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertAccount(Connection connection, String accountName, String accountDescription, double accountBalance, double accountCredit, double accountDebt, double accountGoal) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("REPLACE INTO Accounts(name, description, balance, credit, debt, goal) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, accountName);
            preparedStatement.setString(2, accountDescription);
            preparedStatement.setDouble(3, accountBalance);
            preparedStatement.setDouble(4, accountCredit);
            preparedStatement.setDouble(5, accountDebt);
            preparedStatement.setDouble(6, accountGoal);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Error while trying to insert account into database: " + e.getMessage());
        }
    }

    private void insertTransaction(Connection connection, String id, String accountName, String toAccountName, double amount, String date, String description, String notes) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("REPLACE INTO Transactions(id, account_name, to_account_name, amount, date, description, notes) VALUES (?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, accountName);
            preparedStatement.setString(3, toAccountName);
            preparedStatement.setDouble(4, amount);
            preparedStatement.setString(5, date);
            preparedStatement.setString(6, description);
            preparedStatement.setString(7, notes);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Error while trying to insert account into database: " + e.getMessage());
        }
    }

    private void loadAccounts(Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet == null) {
                return;
            }

            while (resultSet.next()) {
                String accountName = resultSet.getString("name");
                String accountDescription = resultSet.getString("description");
                double accountBalance = resultSet.getDouble("balance");
                double accountCredit = resultSet.getDouble("credit");
                double accountDebt = resultSet.getDouble("debt");
                double accountGoal = resultSet.getDouble("goal");

                Account account = new Account(accountName, accountDescription, accountBalance, accountCredit, accountDebt, accountGoal);
                accountManager.addAccount(account);
            }
        } catch (Exception e) {
            System.out.println("Error while trying to load accounts: " + e.getMessage());
        }
    }

    private void loadTransactions(Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Transactions");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet == null) {
                return;
            }

            while (resultSet.next()) {
                UUID transactionId = UUID.fromString(resultSet.getString("id"));
                String accountName = resultSet.getString("account_name");
                String toAccountName = resultSet.getString("to_account_name");
                double amount = resultSet.getDouble("amount");
                String date = resultSet.getString("date");
                String description = resultSet.getString("description");
                String notes = resultSet.getString("notes");
                Account account = accountManager.getAccountFromName(accountName);
                Account toAccount = accountManager.getAccountFromName(toAccountName);

                Transaction transaction = new Transaction(transactionId, account, toAccount, amount, description, notes, date);
                transactionManager.addTransaction(transaction);
                accountManager.addTransaction(account, transaction);
            }
        } catch (Exception e) {
            System.out.println("Error while trying to load accounts: " + e.getMessage());
        }
    }
}
