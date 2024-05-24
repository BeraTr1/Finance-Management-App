package user.saulo.application;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import user.saulo.*;
import user.saulo.managers.AccountManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Dashboard {
    private String title = "Dashboard";
    private Scene scene;
    private BorderPane pane;

    private AccountManager accountManager;

    private App app;

    public Dashboard() {
        this.app = App.instance;
        this.accountManager = FinancesManagementApp.accountManager;
        BorderPane gridPane = getDashboardPane();
        this.pane = gridPane;
        this.scene = new Scene(gridPane);
        this.load();
    }

    private void load() {
        loadDashboardTop();
        loadDashboardLeft();
        loadDashboardRight();
        loadDashboardCenter();
    }

    private BorderPane getDashboardPane() {
        BorderPane gridPane = new BorderPane();

        return gridPane;
    }

    private void loadDashboardTop() {
        HBox background = new HBox();
        background.setPrefHeight(50);
        background.setStyle("-fx-background-color: #303030");

        this.pane.setTop(background);
    }

    private void loadDashboardCenter() {
        HBox background = new HBox();
        background.setStyle("-fx-background-color: #F4E8E0;");
        background.setPrefSize(2000, 1200);
        background.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // load account cards
        List<Account> accounts = accountManager.getAccounts();
        int maxRow = 10;
        int maxCol = 5;
        final double accountCardWidth = 250;
        final double accountCardHeight = 350;

        int lastRow = 0;
        int lastCol = 0;

        for (int row = 1; row <= maxRow; row++) {
            for (int col = 1; col <= maxCol; col++) {
                if (accounts.stream().findFirst().isEmpty()) {
                    break;
                }

                Account account = accounts.stream().findFirst().get();

                HBox accountCard = new HBox();
                accountCard.setStyle("-fx-background-color: #484848; -fx-background-radius: 15 15 15 15; -fx-border-radius: 15 15 15 15;");
                accountCard.setMinSize(accountCardWidth, accountCardHeight);
                accountCard.setOnMouseClicked(event -> {
                    inspectAccount(account);
                });
                accountCard.setAlignment(Pos.CENTER);
                accountCard.getStyleClass().add("account-card");

                GridPane gridPane = new GridPane();
                accountCard.getChildren().add(gridPane);
                gridPane.setHgap(5);
                gridPane.setVgap(5);
//                gridPane.setGridLinesVisible(true); todo remove
                gridPane.setMinSize(accountCardWidth * 0.95, accountCardHeight * 0.95);
                gridPane.setMaxSize(accountCardWidth * 0.95, accountCardHeight * 0.95);
                gridPane.setAlignment(Pos.CENTER);

                ColumnConstraints column1 = new ColumnConstraints();
                column1.setPercentWidth(25);
                ColumnConstraints column2 = new ColumnConstraints();
                column2.setPercentWidth(25);
                ColumnConstraints column3 = new ColumnConstraints();
                column3.setPercentWidth(25);
                ColumnConstraints column4 = new ColumnConstraints();
                column4.setPercentWidth(25);

                gridPane.getColumnConstraints().addAll(column1, column2, column3, column4);

                RowConstraints row1 = new RowConstraints();
                row1.setPercentHeight(25);
                RowConstraints row2 = new RowConstraints();
                row2.setPercentHeight(25);
                RowConstraints row3 = new RowConstraints();
                row3.setPercentHeight(10);
                RowConstraints row4 = new RowConstraints();
                row4.setPercentHeight(10);
                RowConstraints row5 = new RowConstraints();
                row5.setPercentHeight(15);
                RowConstraints row6 = new RowConstraints();
                row6.setPercentHeight(15);

                gridPane.getRowConstraints().addAll(row1, row2, row3, row4, row5);

                double accountBalance = account.getBalance();
                double accountCredit = account.getCredit();
                double accountDebt = account.getDebt();
                double totalMoney = (accountBalance + accountCredit + accountDebt) == 0 ? 1 : (accountBalance + accountCredit + accountDebt);

                double balancePercentage = MathUtils.roundDouble(accountBalance / totalMoney);
                double arcBalanceAngleLength = MathUtils.roundDouble(balancePercentage * 270);
                Color arcBalanceColor = Color.web("#3FDA2A");
//
                double creditPercentage = MathUtils.roundDouble(accountCredit / totalMoney);
                double arcCreditAngleLength = MathUtils.roundDouble(creditPercentage * 270 + arcBalanceAngleLength);
                Color arcCreditColor = Color.web("#97FC5E");

                double debtPercentage = MathUtils.roundDouble(accountDebt / totalMoney);
                double arcDebtAngleLenght = MathUtils.roundDouble(debtPercentage * 270 + arcCreditAngleLength);
                Color arcDebtColor = Color.web("#EE3024");

                Section graySection = new Section(0, totalMoney);
                graySection.setColor(accountDebt + accountBalance + accountCredit == 0 ? Color.GRAY : arcBalanceColor);
                Section creditSection = new Section(0, accountCredit);
                creditSection.setColor(arcCreditColor);
                creditSection.setTextColor(arcCreditColor);
                creditSection.setText("Credit");
                Section balanceSection = new Section(accountCredit, accountCredit + accountBalance);
                balanceSection.setColor(arcBalanceColor);
                balanceSection.setTextColor(arcBalanceColor);
                balanceSection.setText("Balance");
                Section debtSection = new Section(accountBalance + accountCredit, accountBalance + accountCredit + accountDebt);
                debtSection.setColor(arcDebtColor);
                debtSection.setTextColor(arcDebtColor);
                debtSection.setText("Debt");

                Gauge gauge = GaugeBuilder.create().skinType(Gauge.SkinType.SIMPLE_SECTION).sectionsVisible(true).sectionTextVisible(true).sections(graySection, balanceSection, creditSection, debtSection).maxValue(totalMoney).value((accountBalance + accountCredit)).barBackgroundColor(accountDebt + accountBalance + accountCredit == 0 ? Color.GRAY : arcDebtColor).barColor(Color.WHITE).valueVisible(false).build();
                gauge.setOnMouseClicked(mouseEvent -> {
                    inspectAccount(account);
                });
                gridPane.add(gauge, 0, 0, 4, 2);

                Text accountText = new Text();
                accountText.setText("$" + accountBalance);
                accountText.setFill(arcBalanceColor);
                accountText.setStyle("-fx-font-size: 20");
                GridPane.setHalignment(accountText, HPos.CENTER);
                GridPane.setValignment(accountText, VPos.BOTTOM);
                gridPane.add(accountText, 0, 0, 4, 1);

                Text accountCreditText = new Text();
                accountCreditText.setText("$" + accountCredit);
                accountCreditText.setFill(arcCreditColor);
                accountCreditText.setStyle("-fx-font-size: 10");
                GridPane.setHalignment(accountCreditText, HPos.RIGHT);
                GridPane.setValignment(accountCreditText, VPos.TOP);
                gridPane.add(accountCreditText, 1, 1, 1, 1);

                Text accountDebtText = new Text();
                accountDebtText.setText("$" + accountDebt);
                accountDebtText.setFill(arcDebtColor);
                accountDebtText.setStyle("-fx-font-size: 10");
                GridPane.setHalignment(accountDebtText, HPos.LEFT);
                GridPane.setValignment(accountDebtText, VPos.TOP);
                gridPane.add(accountDebtText, 2, 1, 1, 1);

                HBox accountCardLine = new HBox();
                accountCardLine.setStyle("-fx-background-color: #848484");
                accountCardLine.setMinSize(accountCardWidth * 0.95, 1);
                accountCardLine.setMaxSize(accountCardWidth * 0.95, 1);
                gridPane.add(accountCardLine, 0, 2, 4, 1);

                Text accountName = new Text(account.getName());
                gridPane.add(accountName, 0, 3, 4, 1);
                GridPane.setHalignment(accountName, HPos.CENTER);
                accountName.setFill(Color.web("939393"));
                accountName.setStyle("-fx-font-size: 25;");

                Button depositButton = new Button();
                depositButton.setText("Deposit Money");
                depositButton.setMaxWidth(Double.MAX_VALUE);
                depositButton.setStyle("-fx-background-color: #939393;");
                depositButton.setTextFill(Color.web("484848"));
                depositButton.setOnAction(action -> {
                    depositMoney(account);
                });
                gridPane.add(depositButton, 0, 4, 2, 1);

                Button withdrawButton = new Button();
                withdrawButton.setText("Withdraw Money");
                withdrawButton.setMaxWidth(Double.MAX_VALUE);
                withdrawButton.setStyle("-fx-background-color: #939393;");
                withdrawButton.setTextFill(Color.web("484848"));
                withdrawButton.setOnAction(action -> {
                    withdrawMoney(account);
                });
                gridPane.add(withdrawButton, 2, 4, 2, 1);

                Button transferButton = new Button();
                transferButton.setText("Transfer Money");
                transferButton.setMaxWidth(Double.MAX_VALUE);
                transferButton.setStyle("-fx-background-color: #939393;");
                transferButton.setTextFill(Color.web("484848"));
                transferButton.setOnAction(action -> {
                    transferMoney(account);
                });
                GridPane.setHalignment(transferButton, HPos.CENTER);
                gridPane.add(transferButton, 0, 5, 1, 1);

                Button borrowButton = new Button();
                borrowButton.setText("Borrow Money");
                borrowButton.setMaxWidth(Double.MAX_VALUE);
                borrowButton.setStyle("-fx-background-color: #939393;");
                borrowButton.setTextFill(Color.web("484848"));
                borrowButton.setOnAction(action -> {
                    borrowMoney(account);
                });
                GridPane.setHalignment(borrowButton, HPos.CENTER);
                gridPane.add(borrowButton, 1, 5, 1, 1);

                Button repayDebtButton = new Button();
                repayDebtButton.setText("Repay Debt");
                repayDebtButton.setMaxWidth(Double.MAX_VALUE);
                repayDebtButton.setStyle("-fx-background-color: #939393;");
                repayDebtButton.setTextFill(Color.web("484848"));
                repayDebtButton.setOnAction(action -> {
                    repayDebt(account);
                });
                GridPane.setHalignment(repayDebtButton, HPos.CENTER);
                gridPane.add(repayDebtButton, 2, 5, 1, 1);

                grid.add(accountCard, col, row);
                lastRow = row;
                lastCol = col;
                accounts.remove(account);
            }
        }

        // create account card
        HBox createAccountCard = new HBox();
        createAccountCard.setStyle("-fx-background-color: rgba(147, 147, 147, 0.25); -fx-border-width: 5; -fx-border-color: #939393; -fx-background-radius: 15 15 15 15; -fx-border-radius: 15 15 15 15"); // FCF1EA
        createAccountCard.setMinSize(accountCardWidth, accountCardHeight);
        createAccountCard.setAlignment(Pos.CENTER);
        createAccountCard.setOnMouseClicked(mouseEvent -> {
            createAccountDialog();
        });

        int createAccountCol = lastCol == maxCol ? 1 : lastCol + 1;
        int createAccountRow = lastRow + (lastCol == maxCol ? 1 : 0);

        Text plusText = new Text();
        plusText.setText("+");
        plusText.setFill(Color.GRAY);
        plusText.setStyle("-fx-font-size: 100;");

        createAccountCard.getChildren().add(plusText);
        grid.add(createAccountCard, createAccountCol, createAccountRow, 1, 1);

        // todo make accounts scrollable

        background.getChildren().add(grid);

        this.pane.setCenter(background);
    }

    private void loadDashboardLeft() {
        HBox background = new HBox();
        background.setStyle("-fx-background-color: #484848");
        background.setAlignment(Pos.CENTER);
        background.setPrefWidth(300);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        background.getChildren().add(grid);
        this.pane.setLeft(background);
    }

    private void loadDashboardRight() {
        HBox background = new HBox();
        background.setStyle("-fx-background-color: #484848");
        background.setAlignment(Pos.CENTER);
        background.setPrefWidth(300);
        this.pane.setRight(background);
    }

    private void inspectAccount(Account account) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setPrefSize(600, 700); // 800, 600
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
//        gridPane.setGridLinesVisible(false);
//        gridPane.setPrefSize(dialog.getHeight(), dialog.getWidth());
        gridPane.setAlignment(Pos.CENTER);

        HBox accountVisual = new HBox();
        accountVisual.setPrefSize(200, 200);
        accountVisual.setStyle("-fx-background-color: #37B432");
        gridPane.add(accountVisual, 0, 0, 6, 2);

        HBox accountDetails = new HBox();
        accountDetails.setPrefSize(200, 50);
        accountDetails.setStyle("-fx-background-color: #AFC915");
        gridPane.add(accountDetails, 0, 2, 6, 1);

        Button depositMoneyButton = new Button();
        depositMoneyButton.setText("Deposit Money");
        depositMoneyButton.setOnAction(event -> {
            depositMoney(account);
        });
        gridPane.add(depositMoneyButton, 0, 3, 1, 1);

        Button withdrawMoneyButton = new Button();
        withdrawMoneyButton.setText("Withdraw Money");
        withdrawMoneyButton.setOnAction(event -> {
            withdrawMoney(account);
        });
        gridPane.add(withdrawMoneyButton, 1, 3, 1, 1);

        Button transferMoneyButton = new Button();
        transferMoneyButton.setText("Transfer Money");
        transferMoneyButton.setOnAction(event -> {
            withdrawMoney(account);
        });
        gridPane.add(transferMoneyButton, 2, 3, 1, 1);

        Button borrowMoneyButton = new Button();
        borrowMoneyButton.setText("Borrow Money");
        borrowMoneyButton.setOnAction(event -> {
            borrowMoney(account);
        });
        gridPane.add(borrowMoneyButton, 3, 3, 1, 1);

        Button editAccountButton = new Button();
        editAccountButton.setText("Edit Account");
        editAccountButton.setOnAction(event -> {
            editAccount(account);
        });
        gridPane.add(editAccountButton, 4, 3, 1, 1);

        Button deleteAccountButton = new Button();
        deleteAccountButton.setText("Delete Account");
        deleteAccountButton.setOnAction(event -> {
            deleteAccount(account);

            if (!accountManager.accountExists(account.getName())) {
                dialog.close();
            }
        });
        gridPane.add(deleteAccountButton, 5, 3, 1, 1);

        List<Transaction> recentTransactions = sortRecentTransactionsByDate(account.getTransactions());
        final int maxTransactionsListed = 5;
        int currentTransactionsListed = 0;

        for (Transaction transaction : recentTransactions) {
            if (currentTransactionsListed >= maxTransactionsListed) {
                break;
            }

            GridPane recentTransactionsGrid = new GridPane();
//            recentTransactionsGrid.setGridLinesVisible(true);
            recentTransactionsGrid.setHgap(5);
            recentTransactionsGrid.setPadding(new Insets(5, 5, 5, 5));
            recentTransactionsGrid.setPrefWidth(Double.MAX_VALUE);

            ColumnConstraints col1 = new ColumnConstraints();
            col1.setFillWidth(true);
            col1.setPercentWidth(15);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setFillWidth(true);
            col2.setPercentWidth(70);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setFillWidth(true);
            col3.setPercentWidth(15);

            recentTransactionsGrid.getColumnConstraints().addAll(col1, col2, col3);

            RowConstraints row1 = new RowConstraints();
            row1.setPercentHeight(50);
            RowConstraints row2 = new RowConstraints();
            row2.setPercentHeight(50);

            recentTransactionsGrid.getRowConstraints().addAll(row1, row2);

            Text dateText = new Text();
            dateText.setText(transaction.getDate());
            recentTransactionsGrid.add(dateText, 0, 0, 1, 1);

            Text descriptionText = new Text();
            descriptionText.setText(transaction.getDescription());
            recentTransactionsGrid.add(descriptionText, 1, 0, 1, 1);

            Text amountText = new Text();
            Color amountTextColor = transaction.getAmount() >= 0 ? Color.web("#2ED323") : Color.web("#D52B2B");
            amountText.setFill(amountTextColor);
            String amountString = transaction.getAmount() >= 0 ? "+" + transaction.getAmount() : Double.toString(transaction.getAmount());
            amountText.setText(amountString);
            recentTransactionsGrid.add(amountText, 2, 0, 1, 1);

            Text notesText = new Text();
            notesText.setText(transaction.getNotes());
            notesText.minWidth(Double.MAX_VALUE);
            notesText.maxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(notesText, Priority.ALWAYS);
            recentTransactionsGrid.add(notesText, 0, 1, 3, 1);

            currentTransactionsListed += 1;
            int recentTransactionsRow = currentTransactionsListed + 3;
            gridPane.add(recentTransactionsGrid, 0, recentTransactionsRow, 6, 1);
            GridPane.setHalignment(recentTransactionsGrid, HPos.CENTER);

//            HBox tes = new HBox();
//            tes.setStyle("-fx-background-color: purple");
//            tes.setPrefSize(10, 5);
//            gridPane.add(tes, 0, recentTransactionsRow, 6, 1);

//            tes.getChildren().add(recentTransactionsGrid);
        }

        dialog.getDialogPane().setContent(gridPane);

        dialog.showAndWait();
    }

    private void createAccountDialog() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextField accountNameTextField = new TextField();
        TextField accountDescriptionTextField = new TextField();

        accountNameTextField.setPromptText("Give a name to this account");
        accountDescriptionTextField.setPromptText("Describe what this account is for");

        gridPane.add(accountNameTextField, 1, 1);
        gridPane.add(accountDescriptionTextField, 1, 2);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(gridPane);
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                String accountName = accountNameTextField.getText();
                String accountDescription = accountDescriptionTextField.getText();
                accountManager.createAccount(accountName, accountDescription);
                loadDashboardCenter();
            } catch (Exception e){
                event.consume();
                app.displayError(e.getMessage());
            }
        });

        dialog.showAndWait();
    }

    private void depositMoney(Account account) {
        GridPane gridPane = new GridPane();

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100);

        gridPane.getRowConstraints().addAll(row1, row2);
        gridPane.getColumnConstraints().addAll(col1);

        TextField amountInput = new TextField();
        amountInput.setPromptText("Enter the amount being deposited");
        amountInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*") || !newValue.matches("\\.")) {
                    amountInput.setText(newValue.replaceAll("[^\\d\\.]", ""));
                }
            }
        });
        gridPane.add(amountInput, 0, 0, 1, 1);

        TextField description = new TextField();
        description.setPromptText("Enter a description");
        gridPane.add(description, 0, 1, 1, 1);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(gridPane);
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                double amount = InputUtils.getDoubleFromString(amountInput.getText());
                accountManager.depositMoney(account, amount, description.getText());
                loadDashboardCenter();
            } catch (Exception e){
                event.consume();
                app.displayError(e.getMessage());
            }
        });

        dialog.showAndWait();
    }

    private void withdrawMoney(Account account) {
        GridPane gridPane = new GridPane();
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100);

        gridPane.getRowConstraints().addAll(row1, row2);
        gridPane.getColumnConstraints().addAll(col1);

        TextField amountInput = new TextField();
        amountInput.setPromptText("Enter the amount being withdrawn");
        amountInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*") || !newValue.matches("\\.")) {
                    amountInput.setText(newValue.replaceAll("[^\\d\\.]", ""));
                }
            }
        });
        gridPane.add(amountInput, 0, 0, 1, 1);

        TextField description = new TextField();
        description.setPromptText("Enter a description");
        gridPane.add(description, 0, 1, 1, 1);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(gridPane);
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                double amount = InputUtils.getDoubleFromString(amountInput.getText());
                accountManager.withdrawMoney(account, amount, description.getText());
                loadDashboardCenter();
            } catch (Exception e){
                event.consume();
                app.displayError(e.getMessage());
            }
        });

        dialog.showAndWait();
    }

    private void transferMoney(Account account) {
        GridPane gridPane = new GridPane();
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(30);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(40);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(30);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100);

        gridPane.getRowConstraints().addAll(row1, row2);
        gridPane.getColumnConstraints().addAll(col1);

        TextField amountInput = new TextField();
        amountInput.setPromptText("Enter the amount being transferred");
        amountInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*") || !newValue.matches("\\.")) {
                    amountInput.setText(newValue.replaceAll("[^\\d\\.]", ""));
                }
            }
        });
        gridPane.add(amountInput, 0, 0, 1, 1);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(accountManager.getAccounts().stream().filter(acc -> !acc.getName().equals(account.getName())).map(Account::getName).toList());
        gridPane.add(choiceBox, 0, 1, 1, 1);

        TextField description = new TextField();
        description.setPromptText("Enter a description");
        gridPane.add(description, 0, 2, 1, 1);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(gridPane);
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                double amount = InputUtils.getDoubleFromString(amountInput.getText());
                Account toAccount = accountManager.getAccountFromName(choiceBox.getValue());
                accountManager.transferMoney(account, toAccount, amount, description.getText());
                loadDashboardCenter();
            } catch (Exception e){
                event.consume();
                app.displayError(e.getMessage());
            }
        });

        dialog.showAndWait();
    }

    private void borrowMoney(Account account) {
        GridPane gridPane = new GridPane();
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(30);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(40);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(30);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100);

        gridPane.getRowConstraints().addAll(row1, row2);
        gridPane.getColumnConstraints().addAll(col1);

        TextField amountInput = new TextField();
        amountInput.setPromptText("Enter the amount being borrowed");
        amountInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*") || !newValue.matches("\\.")) {
                    amountInput.setText(newValue.replaceAll("[^\\d\\.]", ""));
                }
            }
        });
        gridPane.add(amountInput, 0, 0, 1, 1);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(accountManager.getAccounts().stream().filter(acc -> !acc.getName().equals(account.getName())).map(Account::getName).toList());
        gridPane.add(choiceBox, 0, 1, 1, 1);

        TextField description = new TextField();
        description.setPromptText("Enter a description");
        gridPane.add(description, 0, 2, 1, 1);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(gridPane);
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                double amount = InputUtils.getDoubleFromString(amountInput.getText());
                Account fromAccount = accountManager.getAccountFromName(choiceBox.getValue());
                accountManager.borrowMoney(fromAccount, account, amount, description.getText());
                loadDashboardCenter();
            } catch (Exception e){
                event.consume();
                app.displayError(e.getMessage());
            }
        });

        dialog.showAndWait();
    }

    private void repayDebt(Account account) {
        GridPane gridPane = new GridPane();
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(30);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(40);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(30);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100);

        gridPane.getRowConstraints().addAll(row1, row2);
        gridPane.getColumnConstraints().addAll(col1);

        TextField amountInput = new TextField();
        amountInput.setPromptText("Enter the amount being repayed");
        amountInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*") || !newValue.matches("\\.")) {
                    amountInput.setText(newValue.replaceAll("[^\\d\\.]", ""));
                }
            }
        });
        gridPane.add(amountInput, 0, 0, 1, 1);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(accountManager.getAccounts().stream().filter(acc -> !acc.getName().equals(account.getName())).map(Account::getName).toList());
        gridPane.add(choiceBox, 0, 1, 1, 1);

        TextField description = new TextField();
        description.setPromptText("Enter a description");
        gridPane.add(description, 0, 2, 1, 1);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(gridPane);
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                double amount = InputUtils.getDoubleFromString(amountInput.getText());
                Account toAccount = accountManager.getAccountFromName(choiceBox.getValue());
                accountManager.repayMoney(account, toAccount, amount, description.getText());
                loadDashboardCenter();
            } catch (Exception e){
                event.consume();
                app.displayError(e.getMessage());
            }
        });

        dialog.showAndWait();
    }

    private void editAccount(Account account) {

    }

    private List<Transaction> sortRecentTransactionsByDate(List<Transaction> transactions) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        Comparator<Transaction> dateComparator = (transaction1, transaction2) -> {
            try {
                Date date1 = dateFormat.parse(transaction1.getDate());
                Date date2 = dateFormat.parse(transaction2.getDate());
                return date2.compareTo(date1);
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        };

        transactions.sort(dateComparator);

        return transactions;
    }

    private void deleteAccount(Account account) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete account named '" + account.getName() + "'?");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.YES);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                accountManager.deleteAccount(account.getName());
                loadDashboardCenter();
            } catch (Exception e){
                event.consume();
                app.displayError(e.getMessage());
            }
        });

        dialog.showAndWait();
    }

    public String getTitle() {
        return this.title;
    }

    public Scene getScene() {
        return this.scene;
    }

    public Pane getPane() {
        return this.pane;
    }
}
