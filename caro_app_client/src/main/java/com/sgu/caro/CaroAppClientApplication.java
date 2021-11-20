package com.sgu.caro;

import com.sgu.caro.GUI.Login.Login;
import java.io.IOException;

public class CaroAppClientApplication {

    public static void main(String[] args) {
        try {
            new Login();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
