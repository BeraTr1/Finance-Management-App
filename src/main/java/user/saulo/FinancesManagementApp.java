package user.saulo;

import user.saulo.application.App;
import user.saulo.data.SQLite;
import user.saulo.managers.AccountManager;
import user.saulo.managers.DataManager;
import user.saulo.managers.TransactionManager;

import java.io.File;

public class FinancesManagementApp {
    public static FinancesManagementApp instance;

    public static TransactionManager transactionManager;
    public static AccountManager accountManager;
    public static DataManager dataManager;

    public static void main(String[] args) {
        FinancesManagementApp app = new FinancesManagementApp();
        instance = app;
        app.start();
    }

    public void start() {
        this.loadManagers();

        try {
            dataManager.loadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // front end
        App app = new App();
        app.initialize();
    }

    public void end() {
        try {
            dataManager.saveData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadManagers() {
        transactionManager = new TransactionManager();
        accountManager = new AccountManager();
        dataManager = new DataManager(new SQLite(new File("").getAbsolutePath(), "database.db"));
    }
}