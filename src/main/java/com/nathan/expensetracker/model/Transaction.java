package com.nathan.expensetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This class will create a financial transaction with title amount, category and date.
 *
 * @author Nathan Tran
 * @version 10/12/2026
 */

public abstract class Transaction {
    private final String title;
    private final  BigDecimal amount;
    private final String category;
    private final LocalDate date;


    /**
     * Abstract method that subclass use to get the balance.
     * @return the balance change
     */
    public abstract double getBalanceChange();
    /**
     * Creates a transaction.
     * @precondition title can not be null
     * @precondition amount should be positive;
     * @precondition category can not be null
     * @precondititon date can not be null
     *
     * @param title title of the transaction
     * @param amount the amount of the transaction
     * @param category the transaction category
     * @param date date of the transaction
     * @postcondition A transaction is created
     */
    public Transaction(String title, BigDecimal amount, String category, LocalDate date) {
        if (title == null) {
            throw new IllegalArgumentException("title can not be null.");
        }
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("amount should be positive.");
        }
        if (amount.stripTrailingZeros().scale() > 2) {
          throw new IllegalArgumentException("Amount can not have two decimal places.");
        }
        if (category == null) {
            throw new IllegalArgumentException("category can not be null");
        }
        if (date == null) {
            throw new IllegalArgumentException("date can not be null");
        }
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;

    }

    /**
     * Return a transaction title.
     * @precondition none
     * @return a transaction title
     */

    public String getTitle() {
        return title;
    }

    /**
     * Return an amount that spend for a day.
     * @precondition none
     * @return an amount money that spend
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Return a category of the transaction.
     * @precondition none
     * @return a category of the transaction
     */
    public String getCategory() {
        return category;
    }

    /**
     * Return date of the transaction.
     * @precondition none
     * @return date of the transaction
     */
    public LocalDate getDate() {
        return date;
    }

}
