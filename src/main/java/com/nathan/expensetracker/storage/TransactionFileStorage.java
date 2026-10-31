package com.nathan.expensetracker.storage;
import com.nathan.expensetracker.model.Transaction;
import java.nio.file.Path;
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
        lines.add("type, title, amount, category, date");

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

}
