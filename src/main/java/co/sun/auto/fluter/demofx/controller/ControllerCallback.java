package co.sun.auto.fluter.demofx.controller;

public class ControllerCallback {
    public interface LoginCallBack {
        void callback(String username, String password, boolean access);
    }

    public interface SuccessCallback {
        void callback(boolean success);
    }

    public interface VerifyCardCallback {
        void callback(boolean verified, int pinAttemptRemain);
    }

}
