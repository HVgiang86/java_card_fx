package co.sun.auto.fluter.demofx.model;

import javax.smartcardio.Card;

public class ApplicationState {
    public boolean isCardInserted = false;
    public boolean isAppLoggedIn = false;
    public boolean isCardVerified = false;
    public String cardNumber = null;
    public Card card = null;

    public boolean isAppLoggedIn() {
        return isAppLoggedIn;
    }
    public boolean isCardInserted() {
        return isCardInserted;
    }
    public boolean isCardVerified() {
        return isCardVerified;
    }
}
