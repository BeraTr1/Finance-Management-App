package user.saulo.application;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import eu.hansolo.medusa.skins.SimpleSectionSkin;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

//                HBox accountCardVisual = new HBox();
//                accountCardVisual.setStyle("-fx-background-color: #DAC816");
//                gridPane.add(accountCardVisual, 0, 0, 4, 2);

//                Arc arc = new Arc(100, 100, 90, 90, 0, 180); // centerX, centerY, radiusX, radiusY, startAngle, angleLength
//                Color arcColor = Color.web("#8E8E8E");
//                arc.setStroke(arcColor);
//                arc.setStrokeWidth(20);
//                arc.setFill(Color.TRANSPARENT);
//                gridPane.add(arc, 0, 0, 4, 2);
//                GridPane.setHalignment(arc, HPos.CENTER);

                double accountBalance = account.getBalance();
                double accountCredit = account.getCredit();
                double accountDebt = account.getDebt();
                double totalMoney = (accountBalance + accountCredit + accountDebt) == 0 ? 1 : (accountBalance + accountCredit + accountDebt);

                double balancePercentage = appManager.roundDouble(accountBalance / totalMoney);
////                double arcBalanceStartAngle = 180;
                double arcBalanceAngleLength = appManager.roundDouble(balancePercentage * 270);
//                System.out.println("Balance arc length: " + arcBalanceAngleLength + " | percentage: " + balancePercentage + " | rounded: " + arcBalanceAngleLength);
//                Arc arcBalance = new Arc(100, 100, 90, 90, 180, arcBalanceAngleLength); // centerX, centerY, radiusX, radiusY, startAngle, angleLength
                Color arcBalanceColor = Color.web("#3FDA2A");
//                arcBalance.setStroke(arcBalanceColor);
//                arcBalance.setStrokeWidth(20);
//                arcBalance.setFill(Color.TRANSPARENT);
//                GridPane.setHalignment(arcBalance, HPos.CENTER);
                Arc arcBalance = createArc(arcBalanceColor, balancePercentage);
//
                double creditPercentage = appManager.roundDouble(accountCredit / totalMoney);
////                double arcCreditStartAngle = arcBalanceStartAngle + arcBalanceAngleLength;
                double arcCreditAngleLength = appManager.roundDouble(creditPercentage * 270 + arcBalanceAngleLength);
//                System.out.println("Credit arc length: " + arcCreditAngleLength + " | percentage: " + creditPercentage + " | rounded: " + arcCreditAngleLength);
//                Arc arcCredit = new Arc(100, 100, 90, 90, 180, arcCreditAngleLength); // centerX, centerY, radiusX, radiusY, startAngle, angleLength
                Color arcCreditColor = Color.web("#97FC5E");
//                arcCredit.setStroke(arcCreditColor);
//                arcCredit.setStrokeWidth(20);
//                arcCredit.setFill(Color.TRANSPARENT);
////                GridPane.setHalignment(arcCredit, HPos.CENTER);
                Arc arcCredit = createArc(arcCreditColor, creditPercentage);

                double debtPercentage = appManager.roundDouble(accountDebt / totalMoney);
////                double arcDebtStartAngle = arcCreditStartAngle + arcCreditAngleLength;
                double arcDebtAngleLenght = appManager.roundDouble(debtPercentage * 270 + arcCreditAngleLength);
//                System.out.println("Debt arc length: " + arcDebtAngleLenght + " | percentage: " + debtPercentage + " | rounded: " + arcDebtAngleLenght);
//                Arc arcDebt = new Arc(100, 100, 90, 90, 180, arcDebtAngleLenght); // centerX, centerY, radiusX, radiusY, startAngle, angleLength
                Color arcDebtColor = Color.web("#EE3024");
//                arcDebt.setStroke(arcDebtColor);
//                arcDebt.setStrokeWidth(20);
//                arcDebt.setFill(Color.TRANSPARENT);
//                GridPane.setHalignment(arcDebt, HPos.CENTER);
                Arc arcDebt = new Arc(); //createArc(arcDebtColor, debtPercentage);


//                gridPane.add(arcDebt, 0, 0, 4, 2);
//                gridPane.add(arcCredit, 0, 0, 4, 2);
//                gridPane.add(arcBalance, 0, 0, 4, 2);
//                GridPane.setHalignment(arcDebt, HPos.CENTER);

//                Group group = new Group(arcDebt, arcCredit, arcBalance);
//                GridPane.setHalignment(group, HPos.CENTER);
//                gridPane.add(group, 0, 0, 4, 2);

                // -- //

//                Gauge gauge = GaugeBuilder.create().skinType(Gauge.SkinType.SIMPLE_SECTION).value(30).threshold(50).thresholdColor(Color.YELLOW).thresholdVisible(true).build();
//                Gauge gauge = GaugeBuilder.create().skinType(Gauge.SkinType.SPACE_X).maxValue(totalMoney).value(arcBalanceAngleLength).valueColor(arcBalanceColor).threshold(arcBalanceAngleLength).thresholdColor(arcDebtColor).barBackgroundColor(arcCreditColor).valueVisible(false).build();
//                Gauge gauge = GaugeBuilder.create().skinType(Gauge.SkinType.SPACE_X).maxValue(totalMoney).value(accountBalance).threshold(accountDebt).thresholdColor(Color.PURPLE).backgroundPaint(Color.TRANSPARENT).barColor(arcBalanceColor).barBackgroundColor(Color.BLUE).valueVisible(false).build();
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

                Gauge gauge = GaugeBuilder.create().skinType(Gauge.SkinType.SIMPLE_SECTION).sectionsVisible(true).sectionTextVisible(true).sections(balanceSection, creditSection, debtSection).maxValue(totalMoney).value(accountBalance + accountCredit).barBackgroundColor(accountDebt + accountBalance + accountCredit == 0 ? Color.GRAY : arcDebtColor).barColor(Color.WHITE).valueVisible(false).build();
//                Gauge gauge = GaugeBuilder.create().skinType(Gauge.SkinType.SPACE_X).sections(section).maxValue(100).value(10).threshold(10).thresholdColor(Color.PURPLE).backgroundPaint(Color.TRANSPARENT).barColor(arcBalanceColor).barBackgroundColor(Color.BLUE).valueVisible(false).build();
//                Gauge gauge = GaugeBuilder.create().skinType(Gauge.SkinType.DASHBOARD).value(50).threshold(1000).build();
                gridPane.add(gauge, 0, 0, 4, 2);

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
                gridPane.add(depositButton, 0, 4, 2, 1);

                Button withdrawButton = new Button();
                withdrawButton.setText("Withdraw Money");
                withdrawButton.setMaxWidth(Double.MAX_VALUE);
                gridPane.add(withdrawButton, 2, 4, 2, 1);

                Button transferButton = new Button();
                transferButton.setText("Transfer Money");
                transferButton.setMaxWidth(Double.MAX_VALUE);
                gridPane.add(transferButton, 0, 5, 2, 1);

                Button borrowButton = new Button();
                borrowButton.setText("Borrow Money");
                borrowButton.setMaxWidth(Double.MAX_VALUE);
                gridPane.add(borrowButton, 2, 5, 2, 1);

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
