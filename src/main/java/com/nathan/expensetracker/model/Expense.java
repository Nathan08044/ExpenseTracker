package com.nathan.expensetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represent an expense that decreases the balance.
 */
public class Expense extends Transaction{

    /**
     * Creates an expense transaction.
     *
     * @param title the expense title
     * @param amount the positive expense amount
     * @param category the expense category
     * @param date the expense date
     * @throws IllegalArgumentException if any value is invalid
     */
    public Expense(String title, BigDecimal amount,
                   String category, LocalDate date) {
        super(title, amount, category, date);
    }

    /**
     * Returns the amount deducted from the balance.
     *
     * @return the negative expense amount
     */
    @Override
    public BigDecimal getBalanceChange() {
        return this.getAmount().negate();
    }


}
