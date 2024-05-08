package user.saulo.managers;

import user.saulo.Account;
import user.saulo.FinancesManagementApp;
import user.saulo.MathUtils;
import user.saulo.Transaction;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class AccountManager {
    private final Map<String, Account> accounts = new HashMap<>();

    private final TransactionManager transactionManager = FinancesManagementApp.transactionManager;

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
            throw new Exception("Account doesn't exist");
        }

        accounts.remove(accountName);
    }

    public Account getAccountFromName(String accountName) {
        return this.accounts.getOrDefault(accountName, null);
    }

    public void depositMoney(Account account, double amount, String notes) {
        addBalance(account, amount);
        String transactionDescription = "Deposited balance";
        addTransaction(account, amount, transactionDescription, notes);
    }

    public void withdrawMoney(Account account, double amount, String note) throws Exception {
        removeBalance(account, amount);
        String transactionDescription = "Withdrew balance";
        addTransaction(account, -amount, transactionDescription, note);
    }

    public void transferMoney(Account fromAccount, Account toAccount, double amount, String notes) throws Exception {
        removeBalance(fromAccount, amount);
        addBalance(toAccount, amount);
        String transactionDescriptionToAccount = "Transferred balance to '" + toAccount.getName() + "'";
        addTransaction(fromAccount, toAccount, -amount, transactionDescriptionToAccount, notes);
        String transactionDescriptionFromAccount = "Transferred balance from '" + toAccount.getName() + "'";
        addTransaction(toAccount, fromAccount, amount, transactionDescriptionFromAccount, notes);
    }

    public void borrowMoney(Account fromAccount, Account toAccount, double amount, String notes) throws Exception {
        removeBalance(fromAccount, amount);
        addBalance(toAccount, amount);
        addCredit(fromAccount, amount);
        addDebt(toAccount, amount);
        String transactionDescriptionToAccount = "Borrowed money from '" + fromAccount.getName() + "'";
        addTransaction(toAccount, fromAccount, amount, transactionDescriptionToAccount, notes);
        String transactionDescriptionFromAccount = "Lent money to '" + fromAccount.getName() + "'";
        addTransaction(fromAccount, toAccount, -amount, transactionDescriptionFromAccount, notes);
    }

    public void repayMoney(Account fromAccount, Account toAccount, double amount, String notes) throws Exception {
        removeBalance(fromAccount, amount);
        addBalance(toAccount, amount);
        removeCredit(toAccount, amount);
        removeDebt(fromAccount, amount);
        String transactionDescriptionToAccount = "Repaid debt to '" + toAccount.getName() + "'";
        addTransaction(toAccount, fromAccount, amount, transactionDescriptionToAccount, notes);
        String transactionDescriptionFromAccount = "Repaid debt from '" + toAccount.getName() + "'";
        addTransaction(fromAccount, toAccount, -amount, transactionDescriptionFromAccount, notes);
    }

    private void addBalance(Account account, double amount) {
        amount = MathUtils.roundDouble(amount);
        double newBalance = MathUtils.roundDouble(new BigDecimal(Double.toString(account.getBalance())).add(new BigDecimal(Double.toString(amount))).doubleValue());

        account.setBalance(newBalance);
    }

    private void removeBalance(Account account, double amount) throws Exception {
        amount = MathUtils.roundDouble(amount);
        double balance = account.getBalance();

        if (amount > balance) {
            throw new Exception(account.getName() + " does not have enough funds");
        }

        double newBalance = MathUtils.roundDouble(new BigDecimal(Double.toString(balance)).subtract(new BigDecimal(Double.toString(amount))).doubleValue());
        account.setBalance(newBalance);
    }

    private void addDebt(Account account, double amount) {
        amount = MathUtils.roundDouble(amount);
        double newDebt = MathUtils.roundDouble(new BigDecimal(Double.toString(account.getDebt())).add(new BigDecimal(Double.toString(amount))).doubleValue());
        System.out.println("New debt is: " + newDebt);
        account.setDebt(newDebt);
    }

    private void removeDebt(Account account, double amount) throws Exception {
        amount = MathUtils.roundDouble(amount);
        double debt = account.getDebt();

        if (amount > debt) {
            throw new Exception(account.getName() + "'s debt is smaller than amount being removed ($" + amount + ")");
        }

        double newDebt = MathUtils.roundDouble(new BigDecimal(Double.toString(debt)).subtract(new BigDecimal(Double.toString(amount))).doubleValue());
        account.setDebt(newDebt);
    }

    private void addCredit(Account account, double amount) {
        amount = MathUtils.roundDouble(amount);
        double newCredit = MathUtils.roundDouble(new BigDecimal(Double.toString(account.getCredit())).add(new BigDecimal(Double.toString(amount))).doubleValue());
        account.setCredit(newCredit);
    }

    private void removeCredit(Account account, double amount) throws Exception {
        amount = MathUtils.roundDouble(amount);
        double credit = account.getCredit();

        if (amount > credit) {
            throw new Exception(account.getName() + "'s credit is smaller than amount being removed ($" + amount + ")");
        }

        double newCredit = MathUtils.roundDouble(new BigDecimal(Double.toString(credit)).subtract(new BigDecimal(Double.toString(amount))).doubleValue());
        account.setCredit(newCredit);
    }

    public boolean accountExists(Account account) {
        if (account == null) {
            return false;
        }

        return accountExists(account.getName());
    }

    public boolean accountExists(String accountName) {
        return this.accounts.containsKey(accountName);
    }

    public void addTransaction(Account account, Transaction transaction) {
        account.addTransaction(transaction);
    }

    public void addTransaction(Account account, double amount, String description, String notes) {
        addTransaction(account, null, amount, description, notes);
    }

    public void addTransaction(Account account, Account toAccount, double amount, String description, String notes) {
        String date = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
        Transaction transaction = transactionManager.createTransaction(account, toAccount, amount, description, notes, date);
        account.addTransaction(transaction);
    }
}
