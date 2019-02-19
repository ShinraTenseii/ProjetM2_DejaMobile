package com.example.thibault.apptest;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

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
