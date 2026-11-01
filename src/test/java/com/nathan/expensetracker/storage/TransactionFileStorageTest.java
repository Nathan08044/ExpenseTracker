package com.nathan.expensetracker.storage;
import com.nathan.expensetracker.model.Expense;
import com.nathan.expensetracker.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionFileStorageTest {

    @TempDir
    Path temporaryFolder;

    @Test
    public void testSaveTransaction() throws IOException {
        TransactionFileStorage theStorage = new TransactionFileStorage();

        Transaction lunch = new Expense("Lunch", new BigDecimal("15.50"), "Food", LocalDate.of(2026, 10, 31));

        Path filePath = temporaryFolder.resolve("transactions.csv");

        theStorage.saveTransactions(List.of(lunch), filePath);

        List<String> savedLines = Files.readAllLines(filePath);

        assertEquals(List.of("type, title, amount, category, date", "Expense,\"Lunch\",15.50,\"Food\",2026-10-31"), savedLines);

    }

    @Test
    public void testSaveAndLoadTransaction() throws IOException {
        TransactionFileStorage theStorage = new TransactionFileStorage();

        Transaction lunch = new Expense("Lunch", new BigDecimal("15.50"),"Food",LocalDate.of(2026, 10,31));
        Path filePath = temporaryFolder.resolve("transactions.csv");
        theStorage.saveTransactions(List.of(lunch), filePath);
        List<Transaction> loadedTransactions = theStorage.loadTransactions(filePath);

        assertEquals(1, loadedTransactions.size());

        Transaction loadedCurrentTransaction = loadedTransactions.get(0);
        assertEquals("Expense", loadedCurrentTransaction.getType());
        assertEquals("Lunch", loadedCurrentTransaction.getTitle());
        assertEquals(new BigDecimal("15.50"), loadedCurrentTransaction.getAmount());
        assertEquals("Food", loadedCurrentTransaction.getCategory());
        assertEquals(LocalDate.of(2026, 10, 31), loadedCurrentTransaction.getDate());

    }

    @Test
    public void testLoadWhenFileDoesNotExist() throws IOException {
        TransactionFileStorage theStorage = new TransactionFileStorage();

        Path filePath = temporaryFolder.resolve("missing.csv");
        List<Transaction> loadedTransactions = theStorage.loadTransactions(filePath);

        assertEquals(0, loadedTransactions.size());

    }
}
