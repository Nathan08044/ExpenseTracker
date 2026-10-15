package com.nathan.expensetracker.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Manage transaction and calculates financial total.
 */
public class TransactionManager {
    private final ArrayList<Transaction> transactions;

    /**
     * Create a manager with no transaction.
     */
    public TransactionManager() {
        this.transactions = new ArrayList<Transaction>();
    }

    /**
     * Add a transaction to the list.
     * @precondition transaction can be null
     * @param transaction the transaction to add
     * @postcondition a transaction has been added to the list
     */
    public void addTransaction(Transaction transaction){
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction can not be null.");
        }
        this.transactions.add(transaction);
    }

    /**
     * Remove a transaction.
     * @param transaction the transaction to be removed
     * @return true if the transaction is removed, false otherwise
     */
    public boolean removeTransaction(Transaction transaction){
        return this.transactions.remove(transaction);
    }

    /**
     * Return a snapshot of the transaction.
     *
     * @return return an unmodifiable copy of the transaction list
     */
    public List<Transaction> getTransactions(){
        return List.copyOf(this.transactions);
    }

    /**
     * Calculate the total income.
     * @return the total income
     */

    public BigDecimal getTotalIncome(){
        BigDecimal total = new BigDecimal("0.00");

        for (Transaction currenttransaction : this.transactions){
            BigDecimal change = currenttransaction.getBalanceChange();

            if (change.signum() > 0){
                total = total.add(change);
            }
        }
        return total;
    }

    public BigDecimal getTotalExpense(){
        BigDecimal total = new BigDecimal("0.00");
        for (Transaction currentTransaction : this.transactions){
            BigDecimal change = currentTransaction.getBalanceChange();

            if (change.signum() < 0){
                total = total.add(change.negate());
            }
        }
        return total;
    }

    /**
     * Calculate the balance.
     *
     * @return the total balance
     */
    public BigDecimal getBalance(){
        BigDecimal balance = new BigDecimal("0.00");
        for (Transaction currentTransaction : this.transactions){
            balance = balance.add(currentTransaction.getBalanceChange());
        }
        return balance;
    }
}
