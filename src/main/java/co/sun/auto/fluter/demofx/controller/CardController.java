package co.sun.auto.fluter.demofx.controller;

public class CardController {
    private CardController instance = null;

    private CardController() {

    }

    public CardController getInstance() {
        if (instance == null) {
            instance = new CardController();
        }
        return instance;
    }
}
