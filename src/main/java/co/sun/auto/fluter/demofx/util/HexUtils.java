package co.sun.auto.fluter.demofx.util;
import static java.lang.Character.digit;

public class HexUtils {
    public static byte[] parseHexStringToByteArray(String hexString) {
        String[] hexArray = hexString.split(" ");
        byte[] byteArray = new byte[hexArray.length];
        for (int i = 0; i < hexArray.length; i++) {
            byteArray[i] = (byte) Integer.parseInt(hexArray[i], 16);
        }
        return byteArray;
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String data;

        for (byte _byte : bytes) {
            data = String.format("%02X", _byte);
            stringBuilder.append(data);
        }
        return stringBuilder.toString();
    }

    public static byte[] fromHexString( String data) {
        int length = data.length();
        byte[] result = new byte[length / 2];

        for (int i = 0; i < length; i += 2) {
            result[i / 2] = (byte) ((digit(data.charAt(i), 16) << 4) + digit(data.charAt(i + 1), 16));
        }
        return result;
    }
}
