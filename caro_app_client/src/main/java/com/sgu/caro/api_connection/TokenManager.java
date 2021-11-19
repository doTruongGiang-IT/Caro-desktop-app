package com.sgu.caro.api_connection;

public class TokenManager {
    private static String jwt;

    public static String getJwt() {
        return jwt;
    }

    public static void setJwt(String jwt) {
        TokenManager.jwt = jwt;
    }
}
