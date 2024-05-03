package user.saulo.managers;

import user.saulo.Account;
import user.saulo.FinancesManagementApp;
import user.saulo.MathUtils;

import java.math.BigDecimal;
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
        addAccount(account);

        return account;
    }

    public void addAccount(Account account) {
        accounts.putIfAbsent(account.getName(), account);
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

    public void repayMoney(Account fromAccount, Account toAccount, double amount) throws Exception {
        transferMoney(fromAccount, toAccount, amount);
        removeCredit(toAccount, amount);
        removeDebt(fromAccount, amount);
    }

    public void addBalance(Account account, double amount) {
        amount = MathUtils.roundDouble(amount);
        double newBalance = MathUtils.roundDouble(new BigDecimal(Double.toString(account.getBalance())).add(new BigDecimal(Double.toString(amount))).doubleValue());

        account.setBalance(newBalance);
    }

    public void removeBalance(Account account, double amount) throws Exception {
        amount = MathUtils.roundDouble(amount);
        double balance = account.getBalance();

        if (amount > balance) {
            throw new Exception(account.getName() + " does not have enough funds");
        }

        double newBalance = MathUtils.roundDouble(new BigDecimal(Double.toString(balance)).subtract(new BigDecimal(Double.toString(amount))).doubleValue());
        account.setBalance(newBalance);
    }

    public void addDebt(Account account, double amount) {
        amount = MathUtils.roundDouble(amount);
        double newDebt = MathUtils.roundDouble(new BigDecimal(Double.toString(account.getCredit())).add(new BigDecimal(Double.toString(amount))).doubleValue());
        account.setDebt(newDebt);
    }

    public void removeDebt(Account account, double amount) throws Exception {
        amount = MathUtils.roundDouble(amount);
        double debt = account.getDebt();

        if (amount > debt) {
            throw new Exception(account.getName() + "'s debt is smaller than amount being removed ($" + amount + ")");
        }

        double newDebt = MathUtils.roundDouble(new BigDecimal(Double.toString(debt)).subtract(new BigDecimal(Double.toString(amount))).doubleValue());
        account.setDebt(newDebt);
    }

    public void addCredit(Account account, double amount) {
        amount = MathUtils.roundDouble(amount);
        double newCredit = MathUtils.roundDouble(new BigDecimal(Double.toString(account.getCredit())).add(new BigDecimal(Double.toString(amount))).doubleValue());
        account.setCredit(newCredit);
    }

    public void removeCredit(Account account, double amount) throws Exception {
        amount = MathUtils.roundDouble(amount);
        double credit = account.getCredit();

        if (amount > credit) {
            throw new Exception(account.getName() + "'s credit is smaller than amount being removed ($" + amount + ")");
        }

        double newCredit = MathUtils.roundDouble(new BigDecimal(Double.toString(credit)).subtract(new BigDecimal(Double.toString(amount))).doubleValue());
        account.setCredit(newCredit);
    }
}
