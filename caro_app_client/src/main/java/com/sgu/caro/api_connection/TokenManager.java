package com.sgu.caro.api_connection;

public class TokenManager {

    private static String jwt;
    private static int user_id;

    public static String getJwt() {
        return jwt;
    }

    public static void setJwt(String jwt) {
        TokenManager.jwt = jwt;
    }

    public static int getUser_id() {
        return user_id;
    }

    public static void setUser_id(int user_id) {
        TokenManager.user_id = user_id;
    }
}
