package co.sun.auto.fluter.demofx.controller;

import co.sun.auto.fluter.demofx.controller.ControllerCallback.SuccessCallback;
import co.sun.auto.fluter.demofx.controller.ControllerCallback.VerifyCardCallback;
import co.sun.auto.fluter.demofx.model.ApplicationState;
import co.sun.auto.fluter.demofx.model.Citizen;

import javax.smartcardio.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CardController {
    private static CardController instance = null;
    private static int testPinAttempt = 5;
    private final ApplicationState appState;
    //Just for test UI
    private boolean isCardDataCreated = false;

    private CardController() {
        appState = new ApplicationState();
    }

    /**
     * Helper to convert bytes to a hex string.
     */
    public static String hexToString(String hex) {
        if (hex == null || hex.isEmpty()) {
            return "";
        }
        hex = hex.replace(" ", ""); // Remove spaces
        StringBuilder sb = new StringBuilder();
        // Ensure the hex string has an even length

        System.out.println("Hex to string: " + hex);
        System.out.println("=====>hex length: " + hex.length());

        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid hex string length");
        }

        for (int i = 0; i < hex.length(); i += 2) {
            // Get two characters (one byte) from the hex string and convert to byte
            String hexByte = hex.substring(i, i + 2);
            sb.append((char) Integer.parseInt(hexByte, 16)); // Convert hex byte to char and append
        }

        return sb.toString();
    }

    public static CardController getInstance() {
        if (instance == null) {
            instance = new CardController();
        }
        return instance;
    }

    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X ", b));
        }
        return hexString.toString().trim();
    }

    public static boolean validateStatusWord(byte[] response) {
        return response.length >= 2 && response[response.length - 2] == (byte) 0x90 && response[response.length - 1] == (byte) 0x00;
    }

    public void connectCardForTest(SuccessCallback callback) {
        appState.isCardInserted = true;
        callback.callback(true);
    }

    /**
     * Connects to a smart card.
     */
    public void connectCard(SuccessCallback callback) {
        byte[] AID_APPLET = {(byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0x55, (byte) 0x00};
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            CardTerminals terminals = factory.terminals();
            System.out.println("Số lượng thiết bị thẻ có sẵn: " + terminals.list().size());

            if (terminals.list().isEmpty()) {
                System.out.println("Không tìm thấy thiết bị thẻ.");
                callback.callback(false);
                return;
            }

            CardTerminal terminal = terminals.list().get(0);
            System.out.println("Đang kết nối tới thiết bị thẻ: " + terminal.getName());

            appState.card = terminal.connect("T=0");
            System.out.println("Kết nối thành công tới thẻ: " + appState.card);

            CardChannel channel = appState.card.getBasicChannel();

            CommandAPDU command = new CommandAPDU(0x00, 0xA4, 0x04, 0x00, AID_APPLET);
            ResponseAPDU response = channel.transmit(command);
            System.out.println("Kết quả trả về: " + Integer.toHexString(response.getSW()));

            if (response.getSW() == 0x9000) {
                System.out.println("Kết nối thẻ thành công!");
                appState.isCardInserted = true;
                callback.callback(true);
            } else {
                System.out.println("Kết nối thẻ thất bại. SW: " + Integer.toHexString(response.getSW()));
                callback.callback(false);
            }
        } catch (CardException e) {
            System.out.println("Lỗi khi kết nối thẻ: " + e.getMessage());
            callback.callback(false);
        }
    }

    public boolean isCardConnected() {
//        return appState.card != null && appState.isCardVerified;
        return appState.isCardInserted && appState.isCardVerified;
    }

    /**
     * Verifies the card PIN.
     */
    public void verifyCardTest(String pinCode, VerifyCardCallback callback) {

        if ("******".equals(pinCode)) {
            callback.callback(true, testPinAttempt);
            appState.isCardVerified = true;
            testPinAttempt = 5;
        } else {
            appState.isCardVerified = false;
            callback.callback(false, testPinAttempt);
            testPinAttempt--;
        }
    }

    /**
     * Change pin code
     */
    public void changePinCodeTest(String pin, SuccessCallback callback) {
        callback.callback(pin.equals("123456"));
        appState.isCardInserted = true;
        appState.isCardVerified = false;
    }

    /**
     * Setup pin code first time
     */
    public void setupPinCodeTest(String pin, SuccessCallback callback) {
        callback.callback(pin.equals("123456"));
        appState.isCardInserted = true;
        appState.isCardVerified = true;
    }

    public void setupPinCode(String pin, Citizen citizen, SuccessCallback callback) {
//        callback.callback(pin.equals("123456"));
        // /send 00010500
        System.out.println("=====>" + bytesToHex(stringToHexArray(citizen.toCardInfo() + "$" + pin)));
        Date createdAt = new Date();

        // Define the desired date format
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        // Format the Date object
        String formattedDate = dateFormatter.format(createdAt);


        this.sendApdu((byte) 0x00, (byte) 0x01, (byte) 0x05, (byte) 0x00, stringToHexArray(citizen.toCardInfo() + "$" + formattedDate + "$" + pin), (isSuccess) -> {
            if (isSuccess) {
                System.out.println("APDU command executed successfully!");
                appState.isCardVerified = true;
                appState.isCardInserted = true;
                callback.callback(true);
            } else {
                System.out.println("Failed to execute APDU command.");
                callback.callback(false);
            }
        });

    }

    /**
     * Disconnects from the smart card.
     */
    public void disconnectCardTest(SuccessCallback callback) {
        appState.isCardInserted = false;
        appState.isCardVerified = false;
        callback.callback(true);

    }

    /**
     * Disconnects from the smart card.
     */
    public void disconnectCard(SuccessCallback callback) {
        if (appState.card == null) {
            System.out.println("Không có thẻ nào để ngắt kết nối.");
            callback.callback(false);
            return;
        }

        try {
            appState.card.disconnect(false);
            appState.card = null; // Reset the card state
            System.out.println("Đã ngắt kết nối thẻ thành công.");
            appState.card = null;
            appState.isCardVerified = false;
            appState.isCardInserted = false;
            callback.callback(true);
        } catch (Exception e) {
            System.out.println("Lỗi khi ngắt kết nối thẻ: " + e.getMessage());
            callback.callback(false);
        }
    }

    public void testSendCard(SuccessCallback callback) {
        // Define APDU parameters
        byte cla = (byte) 0x00;
        byte ins = (byte) 0x01; // INS = 0x00
        byte p1 = (byte) 0x01;
        byte p2 = (byte) 0x00;
        byte[] data = null; // No additional data for this example

        // Sending an APDU with the defined parameters
        byte[] response = sendApdu(cla, ins, p1, p2, stringToHexArray("8"), (isSuccess) -> {
            if (isSuccess) {
                System.out.println("APDU command executed successfully!");
                callback.callback(true);
            } else {
                System.out.println("Failed to execute APDU command.");
                callback.callback(false);
            }
        });

        // Logging the response
        if (response != null) {
            System.out.println("Card Response Data: " + CardController.hexToString(CardController.bytesToHex(response)));
        } else {
            System.out.println("No data received from the card.");
        }
    }

    public void createCardDataTest(Citizen citizen, SuccessCallback callback) {
        callback.callback(true);
        isCardDataCreated = true;

    }

    /**
     * Sends an APDU command to the card and receives a response.
     */
    public byte[] sendApdu(byte cla, byte ins, byte p1, byte p2, byte[] data, SuccessCallback callback) {
        try {
            // Get the CardChannel
            CardChannel channel = appState.card.getBasicChannel();

            // Construct the APDU command
            CommandAPDU command;
            if (data != null) {
                command = new CommandAPDU(cla, ins, p1, p2, data);
            } else {
                command = new CommandAPDU(cla, ins, p1, p2);
            }

            // Log the APDU being sent
            System.out.println("Sending APDU: " + command);

            // Send the APDU and receive the response
            ResponseAPDU response = channel.transmit(command);

            // Log the response status word and data
            System.out.println("Response SW: " + Integer.toHexString(response.getSW()));
            if (response.getData().length > 0) {
                System.out.println("Response Data APDU: " + bytesToHex(response.getData()));
            }

            // Check if the command was successful
            if (response.getSW() == 0x9000) {
                callback.callback(true);
                System.out.print("Response Data APDU raw byte=>>: ");
                for (byte b : response.getData()) {
                    System.out.print(b + " ");
                }
                System.out.println();
                System.out.println("Response Data APDU =>>: " + bytesToHex(response.getData()));
                return response.getData(); // Return the response data
            } else {
                callback.callback(false);
                System.err.println("APDU failed with status word: " + Integer.toHexString(response.getSW()));
                return null; // Return null for error cases
            }

        } catch (CardException e) {
            e.printStackTrace();
            callback.callback(false);
            return null; // Return null in case of exceptions
        }
    }

    public byte[] stringToHexArray(String str) {
        // Convert the string into a byte array
        byte[] byteArray = str.getBytes(StandardCharsets.UTF_8);

        // Create a byte array to hold the hex values
        byte[] hexArray = new byte[byteArray.length];

        // Convert each byte to its hex representation
        System.arraycopy(byteArray, 0, hexArray, 0, byteArray.length);

        return hexArray;
    }

    /**
     * @return Citizen and null if card does not have information
     */
    public Citizen getCardInfoTest() {
        if (isCardDataCreated) {
            return fakeCitizen();
        }
        return null;
    }

    public Citizen getCardInfo() {
        byte[] personalInformation;
        // /send 00020507
        personalInformation = sendApdu((byte) 0x00, (byte) 0x02, (byte) 0x05, (byte) 0x07, null, (isSuccess) -> {
            if (isSuccess) {
                // Log the personalInformation
                System.out.println("...");
            } else {
                System.out.println("Failed to execute APDU command.");
            }
        });

        // Log the personalInformation
        if (personalInformation != null) {
            System.out.println("=====>Card Response Data L1: " + bytesToHex(personalInformation));
            System.out.println("=====>Card Response Data: " + hexToString(bytesToHex(personalInformation)));
            Citizen citizen = new Citizen();
            citizen.fromCardInfo(hexToString(bytesToHex(personalInformation)));
            isCardDataCreated = true;
            return citizen;

        } else {
            return null;
        }

//        if (isCardDataCreated) {
//            return fakeCitizen();
//        }
//        return null;
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
