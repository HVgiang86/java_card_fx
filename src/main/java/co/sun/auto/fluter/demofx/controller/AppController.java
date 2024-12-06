package co.sun.auto.fluter.demofx.controller;

import co.sun.auto.fluter.demofx.model.ApplicationState;
import co. sun. auto. fluter. demofx. controller. ControllerCallback.*;

import javax.smartcardio.*;

public class AppController {
    private ApplicationState appState = null;
    private static AppController INSTANCE = null;

    private AppController() {
        appState = new ApplicationState();
    }

    public boolean appLogin(String username, String password, LoginCallBack callBack) {
        appState.isAppLoggedIn = true;
        callBack.callback(username, password, true);
        return true;
    }

    public boolean connectCard(SuccessCallback callback) {
        byte[] AID_APPLET = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00};
        try {
            // Khởi tạo các đối tượng cần thiết để kết nối với thẻ
            TerminalFactory factory = TerminalFactory.getDefault();
            CardTerminals terminals = factory.terminals();
            System.out.println("Số lượng thiết bị thẻ có sẵn: " + terminals.list().size());

            if (terminals.list().isEmpty()) {
                System.out.println("Không tìm thấy thiết bị thẻ.");
                callback.callback(false);
                return false;
            }

            // Lấy thiết bị thẻ đầu tiên
            CardTerminal terminal = terminals.list().get(0);
            System.out.println("Đang kết nối tới thiết bị thẻ: " + terminal.getName());

            // Kết nối tới thẻ
            appState.card = terminal.connect("T=0");
            System.out.println("Kết nối thành công tới thẻ: " + appState.card);

            // Lấy CardChannel từ card
            CardChannel channel = appState.card.getBasicChannel();

            // Gửi lệnh đến thẻ (APDU command) để chọn ứng dụng
            CommandAPDU command = new CommandAPDU(0x00, 0xA4, 0x04, 0x00,  AID_APPLET); // Lệnh chọn ứng dụng
            ResponseAPDU response = channel.transmit(command);  // Dùng CardChannel để gửi lệnh
            String responseHex = Integer.toHexString(response.getSW());
            System.out.println("Kết quả trả về: " + responseHex);

            // Kiểm tra kết quả trả về (SW1=90, SW2=00 là kết quả thành công)
            if (response.getSW() == 0x9000) {
                System.out.println("Kết nối thẻ thành công!");
                callback.callback(true);
                return true;
            } else {
                callback.callback(false);
                return false;
            }

        } catch (CardException e) {
            System.out.println("Lỗi khi kết nối thẻ: " + e.getMessage());
        }

        callback.callback(false);
        return false;
    }

    public void verifyCard(String pinCode, SuccessCallback callback) {
        if ("123466".equals(pinCode)) callback.callback(true);
        System.out.println();
        callback.callback(false);
    }

    public void disconnectCard(SuccessCallback callback) {
        if (appState.card == null) {
            callback.callback(false);
        }

        try {
            appState.card.disconnect(false);
            callback.callback(false);
        } catch (Exception e) {
            e.printStackTrace();
            callback.callback(false);
        }

    }


    /*
    * SINGLETON
     */
    public static AppController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppController();
        }

        return INSTANCE;
    }


}
