package com.nathan.expensetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represent the income that increase the balance
 */
public class Income extends Transaction {

    /**
     * Creates an income transaction.
     *
     * @param title the income title
     * @param amount the positive income amount
     * @param category the income category
     * @param date the income date
     * @throws IllegalArgumentException if any value is invalid
     */
    public Income(String title, BigDecimal amount,
                  String category, LocalDate date) {
        super(title, amount, category, date);
    }

    /**
     * Returns the amount added to the balance.
     *
     * @return the positive income amount
     */
    @Override
    public BigDecimal getBalanceChange() {
        return this.getAmount();
    }

    /**
     * Return type of category.
     */
    @Override
    public String getType(){
        return "Income";
    }
}


