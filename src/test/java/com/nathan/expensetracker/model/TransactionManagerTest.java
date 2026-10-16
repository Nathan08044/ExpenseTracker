package com.nathan.expensetracker.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionManagerTest {
    @Test
    public void testWhenAddIncomeTheBalanceIncrease(){
        TransactionManager manager = new TransactionManager();
        Income salary = new Income("Salary", new BigDecimal("500.00"), "Work", LocalDate.of(2026, 10, 16));

        manager.addTransaction(salary);

        assertEquals(new BigDecimal("500.00"), manager.getBalance());
    }

    @Test
    public void testExpenseDecreaseBalance(){
        TransactionManager manager = new TransactionManager();
        Expense lunch = new Expense("Lunch", new BigDecimal("15.00"), "Food", LocalDate.of(2026, 10, 16));

        manager.addTransaction(lunch);

        assertEquals(new BigDecimal("-15.00"), manager.getBalance());
    }

    @Test
    public void testIncomeAndExpenseTotal() {
        TransactionManager manager = new TransactionManager();
        LocalDate date = LocalDate.of(2026, 10, 16);

        manager.addTransaction(new Income("Salary", new BigDecimal("2000.00"), "Work", date));

        manager.addTransaction(new Expense("Lunch", new BigDecimal("20.25"), "Food", date));

        manager.addTransaction(new Expense("Gas", new BigDecimal("57.00"), "Transport", date));

        assertEquals(new BigDecimal("2000.00"), manager.getTotalIncome());
        assertEquals(new BigDecimal("77.25"), manager.getTotalExpense());
        assertEquals(new BigDecimal("1922.75"), manager.getBalance());
    }

    @Test
    public void testWhenTransactionManagerStartAtZero(){
        TransactionManager manager = new TransactionManager();
        assertEquals(new BigDecimal("0.00"), manager.getTotalIncome());
        assertEquals(new BigDecimal("0.00"), manager.getTotalExpense());
        assertEquals(new BigDecimal("0.00"), manager.getBalance());

    }

}
