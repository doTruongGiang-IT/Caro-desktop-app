package com.sgu.caro;

import com.sgu.caro.GUI.Login.Login;
import com.sgu.caro.GUI.MatchScreen.ResultMatchScreen;
import com.sgu.caro.api_connection.AESEncryption;
import java.io.IOException;

public class CaroAppClientApplication {

    public static void main(String[] args) {
        try {
            AESEncryption encryption = new AESEncryption();
            
            encryption.getRSAPublic();
            encryption.genAESKey();
            new Login();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
