package in.gov.nmc.rangmach.utility;


import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import in.gov.nmc.rangmach.R;

public class SSLHelper {

    public static void setupSSL(Context context) throws Exception {
        // Load the certificate from the raw folder
        try {
            InputStream certInputStream = context.getResources().openRawResource(R.raw.certificate);
            // Create a CertificateFactory
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            // Generate the certificate
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(certInputStream);

            // Create a KeyStore and add the certificate to it
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);  // Initialize with no password
            keyStore.setCertificateEntry("my_cert", certificate);

            // Initialize TrustManagerFactory with the KeyStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            // Set up SSLContext to use the custom TrustManager
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            // Set the default SSL context to use the custom one
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}