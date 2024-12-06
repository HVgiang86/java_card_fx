package co.sun.auto.fluter.demofx.controller;

import co.sun.auto.fluter.demofx.model.Citizen;

public class CardController {
    private static CardController instance = null;

    private CardController() {

    }

    /**
     * @return Citizen and null if card does not have information
     */
    public Citizen getCardInfo() {
        return fakeCitizen();
    }

    public static CardController getInstance() {
        if (instance == null) {
            instance = new CardController();
        }
        return instance;
    }

    private Citizen fakeCitizen() {
        Citizen citizen = new Citizen();
        citizen.setCitizenId("123456789");
        citizen.setBirthDate("01/01/1990");
        citizen.setAddress("123 ABC Street, XYZ City");
        citizen.setEthnicity("Kinh");
        citizen.setIdentification("123456789");
        citizen.setFullName("Nguyen Van A");
        citizen.setGender("Nam");
        citizen.setHometown("Ha Noi");
        citizen.setNationality("Viet Nam");
        citizen.setReligion("Khong");
        return citizen;
    }
}
