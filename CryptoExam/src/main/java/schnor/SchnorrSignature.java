package schnor;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SchnorrSignature {


    // Генерує приватний ключ
    public static BigInteger generatePrivateKey() {
        SecureRandom secureRandom = new SecureRandom();
        return new BigInteger(256, secureRandom);
    }

    // Вираховує публічний ключ за допомогою приватного ключа
    public static BigInteger generatePublicKey(BigInteger privateKey) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] privateKeyBytes = privateKey.toByteArray();
        byte[] publicKeyBytes = sha256.digest(privateKeyBytes);
        return new BigInteger(1, publicKeyBytes);
    }

    // Підписує повідомлення за допомогою приватного ключа
    public static Signature sign(String message, BigInteger privateKey, BigInteger publicKey) throws NoSuchAlgorithmException {
//        Спочатку використовується алгоритм хешування SHA-256
//        для обчислення хешу повідомлення. Повідомлення перетворюється
//        у байтовий масив, і потім застосовується хеш-функція до цього масиву.
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] messageBytes = message.getBytes();
        byte[] hashBytes = sha256.digest(messageBytes);
        BigInteger hash = new BigInteger(1, hashBytes);

        SecureRandom secureRandom = new SecureRandom();
        BigInteger k = new BigInteger(256, secureRandom);
        BigInteger r = hash.modPow(k, publicKey);

        BigInteger s = k.subtract(privateKey.multiply(r)).mod(publicKey.subtract(BigInteger.ONE));

        System.out.println("s="+s);

        return new Signature(r, s);
    }

    // Перевіряє валідність підпису
    public static boolean verify(String message, BigInteger publicKey, Signature signature) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] messageBytes = message.getBytes();
        byte[] hashBytes = sha256.digest(messageBytes);
        BigInteger hash = new BigInteger(1, hashBytes);

        BigInteger w = signature.getS().modInverse(publicKey);
        BigInteger u1 = hash.multiply(w).mod(publicKey);
        BigInteger u2 = signature.getR().multiply(w).mod(publicKey);

        BigInteger v = publicKey.modPow(u1, publicKey).multiply(signature.getR().modPow(u2, publicKey)).mod(publicKey).add(signature.getR());

        System.out.println(w);
        System.out.println(v);
        System.out.println(signature.getR());
        return v.equals(signature.getR());
    }
}
