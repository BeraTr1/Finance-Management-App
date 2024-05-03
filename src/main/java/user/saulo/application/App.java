package user.saulo.application;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.stage.WindowEvent;
import user.saulo.Account;
import user.saulo.FinancesManagementApp;
import user.saulo.managers.AppManager;
import user.saulo.managers.DataManager;

import java.util.List;

public class App extends Application { // todo merge this class with Main class
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
        currentStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try {
                    FinancesManagementApp.dataManager.saveData();
                } catch (Exception e) {
                    displayError(e.getMessage());
                }

                Platform.exit();
                System.exit(0);
            }
        });
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
}
