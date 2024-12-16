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
            e.printStackTrace();
            return null;
        }
    }

    public static PublicKey generatePublicKey(byte[] data) {
        try {
            System.out.println("Try to generate public key");
            System.out.println("Data length: " + data.length);
            for (byte b : data) {
                System.out.print(b + " ");
            }
            System.out.println();

            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(data);
            KeyFactory factory = KeyFactory.getInstance("RSA");

            return factory.generatePublic(x509EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return false;
        }
    }

    public static boolean areKeysPair(PrivateKey privateKey, PublicKey publicKey) {
        try {
            // Generate a random message
            String message = "Test message";
            byte[] messageBytes = message.getBytes();

            // Sign the message using the private key
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(messageBytes);
            byte[] signedMessage = signature.sign();

            // Verify the signature using the public key
            signature.initVerify(publicKey);
            signature.update(messageBytes);
            return signature.verify(signedMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
