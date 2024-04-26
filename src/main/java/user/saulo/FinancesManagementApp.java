package user.saulo;

import user.saulo.application.App;
import user.saulo.managers.AccountManager;
import user.saulo.managers.AppManager;

public class FinancesManagementApp {
    public static AppManager appManager;
    public static AccountManager accountManager;
    public static FinancesManagementApp instance;

    public static void main(String[] args) {
        FinancesManagementApp app = new FinancesManagementApp();
        app.start();
    }

    public void start() {
        this.loadManagers();
        this.loadData();

        // commands
//        Commands commands = new Commands();
//        commands.initialize();

        // front end
        App app = new App();
        app.initialize();
    }

    public void end() {
        this.saveData();
    }

    private void loadData() {

    }

    private void saveData() {

    }

    private void loadManagers() {
        accountManager = new AccountManager();
        appManager = new AppManager();
    }
}