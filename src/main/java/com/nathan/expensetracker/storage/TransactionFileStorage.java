package com.nathan.expensetracker.storage;
import com.nathan.expensetracker.model.Expense;
import com.nathan.expensetracker.model.Income;
import com.nathan.expensetracker.model.Transaction;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Save transaction to a file and load it back when open again.
 */
public class TransactionFileStorage {

    /**
     * Save transaction to a specific type.
     *
     * @param transactions the transaction to save
     * @param filePath the destination file
     */
    public void saveTransactions (List<Transaction> transactions, Path filePath) throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("type,title,amount,category,date");

        for(Transaction currentTransaction : transactions) {
            String newLine = currentTransaction.getType() + ","
                    + this.escapeCsv(currentTransaction.getTitle()) + ","
                    + currentTransaction.getAmount().toPlainString() + ","
                    + this.escapeCsv(currentTransaction.getCategory()) + ","
                    + currentTransaction.getDate().toString();

            lines.add(newLine);
        }
        Files.write(filePath, lines, StandardCharsets.UTF_8);

    }

    /**
     * Quotes a text field and escape quotation marks for CSV.
     *
     * @param value the text to format
     * @return the quote CSV field
     */

    private String escapeCsv(String value) {
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    /**
     * Return an empty list of the transaction.
     * @param filePath the destination file
     * @return the empty list of the transaction
     */

    public List<Transaction> loadTransactions(Path filePath) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        if (Files.notExists(filePath)) {
            return transactions;
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            return transactions;
        }
        String header = lines.get(0);
        if (!header.equals("type,title,amount,category,date")) {
            throw new IOException("The CSV file has an unexpected header.");
        }

        for (int index = 1; index < lines.size(); index++) {
            String line = lines.get(index);
            List<String> values = this.parseCSVLine(line);
            if (values.size() != 5) {
                throw new IOException("Each transaction must contain exactly five fields.");
            }
            String type = values.get(0);
            String title = values.get(1);
            BigDecimal amount = new BigDecimal(values.get(2));
            String category = values.get(3);
            LocalDate date = LocalDate.parse(values.get(4));

            Transaction theChoosenTransaction;
            if ("Income".equals(type)) {
                theChoosenTransaction = new Income(title,amount,category,date);
            } else if ("Expense".equals(type)) {
                theChoosenTransaction = new Expense(title,amount,category,date);
            } else {
                throw new IOException("Unknown transaction type" + type);
            }
            transactions.add(theChoosenTransaction);

        }

        return transactions;
    }

    private List<String> parseCSVLine(String line) {
        List<String> values = new ArrayList<>();

        StringBuilder currentValue = new StringBuilder();
        boolean insideQuotes = false;

        for (int index = 0; index < line.length(); index++) {
            char currentCharacter = line.charAt(index);

            if (currentCharacter == '"') {
                if (insideQuotes && index + 1 < line.length() && line.charAt(index + 1) == '"') {
                    currentValue.append('"');
                    index++;
                } else {
                insideQuotes = !insideQuotes;}
            } else if (currentCharacter == ',' && !insideQuotes) {
                values.add(currentValue.toString());
                currentValue.setLength(0);
            } else {
                currentValue.append(currentCharacter);
            }

        }
        values.add(currentValue.toString());

        return values;
    }

}
