package ssl;


import org.bouncycastle.x509.X509V3CertificateGenerator;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.cert.*;
import java.util.Date;
import javax.net.ssl.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Random;
import javax.security.auth.x500.X500Principal;

public class SSLServer {
    public static void main(String[] args) throws Exception {
        // Генерація ключа та сертифіката
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        X509Certificate certificate = generateSelfSignedCertificate(keyPair, "CN=Example");

        // Ініціалізація SSL контексту
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }}, null);

        // Створення SSL серверного сокету
        SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
        SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(8000);
        serverSocket.setNeedClientAuth(true);

        // Очікування підключення клієнта
        SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
        System.out.println("Клієнт підключений.");

        // Закриття з'єднання
        clientSocket.close();
        serverSocket.close();
    }

    // Метод для генерації  сертифіката
    // була підключена залежність в maven
    public static X509Certificate generateSelfSignedCertificate(KeyPair keyPair, String subjectDN) throws NoSuchAlgorithmException, CertificateException, InvalidKeyException, SignatureException {
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Створення сертифікату
        X509Certificate certificate = null;
        try {
            // Встановлення дати початку та закінчення дії сертифіката
            Date startDate = new Date();
            Date expiryDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000); // 1 рік

            // Генерація сертифіката
            X500Principal owner = new X500Principal(subjectDN);
            BigInteger serialNumber = new BigInteger(64, new Random());

            X509V3CertificateGenerator certGenerator = new X509V3CertificateGenerator();
            certGenerator.setSerialNumber(serialNumber);
            certGenerator.setIssuerDN(owner);
            certGenerator.setSubjectDN(owner);
            certGenerator.setPublicKey(publicKey);
            certGenerator.setNotBefore(startDate);
            certGenerator.setNotAfter(expiryDate);
            certGenerator.setSignatureAlgorithm("SHA256withRSA");

            // Підпис сертифіката власним приватним ключем
            certificate = certGenerator.generate(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return certificate;
    }
}
