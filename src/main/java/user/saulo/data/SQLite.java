package user.saulo.data;

import user.saulo.Account;
import user.saulo.FinancesManagementApp;
import user.saulo.managers.AccountManager;
import user.saulo.managers.AppManager;

import java.io.File;
import java.sql.*;
import java.util.List;

public class SQLite implements Data {
    private final File databaseFile;

    private AccountManager accountManager;

    public SQLite(String dataFilePath, String dataFileName) {
        this.databaseFile = new File(dataFilePath + File.separator + dataFileName);
        this.accountManager = FinancesManagementApp.accountManager;
    }

    private Connection connect() {
        try {
            String url = "jdbc:sqlite:" + this.databaseFile.getAbsolutePath();
            Connection connection = DriverManager.getConnection(url);

            System.out.println("Established connection to SQLite");
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
    }

    @Override
    public void saveAll() throws Exception {
        System.out.println("Saving all data into " + databaseFile);

        Connection connection = connect();

        if (connection == null) {
            return;
        }

        saveAccounts(connection);
        deleteOldAccounts(connection);

        connection.close();
    }

    private void saveAccounts(Connection connection) {
        try {
            PreparedStatement createTableStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Accounts(name varchar(255) primary key, description varchar(255), balance real, credit real, debt real, goal real)");
            createTableStatement.executeUpdate();

            List<Account> accounts = FinancesManagementApp.accountManager.getAccounts();
            for (Account account : accounts) {
                String accountName = account.getName();
                String accountDescription = account.getDescription();
                double accountBalance = account.getBalance();
                double accountCredit = account.getCredit();
                double accountDebt = account.getDebt();
                double accountGoal = account.getGoal();

                insertAccount(connection, accountName, accountDescription, accountBalance, accountCredit, accountDebt, accountGoal);
            }
        } catch (SQLException e) {
            System.out.println("Error while trying to save accounts: " + e.getMessage());
        }
    }

    private void deleteOldAccounts(Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet == null) {
                return;
            }

            while (resultSet.next()) {
                String accountName = resultSet.getString("name");

                if (accountManager.accountExists(accountName)) {
                    continue;
                }

                deleteAccount(connection, accountName);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteAccount(Connection connection, String accountName) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Accounts WHERE name='" + accountName + "'");
            preparedStatement.execute();
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
                FinancesManagementApp.accountManager.addAccount(account);
            }
        } catch (Exception e) {
            System.out.println("Error while trying to load accounts: " + e.getMessage());
        }
    }
}
