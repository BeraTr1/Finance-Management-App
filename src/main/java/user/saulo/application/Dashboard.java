package user.saulo.application;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import eu.hansolo.medusa.Test;
import eu.hansolo.medusa.skins.SimpleSectionSkin;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import user.saulo.Account;
import user.saulo.FinancesManagementApp;
import user.saulo.managers.AppManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Dashboard {
    private String title = "Dashboard";
    private Scene scene;
    private BorderPane pane;
    private AppManager appManager;
    private App app;

    public Dashboard() {
        this.app = App.instance;
        this.appManager = FinancesManagementApp.appManager;
        BorderPane gridPane = getDashboardPane();
        this.pane = gridPane;
        this.scene = new Scene(gridPane);
        this.load();
    }

    private void load() {
        loadDashboardTop();
        loadDashboardLeft();
        loadDashboardCenter();
    }

    private BorderPane getDashboardPane() {
        BorderPane gridPane = new BorderPane();

        return gridPane;
    }

    private void loadDashboardTop() {
        HBox background = new HBox();
        Button createAccountButton = new Button();

        background.setStyle("-fx-background-color: #DFACEF");
        createAccountButton.setOnAction(actionEvent -> createAccountDialog());
        createAccountButton.setText("Create New Account");
        background.getChildren().add(createAccountButton);
        this.pane.setTop(background);
    }

    private void loadDashboardCenter() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // load account cards
        List<Account> accounts = appManager.getAccounts();
        int maxRow = 10;
        int maxCol = 5;

        for (int row = 1; row <= maxRow; row++) {
            for (int col = 1; col <= maxCol; col++) {
                if (accounts.stream().findFirst().isEmpty()) {
                    break;
                }

                Account account = accounts.stream().findFirst().get();

                HBox accountCard = new HBox();
                final double accountCardWidth = 250;
                final double accountCardHeight = 350;
                accountCard.setStyle("-fx-background-color: #484848");
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
//                gridPane.setGridLinesVisible(true);
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

                double balancePercentage = appManager.roundDouble(accountBalance / totalMoney);
                double arcBalanceAngleLength = appManager.roundDouble(balancePercentage * 270);
                Color arcBalanceColor = Color.web("#3FDA2A");
                Arc arcBalance = createArc(arcBalanceColor, balancePercentage);
//
                double creditPercentage = appManager.roundDouble(accountCredit / totalMoney);
                double arcCreditAngleLength = appManager.roundDouble(creditPercentage * 270 + arcBalanceAngleLength);
                Color arcCreditColor = Color.web("#97FC5E");
                Arc arcCredit = createArc(arcCreditColor, creditPercentage);

                double debtPercentage = appManager.roundDouble(accountDebt / totalMoney);
                double arcDebtAngleLenght = appManager.roundDouble(debtPercentage * 270 + arcCreditAngleLength);
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

                // -- //

                HBox accountCardLine = new HBox();
                accountCardLine.setStyle("-fx-background-color: #848484");
                accountCardLine.setMinSize(accountCardWidth * 0.95, 1);
                accountCardLine.setMaxSize(accountCardWidth * 0.95, 1);
                gridPane.add(accountCardLine, 0, 2, 4, 1);

                Text accountName = new Text(account.getName());
                gridPane.add(accountName, 0, 3, 4, 1);
                GridPane.setHalignment(accountName, HPos.CENTER);
//                accountName.setTextAlignment(TextAlignment.CENTER);

                Button depositButton = new Button();
                depositButton.setText("Deposit Money");
                depositButton.setMaxWidth(Double.MAX_VALUE);
                depositButton.setOnAction(action -> {
                    depositMoney(account);
                });
                gridPane.add(depositButton, 0, 4, 2, 1);

                Button withdrawButton = new Button();
                withdrawButton.setText("Withdraw Money");
                withdrawButton.setMaxWidth(Double.MAX_VALUE);
                withdrawButton.setOnAction(action -> {
                    withdrawMoney(account);
                });
                gridPane.add(withdrawButton, 2, 4, 2, 1);

                Button transferButton = new Button();
                transferButton.setText("Transfer Money");
                transferButton.setMaxWidth(Double.MAX_VALUE);
                transferButton.setOnAction(action -> {
                    transferMoney(account);
                });
                GridPane.setHalignment(transferButton, HPos.CENTER);
                gridPane.add(transferButton, 0, 5, 1, 1);

                Button borrowButton = new Button();
                borrowButton.setText("Borrow Money");
                borrowButton.setMaxWidth(Double.MAX_VALUE);
                borrowButton.setOnAction(action -> {
                    borrowMoney(account);
                });
                GridPane.setHalignment(borrowButton, HPos.CENTER);
                gridPane.add(borrowButton, 1, 5, 1, 1);

                Button repayDebtButton = new Button();
                repayDebtButton.setText("Repay Debt");
                repayDebtButton.setMaxWidth(Double.MAX_VALUE);
                repayDebtButton.setOnAction(action -> {
                    repayDebt(account);
                });
                GridPane.setHalignment(repayDebtButton, HPos.CENTER);
                gridPane.add(repayDebtButton, 2, 5, 1, 1);

                grid.add(accountCard, col, row);
                accounts.remove(account);
            }
        }

        // todo make accounts scrollable when needed

        this.pane.setCenter(grid);
    }

    private Arc createArc(Color color, double percentage) {
        Arc arc = new Arc();
        final double radius = 90;
        arc.setCenterX(radius);
        arc.setCenterY(radius);
        arc.setRadiusX(radius);
        arc.setRadiusY(radius);
        arc.setStartAngle(180);
        arc.setLength(-percentage * 180);
        arc.setStroke(color);
        arc.setStrokeWidth(20);
        arc.setFill(Color.TRANSPARENT);

        return arc;
    }

    private void loadDashboardLeft() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));
    }

    private void inspectAccount(Account account) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setPrefSize(600, 700); // 800, 600
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setGridLinesVisible(true);
        gridPane.setPrefSize(dialog.getHeight(), dialog.getWidth());
        gridPane.setAlignment(Pos.CENTER);

        HBox accountVisual = new HBox();
        accountVisual.setPrefSize(200, 200);
        accountVisual.setStyle("-fx-background-color: #37B432");
        gridPane.add(accountVisual, 0, 0, 5, 2);

        HBox accountDetails = new HBox();
        accountDetails.setPrefSize(200, 50);
        accountDetails.setStyle("-fx-background-color: #AFC915");
        gridPane.add(accountDetails, 0, 2, 5, 1);

        Button depositMoneyButton = new Button();
        depositMoneyButton.setText("Deposit Money");
        depositMoneyButton.setOnAction(event -> {

        });
        gridPane.add(depositMoneyButton, 0, 3, 1, 1);

        Button withdrawMoneyButton = new Button();
        withdrawMoneyButton.setText("Withdraw Money");
        withdrawMoneyButton.setOnAction(event -> {

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

        });
        gridPane.add(borrowMoneyButton, 3, 3, 1, 1);

        Button editAccountButton = new Button();
        editAccountButton.setText("Edit Account");
        editAccountButton.setOnAction(event -> {

        });
        gridPane.add(editAccountButton, 4, 3, 1, 1);

        // todo add recent transactions
        int maxTransactions = 5;

        for (int i = 1; i <= maxTransactions; i++) {
            HBox recentTransactionBox = new HBox();
            recentTransactionBox.setStyle("-fx-background-color: #D4759C");
            recentTransactionBox.setPrefSize(200, 50);

            int row = i + 3;
            gridPane.add(recentTransactionBox, 0, row, 5, 1);
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
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String accountName = accountNameTextField.getText();
                String accountDescription = accountDescriptionTextField.getText();
                createAccount(accountName, accountDescription);
            }

            return null;
        });

        dialog.showAndWait();
    }

    private void createAccount(String accountName, String accountDescription) {
        try {
            appManager.createAccount(accountName, accountDescription);
            loadDashboardCenter();
        } catch (Exception e) {
            this.app.displayError(e.getMessage());
        }
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

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    double amount = appManager.getDoubleFromString(amountInput.getText());
                    appManager.addBalance(account.getName(), amount);
                    loadDashboardCenter();
                } catch (Exception e) {
                    app.displayError(e.getMessage());
                }
            }

            return null;
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

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    double amount = appManager.getDoubleFromString(amountInput.getText());
                    appManager.removeBalance(account.getName(), amount);
                    loadDashboardCenter();
                } catch (Exception e) {
                    app.displayError(e.getMessage());
                }
            }

            return null;
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
        choiceBox.getItems().addAll(appManager.getAccounts().stream().filter(acc -> !acc.getName().equals(account.getName())).map(Account::getName).toList());
        gridPane.add(choiceBox, 0, 1, 1, 1);

        TextField description = new TextField();
        description.setPromptText("Enter a description");

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    double amount = appManager.getDoubleFromString(amountInput.getText());
                    String toAccount = choiceBox.getValue();
                    appManager.transferMoney(account.getName(), toAccount, amount);
                    loadDashboardCenter();
                } catch (Exception e) {
                    app.displayError(e.getMessage());
                }
            }

            return null;
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
        choiceBox.getItems().addAll(appManager.getAccounts().stream().filter(acc -> !acc.getName().equals(account.getName())).map(Account::getName).toList());
        gridPane.add(choiceBox, 0, 1, 1, 1);

        TextField description = new TextField();
        description.setPromptText("Enter a description");

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    double amount = appManager.getDoubleFromString(amountInput.getText());
                    String fromAccount = choiceBox.getValue();
                    appManager.borrowMoney(fromAccount, account.getName(), amount);
                    loadDashboardCenter();
                } catch (Exception e) {
                    app.displayError(e.getMessage());
                }
            }

            return null;
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
        choiceBox.getItems().addAll(appManager.getAccounts().stream().filter(acc -> !acc.getName().equals(account.getName())).map(Account::getName).toList());
        gridPane.add(choiceBox, 0, 1, 1, 1);

        TextField description = new TextField();
        description.setPromptText("Enter a description");

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    double amount = appManager.getDoubleFromString(amountInput.getText());
                    String toAccount = choiceBox.getValue();
                    appManager.repayMoney(account.getName(), toAccount, amount);
                    loadDashboardCenter();
                } catch (Exception e) {
                    app.displayError(e.getMessage());
                }
            }

            return null;
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
