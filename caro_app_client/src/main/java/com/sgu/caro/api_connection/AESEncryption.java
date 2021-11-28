package com.sgu.caro.api_connection;

import com.sgu.caro.GUI.MatchScreen.Cell;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;
import com.sgu.caro.socket_connection.SocketHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.util.Base64;
import java.util.Random;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AESEncryption {

    private static String RSAPublicAPI = "";
    private static String RSAPublicSocket = "";
    private static String SECRET_KEY = "863bff2d32acf9f003221b6e492e0884";
    private static String encryptionMode = "AES/CBC/PKCS5Padding";
    private static SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
    private APIConnection apiConnection = new APIConnection();

    public void getRSAPublic() {
        JSONObject dataResponse = apiConnection.callGetPublicKeyAPI();
        RSAPublicAPI = dataResponse.getString("public_key");
    }
    
    public void addRSAPublicKeySocketHandle(){
        SocketConnection socket = new SocketConnection();
        DataSocket dataSocket = new DataSocket();
        
        socket.addListenConnection("public_key", new SocketHandler() {
            @Override
            public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
                RSAPublicSocket = data.getString("public_key");
                socket.sendData(dataSocket.exportDataAESKey(RSAEcryptSocket(SECRET_KEY)));
            }
        });
    }

    public String RSAEcryptAPI(String plaintext) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(RSAPublicAPI.getBytes());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey;
            publicKey = keyFactory.generatePublic(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] byteEncrypted = cipher.doFinal(plaintext.getBytes());
            String encrypted = Base64.getEncoder().encodeToString(byteEncrypted);
            return encrypted;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String RSAEcryptSocket(String plaintext) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(RSAPublicSocket.getBytes());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey;
            publicKey = keyFactory.generatePublic(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] byteEncrypted = cipher.doFinal(plaintext.getBytes());
            String encrypted = Base64.getEncoder().encodeToString(byteEncrypted);
            return encrypted;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getSecretKey() {
        return RSAEcryptAPI(SECRET_KEY);
    }

    public void genAESKey() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < 32) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        SECRET_KEY = sb.toString().substring(0, 32);
    }

    public String encrypt(String plaintext) {
        try {
            Cipher cipher = Cipher.getInstance(encryptionMode);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] byteEncrypted;
            byteEncrypted = cipher.doFinal(plaintext.getBytes());

            String encrypted = Base64.getEncoder().encodeToString(byteEncrypted);

            return encrypted;
        } catch (InvalidKeyException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String decrypt(String encrypted) {
        try {
            Cipher cipher = Cipher.getInstance(encryptionMode);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] byteDecrypted = cipher.doFinal(encrypted.getBytes());
            String decrypted = new String(byteDecrypted);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
