package co.sun.auto.fluter.demofx.controller;

import co.sun.auto.fluter.demofx.controller.ControllerCallback.SuccessCallback;
import co.sun.auto.fluter.demofx.controller.ControllerCallback.VerifyCardCallback;
import co.sun.auto.fluter.demofx.model.ApplicationState;
import co.sun.auto.fluter.demofx.model.Citizen;
import co.sun.auto.fluter.demofx.util.HexUtils;
import co.sun.auto.fluter.demofx.util.RSAUtils;

import javax.smartcardio.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class CardController {
    private static final String CHARACTERS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static CardController instance = null;
    private static int testPinAttempt = 5;
    private final ApplicationState appState;
    public byte[] avatarTest = null;
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

    public static String hexToStringUnicode(String hex) {
        if (hex == null || hex.isEmpty()) {
            return "";
        }
        hex = hex.replace(" ", ""); // Remove spaces

        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid hex string length");
        }

        byte[] byteArray = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byteArray[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
        }

        return new String(byteArray, StandardCharsets.UTF_8);
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

    public static String getLast6CharactersIncremented() {
        String latestId = DBController.getLatestCitizenId();

        if (latestId == null) {
            return "000001";
        }

        if (latestId.length() >= 6) {
            String last6Chars = latestId.substring(latestId.length() - 6);
            int last6Int = Integer.parseInt(last6Chars);
            return String.format("%06d", last6Int + 1);
        }
        return "000001";
    }

    public static String generateId() {
        String prefix = new SimpleDateFormat("ddMMyy").format(new Date());
        String subFix = getLast6CharactersIncremented();
        return prefix + subFix;
    }

    public void connectCardForTest(SuccessCallback callback) {
        appState.isCardInserted = true;
        callback.callback(true);
    }

    public void getAvatar(Citizen citizen) {
        // /send 00020509
        ApduResult result = sendApdu((byte) 0x00, (byte) 0x02, (byte) 0x05, (byte) 0x09, null);
        if (result.isSuccess) {
            System.out.println("APDU command executed successfully!");
            System.out.println("response: " + bytesToHex(result.response));
            citizen.setAvatar(result.response);
        } else {
            System.out.println("Failed to execute APDU command.");
            System.out.println("response: " + bytesToHex(result.response));
        }
    }

    public boolean challengeCard(String citizenId) {
        Random random = new Random();
        String challenge = String.valueOf(random.nextInt(1000000));

        System.out.println("[DEBUG] Challenge: " + challenge);

        String storedPublicKey = DBController.getPublicKeyById(citizenId);

        System.out.println("[DEBUG] Stored public key: " + storedPublicKey);

        if (storedPublicKey == null) {
            return false;
        }

        byte[] publicKey = HexUtils.parseHexStringToByteArray(storedPublicKey);

        System.out.println("[DEBUG] Public key: " + bytesToHex(publicKey));

        ApduResult result = sendApdu((byte) 0x00, (byte) 0x01, (byte) 0x06, (byte) 0x00, stringToHexArray(challenge));
        if (result.isSuccess) {
            System.out.println("APDU command executed successfully!");
            System.out.println("response: " + bytesToHex(result.response));
            System.out.println("[DEBUG] Signature Sucess: " + bytesToHex(result.response));

            return verifySignature(publicKey, result.response, challenge);
        } else {
            System.out.println("Failed to execute APDU command.");
            System.out.println("response: " + bytesToHex(result.response));
            System.out.println("[DEBUG] Signature Failed: " + bytesToHex(result.response));
            return false;
        }
    }

    private boolean verifySignature(byte[] publicKey, byte[] signature, String challenge) {
        PublicKey key = RSAUtils.generatePublicKeyFromBytes(publicKey);
        if (key == null) {
            return false;
        }
        return RSAUtils.accuracy(signature, key, challenge);
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

            terminals.list().forEach(terminal -> System.out.println("Thiết bị thẻ: " + terminal.getName()));

            CardTerminal terminal = terminals.list().getFirst();
            System.out.println("Đang kết nối tới thiết bị thẻ: " + terminal.getName());

            System.out.println("Card present: " + terminal.isCardPresent());

            if (!terminal.isCardPresent()) {
                System.out.println("Không có thẻ nào được chèn vào thiết bị.");
                callback.callback(false);
                return;
            }

            appState.card = terminal.connect("T=1");
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
        } catch (Exception e) {
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

    public void verifyCard(String pinCode, VerifyCardCallback callback) {
        // send 00000000
        byte cla = (byte) 0x00;
        byte ins = (byte) 0x00; // INS = 0x00
        byte p1 = (byte) 0x00;
        byte p2 = (byte) 0x00;

        // Sending an APDU with the defined parameters
        ApduResult result = sendApdu(cla, ins, p1, p2, stringToHexArray(pinCode));

        if (result.isSuccess) {
            System.out.println("APDU command executed successfully!");
            System.out.println("response: " + bytesToHex(result.response));
            appState.isCardVerified = true;
            callback.callback(true, 5);
        } else {
            System.out.println("Failed to execute APDU command.");
            System.out.println("response: " + bytesToHex(result.response));
            callback.callback(false, Integer.parseInt(bytesToHex(result.response)));
        }

    }

    public String getCardId() {
        ApduResult result = sendApdu((byte) 0x00, (byte) 0x02, (byte) 0x05, (byte) 0x0A, null);
        if (result.isSuccess) {
            System.out.println("APDU command executed successfully!");
            System.out.println("response: " + bytesToHex(result.response));

            if (result.response == null) {
                return null;
            }

            if (result.response.length != 12) {
                System.out.println("Invalid Card Id length.");
                return null;
            }

            System.out.println("Card Id: " + hexToString(bytesToHex(result.response)));
            return hexToString(bytesToHex(result.response));
        } else {
            System.out.println("Failed to execute APDU command.");
            System.out.println("response: " + bytesToHex(result.response));
            return null;
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

    public void setupPinCode(String pin, SuccessCallback callback) {
        // send 00030A00
        ApduResult result = this.sendApdu((byte) 0x00, (byte) 0x03, (byte) 0x0A, (byte) 0x00, stringToHexArray(pin));
        if (result.isSuccess) {
            System.out.println("APDU command executed successfully!");
            System.out.println("response: " + bytesToHex(result.response));
            appState.isCardVerified = true;
            appState.isCardInserted = true;

            callback.callback(true);
        } else {
            System.out.println("Failed to execute APDU command.");
            callback.callback(false);
        }
    }

    public void setupPinCode(String pin, Citizen citizen, SuccessCallback callback) {
//        callback.callback(pin.equals("123456"));
        // /send 00010500
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12 - 8; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        sb.append(Instant.now().toEpochMilli());

        citizen.setCitizenId(generateId());

        System.out.println("=====>" + bytesToHex(stringToHexArray(citizen.toCardInfo() + "$" + pin)));
        Date createdAt = new Date();

        // Define the desired date format
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        // Format the Date object
        String formattedDate = dateFormatter.format(createdAt);


        ApduResult result = this.sendApdu((byte) 0x00, (byte) 0x01, (byte) 0x05, (byte) 0x00, stringToHexArray(citizen.toCardInfo() + "$" + formattedDate + "$" + pin));
        if (result.isSuccess) {
            System.out.println("APDU command executed successfully!");
            appState.isCardVerified = true;
            appState.isCardInserted = true;

            String publicKey = bytesToHex(result.response);
            System.out.println("=====>publicKey: " + publicKey);

            DBController.insertCitizen(citizen);
            DBController.updatePublicKey(citizen.getCitizenId(), publicKey);

            if (citizen.getAvatar() != null) {
                sendAvatar(citizen.getAvatar(), (isSuccess) -> {
                    if (isSuccess) {
                        System.out.println("Avatar sent successfully!");
                    } else {
                        System.out.println("Failed to send avatar.");
                    }
                });
            }

            callback.callback(true);
        } else {
            System.out.println("Failed to execute APDU command.");
            callback.callback(false);
        }

    }

    public void sendAvatar(byte[] avatar, SuccessCallback callback) {
        // /send 00030509
        ApduResult result = sendApdu((byte) 0x00, (byte) 0x03, (byte) 0x05, (byte) 0x09, avatar);
        if (result.isSuccess) {
            System.out.println("APDU command executed successfully!");
            callback.callback(true);
        } else {
            System.out.println("Failed to execute APDU command.");
            callback.callback(false);
        }
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

//    public void testSendCard(SuccessCallback callback) {
//        // Define APDU parameters
//        byte cla = (byte) 0x00;
//        byte ins = (byte) 0x01; // INS = 0x00
//        byte p1 = (byte) 0x01;
//        byte p2 = (byte) 0x00;
//        byte[] data = null; // No additional data for this example
//
//        // Sending an APDU with the defined parameters
//        byte[] response = sendApdu(cla, ins, p1, p2, stringToHexArray("8"), (isSuccess, response) -> {
//            if (isSuccess) {
//                System.out.println("APDU command executed successfully!");
//                callback.callback(true);
//            } else {
//                System.out.println("Failed to execute APDU command.");
//                callback.callback(false);
//            }
//        });
//
//        // Logging the response
//        if (response != null) {
//            System.out.println("Card Response Data: " + CardController.hexToString(CardController.bytesToHex(response)));
//        } else {
//            System.out.println("No data received from the card.");
//        }
//    }

    public void createCardDataTest(Citizen citizen, SuccessCallback callback) {
        callback.callback(true);
        isCardDataCreated = true;

    }

    public void updateCardData(Citizen citizen, SuccessCallback callback) {
        // /send 00030507
        //TODO: Update information to card
        ApduResult result = sendApdu((byte) 0x00, (byte) 0x03, (byte) 0x05, (byte) 0x07, stringToHexArray(citizen.toCardInfo()));
        if (result.isSuccess) {
            System.out.println("APDU command executed successfully!");
            System.out.println("response: " + bytesToHex(result.response));
            callback.callback(true);
            DBController.updateCitizen(citizen);
        } else {
            System.out.println("Failed to execute APDU command.");
            System.out.println("response: " + bytesToHex(result.response));
            callback.callback(false);
        }
    }

    public void changePinCode(String pinCode, SuccessCallback callback) {

    }

    public void activateCard(SuccessCallback callback) {

    }

    public void deactivateCard(SuccessCallback callback) {

    }


    /**
     * Sends an APDU command to the card and receives a response.
     */
    public ApduResult sendApdu(byte cla, byte ins, byte p1, byte p2, byte[] data) {
        try {
            // Get the CardChannel
            CardChannel channel = appState.card.getBasicChannel();

            // Construct the APDU command with a 3-byte LC (extended length)
            ByteArrayOutputStream apduStream = new ByteArrayOutputStream();
            apduStream.write(cla);
            apduStream.write(ins);
            apduStream.write(p1);
            apduStream.write(p2);

            // Always use a 3-byte LC encoding for data length
            int dataLength = data == null ? 0 : data.length; // Add 1 byte for the data length

            // The first byte of LC is always 0x00 (indicating extended length)
            apduStream.write((dataLength >> 16) & 0xFF); // High byte (most significant byte of LC)
            apduStream.write((dataLength >> 8) & 0xFF);  // Middle byte of LC
            apduStream.write(dataLength & 0xFF);         // Low byte (least significant byte of LC)

            // Write the data to the APDU
            if (data != null) {
                apduStream.write(data);
            }

            // Create the APDU command from the byte array
            CommandAPDU command = new CommandAPDU(apduStream.toByteArray());

            // Log the APDU being sent
            System.out.println("Sending APDU: " + command);

            // Send the APDU and receive the response
            ResponseAPDU response = channel.transmit(command);

            // Log the response status word and data
            System.out.println("APDU Response SW: " + Integer.toHexString(response.getSW()));
            if (response.getData().length > 0) {
                System.out.println("APDU Response Data: " + Arrays.toString(response.getData()));
            }

            // Check if the command was successful
            if (response.getSW() == 0x9000) {
                return new ApduResult(true, response.getData());
            } else {
                System.err.println("APDU failed with status word: " + Integer.toHexString(response.getSW()));
                return new ApduResult(false, response.getData());
            }

        } catch (CardException | IOException e) {
            e.printStackTrace();
            return new ApduResult(false, null); // Return null in case of exceptions
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
        // /send 00020507
        System.out.println("Call getCardInfo");
        ApduResult result = sendApdu((byte) 0x00, (byte) 0x02, (byte) 0x05, (byte) 0x07, null);
        if (result.isSuccess) {
            // Log the personalInformation
            System.out.println("...");
            System.out.println("=====>Card Response Data L1: " + bytesToHex(result.response));
            System.out.println("=====>Card Response Data: " + hexToString(bytesToHex(result.response)));
            Citizen citizen = new Citizen();
            citizen.fromCardInfo(hexToStringUnicode(bytesToHex(result.response)));
            isCardDataCreated = true;

            // Get avatar
            getAvatar(citizen);

            return citizen;
        } else {
            System.out.println("Failed to execute APDU command.");
            return null;
        }
    }

    public Citizen fakeCitizen() {
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

    public void changePin(String oldPin, String newPin, SuccessCallback callback) {
        // /send 00030500
        // TODO: Change pin code
        ApduResult result = sendApdu((byte) 0x00, (byte) 0x03, (byte) 0x05, (byte) 0x00, stringToHexArray(oldPin + "$" + newPin));
        if (result.isSuccess) {
            System.out.println("APDU command executed successfully!");
            System.out.println("response: " + bytesToHex(result.response));
            callback.callback(true);
        } else {
            System.out.println("Failed to execute APDU command.");
            System.out.println("response: " + bytesToHex(result.response));
            callback.callback(false);
        }
    }

    public void deactiveCard(SuccessCallback callback) {
        // /send 00030C00
        //TODO: Deactive card
        ApduResult result = sendApdu((byte) 0x00, (byte) 0x03, (byte) 0x0C, (byte) 0x00, null);
        if (result.isSuccess) {
            System.out.println("APDU command executed successfully!");
            System.out.println("response: " + bytesToHex(result.response));
            callback.callback(true);
        } else {
            System.out.println("Failed to execute APDU command.");
            System.out.println("response: " + bytesToHex(result.response));
            callback.callback(false);
        }
    }

    public void activeCard(String pinCode, SuccessCallback callback) {
        // /send 00030B00
        ApduResult result = sendApdu((byte) 0x00, (byte) 0x03, (byte) 0x0B, (byte) 0x00, null);
        if (result.isSuccess) {
            System.out.println("APDU command executed successfully!");
            System.out.println("response: " + bytesToHex(result.response));
            callback.callback(true);
        } else {
            System.out.println("Failed to execute APDU command.");
            System.out.println("response: " + bytesToHex(result.response));
            callback.callback(false);
        }
    }

    public void isCardActive(SuccessCallback callback) {
        // send 00020508
        ApduResult result = sendApdu((byte) 0x00, (byte) 0x02, (byte) 0x05, (byte) 0x08, null);
        if (result.isSuccess) {
            System.out.println("APDU command executed successfully!");
            System.out.println("response: " + bytesToHex(result.response));
            // Check if the response (short) > 0
            callback.callback(Integer.parseInt(bytesToHex(result.response)) > 0);
        }
        else {
            System.out.println("Failed to execute APDU command.");
            System.out.println("response: " + bytesToHex(result.response));
            callback.callback(false);
        }
    }
}
