package user.saulo.managers;

import user.saulo.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountManager {
    private final Map<String, Account> accounts = new HashMap<>();

    public Account createAccount(String accountName, String accountDescription) throws Exception {
        if (accounts.containsKey(accountName)) {
            throw new Exception("Account with name '" + accountName + "' already exists!");
        }

        Account account = new Account(accountName, accountDescription);
        accounts.putIfAbsent(accountName, account);

        return account;
    }

    public List<Account> getAccounts() {
        List<Account> accountsList = new ArrayList<>();
        accountsList.addAll(this.accounts.values());

        return accountsList;
    }

    public void deleteAccount(String accountName) throws Exception {
        boolean accountExists = accounts.containsKey(accountName);

        if (!accountExists) {
            throw new Exception("Account already exists");
        }

        accounts.remove(accountName);
    }

    public Account getAccountFromName(String accountName) {
        return this.accounts.getOrDefault(accountName, null);
    }

    public void transferMoney(Account fromAccount, Account toAccount, double amount) throws Exception {
        removeBalance(fromAccount, amount);
        addBalance(toAccount, amount);
    }

    public void borrowMoney(Account fromAccount, Account toAccount, double amount) throws Exception {
        transferMoney(fromAccount, toAccount, amount);
        addCredit(fromAccount, amount);
        addDebt(toAccount, amount);
    }

    public void addBalance(Account account, double amount) {
        double newBalance = account.getBalance() + amount;

        account.setBalance(newBalance);
    }

    public void removeBalance(Account account, double amount) throws Exception {
        double balance = account.getBalance();

        if (amount > balance) {
            throw new Exception(account.getName() + " does not have enough funds");
        }

        double newBalance = balance - amount;
        account.setBalance(newBalance);
    }

    public void addDebt(Account account, double amount) {
        double newDebt = account.getDebt() + amount;
        account.setDebt(newDebt);
    }

    public void removeDebt(Account account, double amount) throws Exception {
        double debt = account.getDebt();

        if (amount > debt) {
            throw new Exception(account.getName() + "'s debt is smaller than amount being removed ($" + amount + ")");
        }

        double newDebt = debt - amount;
        account.setDebt(newDebt);
    }

    public void addCredit(Account account, double amount) {
        double newCredit = account.getCredit() + amount;
        account.setCredit(newCredit);
    }

    public void removeCredit(Account account, double amount) throws Exception {
        double credit = account.getCredit();

        if (amount > credit) {
            throw new Exception(account.getName() + "'s credit is smaller than amount being removed ($" + amount + ")");
        }

        double newCredit = credit - amount;
        account.setCredit(newCredit);
    }
}
