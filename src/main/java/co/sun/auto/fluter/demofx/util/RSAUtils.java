package co.sun.auto.fluter.demofx.util;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {
    public static PublicKey generatePublicKey(byte[] exponentBytes, byte[] modulusBytes) {
        BigInteger exponent = new BigInteger(1, exponentBytes);
        BigInteger modulus = new BigInteger(1, modulusBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");

            return factory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }
    }

    public static PublicKey generatePublicKey(byte[] data) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(data);
            KeyFactory factory = KeyFactory.getInstance("RSA");

            return factory.generatePublic(x509EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }
    }

    public static boolean accuracy(byte[] signature, PublicKey publicKey, String code) {
        try {
            Signature verifier = Signature.getInstance("SHA1withRSA");
            verifier.initVerify(publicKey);
            verifier.update(code.getBytes());
            return verifier.verify(signature);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            return false;
        }
    }
}
