package user.saulo.application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import user.saulo.Account;
import user.saulo.FinancesManagementApp;
import user.saulo.managers.AppManager;

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
                accountCard.setStyle("-fx-background-color: #BCB3FA");
                accountCard.setMinSize(150, 175);

                Text accountName = new Text(account.getName());

                accountCard.getChildren().add(accountName);
                grid.add(accountCard, col, row);

                accounts.remove(account);
            }
        }

        // todo make accounts scrollable when needed

        this.pane.setCenter(grid);
    }

    private void loadDashboardLeft() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // transfer money
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
