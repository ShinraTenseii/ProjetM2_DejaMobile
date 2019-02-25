package com.example.thibault.apptest;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;
import java.security.cert.Certificate;

public class MainActivity extends AppCompatActivity {

    public static final String CERTIFICATE=("-----BEGIN CERTIFICATE-----\n" +
            "MIIDBjCCAe4CCQDIKeq+lMpuujANBgkqhkiG9w0BAQsFADBFMQswCQYDVQQGEwJG\n" +
            "UjETMBEGA1UECAwKU29tZS1TdGF0ZTEhMB8GA1UECgwYSW50ZXJuZXQgV2lkZ2l0\n" +
            "cyBQdHkgTHRkMB4XDTE5MDIxOTE3NTgyMFoXDTE5MDMyMTE3NTgyMFowRTELMAkG\n" +
            "A1UEBhMCRlIxEzARBgNVBAgMClNvbWUtU3RhdGUxITAfBgNVBAoMGEludGVybmV0\n" +
            "IFdpZGdpdHMgUHR5IEx0ZDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\n" +
            "AKjDFnYmZ2uqOr4YCgDqZpfY3JJwIdHN5it0ekvJF/rJivpjFNrRLE/i8L2eGoVn\n" +
            "UOlyCA5U+yZ04NQqDG9mPBfZNiPRGVIhz43qsmTKts6dGh4MhUCjzCT/ROz0Wlrd\n" +
            "IosIWDdBXJJbPMn5ednOdMxP5ggWqyx1B1hQH34RpKNcR5pBdIeoPxP49/PiRsGy\n" +
            "UYdvQtVUz5GD/CnSZQaIHiPEQjL21eOwjdVrUx1r2SGEhA6a67FKHTkkddA4OiPg\n" +
            "rqL2o2sAjkxQmAUmmQhKsvr+Ccsn6wUngJSZLrXW+1kV+pFbZLINoQKtrex1DuXA\n" +
            "UlsEqoDw+CmIjxjDxpJFDDECAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAXxf92JQi\n" +
            "tTSd/98OgSBq82DV5XZL9cCK2p5Qj1B2zXHRqIN5no54vsnHMFw1GgsIrIBUCK41\n" +
            "nHxNJUjDUEQT8PtwfzhIuk7dA5a0ZYir7L7dCPrnARgv+ut84u6lhRiv0Sg2VLAK\n" +
            "QXNFFZ/WdFB+Q1eVUU/kVao54utsGOusPRjm5CLBBYpgSJqgesRIHMLrRo1MEOGB\n" +
            "MXY/oa/hiB4R3PJiMIhqBsabX9EmbuCh+XaBCQE8+W3VYgda2vRpTSdVjWBV2Net\n" +
            "LoRsbY1XEqKyVl/dIgZ7WO1arC3b6mQBZopXq4YxPNRq+MMDbPnNREmxGmZPB5qZ\n" +
            "GEfA+01VDOWEBA==\n" +
            "-----END CERTIFICATE-----\n");

    public static SSLSocketFactory socketf=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_dbg = findViewById(R.id.buttonGen);
        button_dbg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final TextView textDebug = findViewById(R.id.textViewDebug);
                try {
                    genKeys();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
                Enumeration<String> elements = null;
                String text = "";
                try {
                    elements = getKeys();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while(elements.hasMoreElements()){
                    text += elements.nextElement();
                    if(elements.hasMoreElements()){
                        text += " | ";
                    }
                }
                textDebug.setText(text);
            }
        });

        Button button_get = findViewById(R.id.buttonGet);
        button_get.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final TextView textPrint = findViewById(R.id.textViewPrint);
                String text = "";
                KeyStore keyStore = null;
                try {
                    keyStore = KeyStore.getInstance("AndroidKeyStore");
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                }
                try {
                    keyStore.load(null);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                }

                try {
                    PrivateKey privateKey = (PrivateKey) keyStore.getKey("Keys_RSA", null);
                    text += "Private key : " + privateKey.toString() + "\nPublic key : ";
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnrecoverableKeyException e) {
                    e.printStackTrace();
                }
                try {
                    PublicKey publicKey = keyStore.getCertificate("Keys_RSA").getPublicKey();
                    text += publicKey.toString();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                }

                textPrint.setText(text);
            }
        });

        Button button_tls = findViewById(R.id.buttonTLS);
        button_tls.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final TextView textTLS = findViewById(R.id.textViewTLS);

                textTLS.setText("Trying...");
                // Load CAs from an InputStream
                // (could be from a resource or ByteArrayInputStream or ...)
                /*InputStream in=null;
                try {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");

                    Certificate ca;

                    in = new ByteArrayInputStream(CERTIFICATE.getBytes());

                    ca = cf.generateCertificate(in);

                    // Create a KeyStore containing our trusted CAs
                    String keyStoreType = KeyStore.getDefaultType();
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(null, null);
                    keyStore.setCertificateEntry("ca", ca);

                    // Create a TrustManager that trusts the CAs in our KeyStore
                    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                    tmf.init(keyStore);

                    // Create an SSLContext that uses our TrustManager
                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, tmf.getTrustManagers(), null);
                    socketf=context.getSocketFactory();
                    SSLSocket socket = (SSLSocket) socketf.createSocket();
                    socket.connect(new InetSocketAddress("10.0.2.2",1599), 5000);*/

                Socket socket = null;
                try {
                    socket = new Socket("10.0.2.2",1599);
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                textTLS.setText("Done");

                /*}catch (Exception e){
                    e.printStackTrace();
                    socketf=null;
                }
                finally {
                    if (in!=null)
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }*/
            }
        });
    }

    protected void genKeys() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");
        kpg.initialize(new KeyGenParameterSpec.Builder(
                "Keys_EC",
                KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                .setDigests(KeyProperties.DIGEST_SHA256,
                        KeyProperties.DIGEST_SHA512)
                .build());
        KeyPair kp = kpg.generateKeyPair();

        kpg = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
        kpg.initialize(
                new KeyGenParameterSpec.Builder(
                        "Keys_RSA",
                        KeyProperties.PURPOSE_DECRYPT)
                        .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                        .build());
        KeyPair kp2 = kpg.generateKeyPair();
    }

    protected Enumeration<String> getKeys() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
        ks.load(null);
        Enumeration<String> aliases = ks.aliases();
        return aliases;
    }



}
