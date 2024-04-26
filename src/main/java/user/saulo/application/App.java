package user.saulo.application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import user.saulo.Account;
import user.saulo.FinancesManagementApp;
import user.saulo.managers.AppManager;

import java.util.List;

public class App extends Application {
    private static AppManager appManager;
    private static Stage currentStage;
    public static App instance;

    public void initialize() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        appManager = FinancesManagementApp.appManager;
        currentStage = stage;
        loadDashboard();
    }

    public void loadDashboard() {
        Dashboard dashboard = new Dashboard();
        Scene dashboardScene = dashboard.getScene();
        String dashboardTitle = dashboard.getTitle();

        loadScene(dashboardScene, dashboardTitle);
    }

    public void loadScene(Scene scene, String title) {
        currentStage.setScene(scene);
        currentStage.setTitle(title);
        currentStage.setMaximized(true);
        currentStage.show();
    }

    public void displayError(String message) {
        BorderPane borderPane = new BorderPane();
        Text text = new Text();
        text.setText(message);
        borderPane.setCenter(text);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().setContent(borderPane);
        dialog.showAndWait();
    }

//    public void loadDashboard() {
//        Scene dashboardScene = getDashboardScene();
//        currentStage.setTitle("Finances Management Application");
//        currentStage.setScene(dashboardScene);
//
//        currentStage.setMaximized(true);
//
//        currentStage.show();
//    }

//    public Scene getDashboardScene() {
//        BorderPane dashboardPane = new BorderPane();
//        dashboardPane.setCenter(getDashboardCenter());
//        dashboardPane.setTop(getDashboardTop());
//
//        Scene dashboardScene = new Scene(dashboardPane); // , 300, 200
//
//        return dashboardScene;
//    }

//    public Pane getDashboardCenter() {
//        GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(20, 20, 20, 20));
//
//        List<Account> accounts = appManager.getAccounts();
//        int maxRow = 10;
//        int maxCol = 5;
//
//        for (int col = 0; col < maxCol; col++) {
//            for (int row = 0; row < maxRow; row++) {
//                if (accounts.stream().findFirst().isEmpty()) {
//                    break;
//                }
//
//                Account account = accounts.stream().findFirst().get();
//
//                HBox accountCard = new HBox();
//                accountCard.setStyle("-fx-background-color: #BCB3FA");
//                accountCard.setMinSize(150, 175);
//
//                Text accountName = new Text(account.getName());
//
//                accountCard.getChildren().add(accountName);
//                grid.add(accountCard, row, col);
//
//                accounts.remove(account);
//            }
//        }
//
//        return grid;
//    }

//    public HBox getDashboardTop() {
//        HBox background = new HBox();
//        background.setStyle("-fx-background-color: #DFACEF");
//
//        Button createAccountButton = new Button();
//        createAccountButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                int numberOfAccounts = appManager.getAccounts().size();
//                String accountName = "acc" + (numberOfAccounts + 1);
//
//                appManager.createAccount(accountName, "");
//                update();
//            }
//        });
//
//        createAccountButton.setText("Create New Account");
//        background.getChildren().add(createAccountButton);
//
//        return background;
//    }

//    public void update() {
//        Scene t = getDashboardScene();
//        currentStage.setScene(t);
//        currentStage.setMaximized(false);
//        currentStage.setMaximized(true);
//    }
}
