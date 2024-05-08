package user.saulo.managers;

import user.saulo.Account;
import user.saulo.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TransactionManager {
    private final Map<UUID, Transaction> transactionsMap = new HashMap<>();

    public Transaction createTransaction(Account account, Account toAccount, double amount, String description, String notes, String date) {
        UUID transactionUUID = UUID.randomUUID();

        while (this.transactionsMap.containsKey(transactionUUID)) {
            transactionUUID = UUID.randomUUID();
        }

        Transaction transaction = new Transaction(transactionUUID, account, toAccount, amount, description, notes, date);
        addTransaction(transaction);

        return transaction;
    }

    public void addTransaction(Transaction transaction) {
        this.transactionsMap.putIfAbsent(transaction.getId(), transaction);
    }
}
