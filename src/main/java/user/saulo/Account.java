package user.saulo;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String name;
    private String description;
    private double balance;
    private double debt;
    private double credit;
    private double goal;
    private List<Transaction> transactions = new ArrayList<>();

    public Account(String name, String description, double balance, double credit, double debt, double goal) {
        this.name = name;
        this.description = description;
        this.debt = debt;
        this.balance = balance;
        this.credit = credit;
        this.goal = goal;
    }

    public Account(String name, String description) {
        this.name = name;
        this.description = description;
        this.debt = 0;
        this.balance = 0;
        this.credit = 0;
        this.goal = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

    public void addTransaction(Transaction transaction) {
        if (this.transactions.contains(transaction)) {
            return;
        }

        this.transactions.add(0, transaction);
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }
}
