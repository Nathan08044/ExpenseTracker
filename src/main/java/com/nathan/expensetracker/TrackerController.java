package com.nathan.expensetracker;

import com.nathan.expensetracker.model.Expense;
import com.nathan.expensetracker.model.Income;
import com.nathan.expensetracker.model.Transaction;
import com.nathan.expensetracker.model.TransactionManager;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void initialize() {
        this.typeComboBox.getItems().setAll("Income", "Expense");
        this.typeComboBox.setValue("Expense");
        this.datePicker.setValue(LocalDate.now());
        this.updateTotal();
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

    }

    private final TransactionManager transactionManager = new TransactionManager();

    /**
     * Updates the display incomes, expense and balance.
     */
    private void updateTotal(){
        this.incomeLabel.setText("Income: $" + this.transactionManager.getTotalIncome());

        this.expenseLabel.setText("Expense: $" + this.transactionManager.getTotalExpense());

        this.balanceLabel.setText("Balance: $" + this.transactionManager.getBalance());

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
