package com.sgu.caro.api_connection;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class DataAPI {

    public DataAPI() {
    }

    public String encryptData(String rawData) {
        return rawData;
    }

    /**
     * # data format { "username": email@email.com, # email người dùng
     * "password": mypassword # mật khẩu người dùng }
     *
     * @param username
     * @param password
     * @return String
     */
    public String exportLoginAPI(String username, String password) {
        JSONObject jo = new JSONObject();

        jo.put("username", username);
        jo.put("password", password);
        return encryptData(jo.toString());
    }

    public JSONObject importData(String rawData) {
        return new JSONObject(rawData);
    }

    public JSONObject getInfoUserByID(int userID, String jwt) {
        JSONObject user = null;
        String url = TokenManager.getHOST() + "/caro_api/users/" + userID;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url)).headers("Authorization", jwt).GET()
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            user = new JSONObject(response.body().toString());
            //System.out.println(response.body().toString());
        } catch (URISyntaxException e1) {
        } catch (IOException e2) {
        } catch (InterruptedException e2) {
        }
        return user;
    }

    public static void main(String[] args) {
    }
}
