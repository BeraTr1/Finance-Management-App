package user.saulo;

import user.saulo.managers.AppManager;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

public class Commands {
    AppManager appManager;

    public void initialize() {
        listCommands();
        String command = getUserString("Please enter a command:").toLowerCase();
        boolean exit = false;
        appManager = FinancesManagementApp.appManager;

        while (!exit) {
            switch (command) {
                case "overview":
                    overview();
                    break;
                case "create account":
                case "createaccount":
                    createAccount();
                    break;
                case "borrow":
                case "borrowmoney":
                case "borrow money":
                    borrowMoney();
                    break;
                case "addmoney":
                case "add":
                    addMoney();
                    break;
                case "removemoney":
                case "remove":
                    removeMoney();
                    break;
                case "inspect":
                    inspectAccount();
                    break;
                case "transfer":
                case "transfermoney":
                    transferMoney();
                    break;
                case "exit":
                    exit = true;
                    break;
                default:
                    break;
            }

            listCommands();
            command = getUserString("Please enter a command:");
        }
    }

    public void listCommands() {
        System.out.println("--- Commands ---");
        System.out.println("Overview - displays useful information about all of your accounts");
        System.out.println("Create Account - creates a new account");
        System.out.println("Borrow Money - transfers money from one account to another, but adds a debt value that you must repay later");
        System.out.println("Transfer Money - transfers money from one account to another");
        System.out.println("Add Balance - adds funds to an account");
        System.out.println("Remove Balance - remove funds from an account");
        System.out.println("Inspect - prints detailed information (such as recent transactions, debt, credit, balance, etc.) for a specific account");
        System.out.println("Exit - exits the program");
        System.out.println();
    }

    public void createAccount() {
        System.out.println("(Creating New Account)");
        String accountName = getUserString("Enter the name of your new account:");

        while (appManager.accountExists(accountName)) {
            accountName = getUserString("An account with this name already exists. Enter another name for your new account:");
        }

        String accountDescription = getUserString("Enter a description for your account (Optional):");

        try {
            appManager.createAccount(accountName, accountDescription);
            System.out.println("Successfully created an account named '" + accountName + "'!");
            System.out.println();
        } catch (Exception e) {
            System.out.println("There was an issue while trying to create a new account: " + e.getMessage());
        }
    }

    public void deleteAccount() {

    }

    public void createTransaction() {

    }

    public void overview() {
        System.out.println("(Overview)");
        List<Account> accounts = appManager.getAccounts();
        System.out.println("You have '" + accounts.size() + "' accounts");
        double totalBalance = 0;
        double totalDebt = 0;

        for (Account account : accounts) {
            totalBalance += account.getBalance();
            totalDebt += account.getDebt();

            System.out.printf("%s: Current Balance ($%.2f) | Balance ($%.2f) + Account Credit ($%.2f) - Account Debt ($%.2f) = Calculated Balance ($%.2f)\n", account.getName(), account.getBalance(), account.getBalance(), account.getCredit(), account.getDebt(), (account.getBalance() + account.getCredit() - account.getDebt()));
        }

        System.out.println("Your total balance is $" + totalBalance + " and your total debt is $" + totalDebt);
        System.out.println();
    }

    public void addMoney() {
        System.out.println("(Add Funds)");
        String accountName = getUserString("Enter the name of the account you want to add funds to:");

        while (!appManager.accountExists(accountName)) {
            accountName = getUserString("This account doesn't exist. Enter the name of an existing account you want to add funds to:");
        }

        double amount = getUserDouble("Enter the amount of money you want to add to " + accountName + ":");

        try {
            appManager.addBalance(accountName, amount);
            System.out.println("Successfully added $" + amount + " to " + accountName);
            System.out.println();
        } catch (Exception e) {
            System.out.println("There was an issue while trying to add funds to an account: " + e.getMessage());
        }
    }

    public void removeMoney() {
        System.out.println("(Remove Funds)");
        String accountName = getUserString("Enter the name of the account you want to remove funds from:");

        while (!appManager.accountExists(accountName)) {
            accountName = getUserString("This account doesn't exist. Enter the name of an existing account you want to remove funds from:");
        }

        double amount = getUserDouble("Enter the amount of money you want to remove from " + accountName + ":");

        try {
            appManager.removeBalance(accountName, amount);
        } catch (Exception e) {
            System.out.println("There was an issue while trying to remove funds from account: " + e.getMessage());
        }
    }

    public void transferMoney() {
        System.out.println("(Transfer Money)");
        String fromAccountName = getUserString("Enter the name of the account you want to remove funds from:");

        while (!appManager.accountExists(fromAccountName)) {
            fromAccountName = getUserString("This account doesn't exist. Enter the name of an existing account you want to remove funds from:");
        }

        double amount = getUserDouble("Enter the amount you want to transfer from that account:");
        String toAccountName = getUserString("Enter the name of the account you want to transfer the money to:");

        while (!appManager.accountExists(toAccountName)) {
            toAccountName = getUserString("This account doesn't exist. Enter the name of an existing account you want to transfer money to:");
        }

        try {
            appManager.transferMoney(fromAccountName, toAccountName, amount);

            System.out.println("Successfully transferred $" + amount + " from '" + fromAccountName + "' to '" + toAccountName + "'");
            System.out.println();
        } catch (Exception e) {
            System.out.println("There was an issue while trying to transfer money from an account: " + e.getMessage());
            System.out.println();
        }
    }

    public void inspectAccount() {
        System.out.println("(Inspect Account)");
        String accountName = getUserString("Enter the name of the account you want to inspect:");

        while (!appManager.accountExists(accountName)) {
            accountName = getUserString("This account doesn't exist. Enter the name of the account you want to inspect:");
        }

        Account account = appManager.getAccountFromName(accountName);
        System.out.println("Account name: " + account.getName());
        System.out.println("Account description: " + account.getDescription());
        System.out.println("Savings goal: " + account.getGoal());
        System.out.println("Account balance: " + account.getBalance());
        System.out.println("Account credit: " + account.getCredit());
        System.out.println("Account debt: " + account.getDebt());
    }

    public void borrowMoney() {
        System.out.println("(Borrowing Money)");
        String fromAccountName = getUserString("Enter the name of the account you want to borrow from:");

        while (!appManager.accountExists(fromAccountName)) {
            fromAccountName = getUserString("This account doesn't exist. Enter the name of an existing account you want to borrow from:");
        }

        double amount = getUserDouble("Enter the amount you want to borrow from that account:");
        String toAccountName = getUserString("Enter the name of the account you want to transfer the money to:");

        while (!appManager.accountExists(toAccountName)) {
            toAccountName = getUserString("This account doesn't exist. Enter the name of an existing account you want to transfer money to:");
        }

        try {
            appManager.borrowMoney(fromAccountName, toAccountName, amount);

            System.out.println("Successfully borrowed $'" + amount + "' from '" + fromAccountName + "' to '" + toAccountName + "'");
            System.out.println();
        } catch (Exception e) {
            System.out.println("There was an issue while trying to borrow money from an account: " + e.getMessage());
            System.out.println();
        }
    }

    public double getUserDouble(String prompt) {
        System.out.println(prompt);
        Scanner scanner = new Scanner(System.in);

        try {
            double userDouble = scanner.nextDouble();
            return roundDouble(userDouble);
        } catch (Exception e) {
            System.out.println("This is not a number or is invalid.");
            return getUserDouble(prompt);
        }
    }

    public double roundDouble(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("####0.00");
        double roundedDouble = Double.valueOf(decimalFormat.format(d));

        return roundedDouble;
    }

    public String getUserString(String prompt) {
        System.out.println(prompt + " ");
        Scanner scanner = new Scanner(System.in);
        String userString = scanner.nextLine();

        return userString;
    }

    public String removeWhiteSpace(String str) {
        return null;
    }
}
