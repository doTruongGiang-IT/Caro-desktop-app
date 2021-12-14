package com.sgu.caro.api_connection;

public class TokenManager {

    private static String jwt;
    private static int user_id;
    private static String display_name;
    private static int score;
    private static final String HOST = "http://139.177.184.82:8080";

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

    public static String getDisplay_name() {
        return display_name;
    }

    public static void setDisplay_name(String display_name) {
        TokenManager.display_name = display_name;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        TokenManager.score = score;
    }

    public static String getHOST() {
        return HOST;
    }
    
}
