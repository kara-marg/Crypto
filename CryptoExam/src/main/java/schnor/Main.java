package schnor;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // Генеруємо приватний ключ
        BigInteger privateKey = SchnorrSignature.generatePrivateKey();
        // Вираховуємо публічний ключ за допомогою приватного ключа
        BigInteger publicKey = SchnorrSignature.generatePublicKey(privateKey);
        // Підписуємо повідомлення
        String message = "Hello  World!";
        Signature signature = SchnorrSignature.sign(message, privateKey, publicKey);

        // Перевіряємо валідність підпису
        boolean isValid = SchnorrSignature.verify(message, publicKey, signature);

        System.out.println("Private key: " + privateKey);
        System.out.println("Public key: " + publicKey);
        System.out.println("Signature: (r = " + signature.getR() + ", s = " + signature.getS() + ")");
        System.out.println("Signature validity: " + isValid);
    }
}
