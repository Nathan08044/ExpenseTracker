package com.nathan.expensetracker;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class HelloController {
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
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void initialize() {
        this.typeComboBox.getItems().setAll("Income", "Expense");
        this.typeComboBox.setValue("Expense");
        this.datePicker.setValue(LocalDate.now());

    }
}
