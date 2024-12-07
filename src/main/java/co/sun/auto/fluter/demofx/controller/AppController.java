package co.sun.auto.fluter.demofx.controller;

import co.sun.auto.fluter.demofx.model.ApplicationState;

public class AppController {
    private static AppController INSTANCE = null;
    private ApplicationState appState = null;

    private AppController() {
        appState = new ApplicationState();
    }

    /*
     * Singleton instance
     */
    public static AppController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppController();
        }
        return INSTANCE;
    }

    /**
     * Generate Card's Number
     */
    public String generateCardNumber() {
        //TODO: Implement card number generation
        return "123456789012";
    }

    /**
     * Handles application login.
     */
    public boolean appLogin(String username, String password, LoginCallback callback) {
        appState.isAppLoggedIn = true;
        callback.callback(username, password, true);
        return true;
    }

    /*
     * Callback Interfaces
     */


    public interface LoginCallback {
        void callback(String username, String password, boolean success);
    }
}
