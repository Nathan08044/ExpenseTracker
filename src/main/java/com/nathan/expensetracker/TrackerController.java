package com.nathan.expensetracker;

import com.nathan.expensetracker.model.Expense;
import com.nathan.expensetracker.model.Income;
import com.nathan.expensetracker.model.Transaction;
import com.nathan.expensetracker.model.TransactionManager;
import com.nathan.expensetracker.storage.TransactionFileStorage;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TrackerController {
    @FXML
    private Label welcomeText;

    @FXML
    private Label incomeLabel;

    @FXML
    private Label expenseLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private TextField titleField;

    @FXML
    private TextField amountField;

    @FXML
    private TextField categoryField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button addButton;

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, String> titleColumn;

    @FXML
    private TableColumn<Transaction, BigDecimal> amountColumn;

    @FXML
    private TableColumn<Transaction, LocalDate> dateColumn;

    @FXML
    private TableColumn<Transaction, String> categoryColumn;

    @FXML
    private TableColumn<Transaction, String> typeColumn;



    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void initialize() {
        this.typeComboBox.getItems().setAll("Income", "Expense");
        this.typeComboBox.setValue("Expense");
        this.datePicker.setValue(LocalDate.now());
        this.titleColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTitle()));

        this.amountColumn.setCellValueFactory(cellData -> {
            Transaction transaction = cellData.getValue();
            BigDecimal amount = transaction.getAmount();
            return new ReadOnlyObjectWrapper<BigDecimal>(amount);
        });

        this.dateColumn.setCellValueFactory(cellData -> {
            Transaction theTransaction = cellData.getValue();
            LocalDate theDate = theTransaction.getDate();
            return new ReadOnlyObjectWrapper<LocalDate>(theDate);
        });

        this.categoryColumn.setCellValueFactory(cellData -> {
            Transaction theTransaction = cellData.getValue();
            String theCategory = theTransaction.getCategory();
            return new ReadOnlyStringWrapper(theCategory);
        });

        this.typeColumn.setCellValueFactory(cellData -> {
            Transaction theTransaction = cellData.getValue();
            String theType = theTransaction.getType();
            return new ReadOnlyStringWrapper(theType);
        });
        this.loadSavedTransactions();
        this.updateTable();
        this.updateTotal();

    }

    private void loadSavedTransactions(){
        try {
            List<Transaction> savedTransactions = this.fileStorage.loadTransactions(this.transactionFilePath);
            for (Transaction theTransaction : savedTransactions) {
                this.transactionManager.addTransaction(theTransaction);
            }
        } catch (IOException | IllegalArgumentException | DateTimeParseException theException) {
            this.loadingFailed = true;
            Alert theAlert = new Alert(Alert.AlertType.ERROR, "Could not load saved transactions: " + theException.getMessage());
            theAlert.showAndWait();
        }
    }

    private boolean savedTransactions (List<Transaction> transactions) {
        try {
            if (this.loadingFailed) {
                throw new IOException("The existing file could not be loaded.");
            }
            this.fileStorage.saveTransactions(transactions, this.transactionFilePath);
            return true;
        } catch (IOException theException) {
            Alert theAlert = new Alert(Alert.AlertType.ERROR, "Could not save the transaction: " + theException.getMessage());
            theAlert.showAndWait();
            return false;
        }
    }

    private final TransactionManager transactionManager = new TransactionManager();
    private final TransactionFileStorage fileStorage = new TransactionFileStorage();
    private final Path transactionFilePath = Paths.get(System.getProperty("user.home"), "expense-tracker-transactions.csv");
    private boolean loadingFailed = false;

    /**
     * Updates the display incomes, expense and balance.
     */
    private void updateTotal(){
        this.incomeLabel.setText("$" + this.transactionManager.getTotalIncome());

        this.expenseLabel.setText("$" + this.transactionManager.getTotalExpense());

        this.balanceLabel.setText("$" + this.transactionManager.getBalance());

    }

    @FXML
    private void handleAddTransaction() {
        try {
            String title = this.titleField.getText();
            String category = this.categoryField.getText();
            String type = this.typeComboBox.getValue();
            LocalDate date = this.datePicker.getValue();
            String amountText = this.amountField.getText();
            BigDecimal amount = new BigDecimal(amountText.trim());

            Transaction transaction;

            if ("Income".equals(type)) {
                transaction = new Income(title, amount, category, date);
            } else if ("Expense".equals(type)) {
                transaction = new Expense(title, amount, category, date);
            } else {
                throw new IllegalArgumentException("Please select Income or Expense.");
            }
            List<Transaction> transactionsToSave = new ArrayList<>(this.transactionManager.getTransactions());

            transactionsToSave.add(transaction);

            if (!this.savedTransactions(transactionsToSave)) {
                return;
            }

            this.transactionManager.addTransaction(transaction);
            this.updateTotal();
            this.updateTable();
            this.clearForm();
        } catch (NumberFormatException theException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Enter a valid amount, such as 15.50");
            alert.showAndWait();
        } catch (IllegalArgumentException theException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(theException.getMessage());
            alert.showAndWait();
        }
    }
    private void updateTable(){
        this.transactionTable.getItems().setAll(this.transactionManager.getTransactions());
    }

    /**
     * Handle a request to delete the transaction.
     */
    @FXML
    private void handleDeleteTransaction(){
        Transaction selectedTransaction = this.transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            Alert theAlert = new Alert(Alert.AlertType.WARNING);
            theAlert.setContentText("Please select a transaction to delete.");
            theAlert.showAndWait();
            return;
        }
        List<Transaction> transactionsToSave = new ArrayList<>(this.transactionManager.getTransactions());
        transactionsToSave.remove(selectedTransaction);

        if (!this.savedTransactions(transactionsToSave)) {
            return;
        }
        this.transactionManager.removeTransaction(selectedTransaction);
        this.updateTotal();
        this.updateTable();
    }

    /**
     * Clears the text fields after a successful addition.
     */
    private void clearForm() {
        this.titleField.clear();
        this.amountField.clear();
        this.categoryField.clear();
        this.titleField.requestFocus();
    }


}
