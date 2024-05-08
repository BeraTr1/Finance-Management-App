package user.saulo;

import user.saulo.managers.TransactionManager;

import java.util.UUID;

public class Transaction {
    private Account account;
    private Account toAccount;

    private double amount;
    private String description;
    private String notes;
    private String date;

    private UUID id;

    public Transaction(UUID uuid, Account account, Account toAccount, double amount, String description, String notes, String date) {
        this.id = uuid;
        this.account = account;
        this.toAccount = toAccount;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.notes = notes;
    }

    public UUID getId() {
        return this.id;
    }

    public Account getAccount() {
        return account;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getNotes() {
        return notes;
    }

    public String getDate() {
        return date;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Transaction transaction)) {
            return false;
        }

        return transaction.getId() == this.id;
    }
}
