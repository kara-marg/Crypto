package ssl;
import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLClient {
    public static void main(String[] args) throws Exception {
        // Ініціалізація SSL контексту
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            public X509Certificate[] getAcceptedIssuers() { return null; }
        }}, null);

        // Створення SSL клієнтського сокету
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        SSLSocket socket = (SSLSocket) socketFactory.createSocket("localhost", 8000);


        socket.close();
    }
}
