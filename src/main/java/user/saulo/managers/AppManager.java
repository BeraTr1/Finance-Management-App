package user.saulo.managers;

import user.saulo.Account;
import user.saulo.FinancesManagementApp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* Responsible for things like creating/deleting accounts, etc. (things that a UI or a 'command' can interact with)
*
* */
public class AppManager {
    private final AccountManager accountManager;

    public AppManager() {
        this.accountManager = FinancesManagementApp.accountManager;
    }

    public List<Account> getAccounts() {
        return accountManager.getAccounts();
    }

    public Account getAccountFromName(String accountName) {
        return accountManager.getAccountFromName(accountName);
    }

    public void createAccount(String accountName, String accountDescription) throws Exception {
        Account account = accountManager.createAccount(accountName, accountDescription);
    }

    public void deleteAccount(String accountName) throws Exception {
        accountManager.deleteAccount(accountName);
    }

    public void borrowMoney(String fromAccountName, String toAccountName, double amount) throws Exception {
        Account fromAccount = accountManager.getAccountFromName(fromAccountName);

        if (fromAccount == null) {
            throw new Exception("Account with name '" + fromAccountName + "' doesn't exist");
        }

        Account toAccount = accountManager.getAccountFromName(toAccountName);

        if (toAccount == null) {
            throw new Exception("Account with name '" + toAccountName + "' doesn't exist");
        }

        accountManager.borrowMoney(fromAccount, toAccount, amount);
    }

    public void addBalance(String accountName, double amount) throws Exception {
        Account account = accountManager.getAccountFromName(accountName);

        if (account == null) {
            throw new Exception("Account with name '" + accountName + "' doesn't exist");
        }

        accountManager.addBalance(account, amount);
    }

    public void removeBalance(String accountName, double amount) throws Exception {
        Account account = accountManager.getAccountFromName(accountName);

        if (account == null) {
            throw new Exception("Account with name '" + accountName + "' doesn't exist");
        }

        accountManager.removeBalance(account, amount);
    }

    public void transferMoney(String fromAccountName, String toAccountName, double amount) throws Exception {
        Account fromAccount = accountManager.getAccountFromName(fromAccountName);

        if (fromAccount == null) {
            throw new Exception("Account with name '" + fromAccountName + "' doesn't exist");
        }

        Account toAccount = accountManager.getAccountFromName(toAccountName);

        if (toAccount == null) {
            throw new Exception("Account with name '" + toAccountName + "' doesn't exist");
        }

        accountManager.transferMoney(fromAccount, toAccount, amount);
    }

    public boolean accountExists(String accountName) {
        Account account = accountManager.getAccountFromName(accountName);

        return account != null;
    }

    public void test() {
        System.out.println("App Manager is loaded!");
    }
}
