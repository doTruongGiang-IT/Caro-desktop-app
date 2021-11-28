package com.sgu.caro.api_connection;

import com.sgu.caro.GUI.MainScreen.MainScreenDesign;
import com.sgu.caro.GUI.WindowManager;
import com.sgu.caro.socket_connection.DataSocket;
import java.awt.Color;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class APIConnection {
    public static final String HOST = "http://localhost:8080";
    public static final String getPublicKeyAPIURL = HOST + "/caro_api/public_key";
    public static final String postAuthAPIURL = HOST + "/caro_api/auth";
    public static final String postAESKeyAPIURL = HOST + "/caro_api/aes_key";
    private DataSocket dataSocket = new DataSocket();
    private DataAPI dataAPI = new DataAPI();
    private AESEncryption encryption = new AESEncryption();
    
    public JSONObject callGetPublicKeyAPI() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(getPublicKeyAPIURL))
                    .headers("Content-Type", "application/json;charset=UTF-8")
                    .GET()
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject responseData = dataSocket.importData(response.body().toString());
            return responseData;
            
        } catch (URISyntaxException e1) {
        } catch (IOException e2) {
        } catch (InterruptedException e2) {
        }
        return null;
    }

    public JSONObject callPostAuthAPI(String username, String password) {
        try {
            String requestData = dataAPI.exportLoginAPI(username, password);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(postAuthAPIURL))
                    .headers("Content-Type", "application/json;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(requestData))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject responseData = dataSocket.importData(response.body().toString());
            return responseData;
            
        } catch (URISyntaxException e1) {
        } catch (IOException e2) {
        } catch (InterruptedException e2) {
        }
        return null;
    }
    
    public JSONObject callPostAESKeyAPI(String username, String aesAESKey) {
        try {
            String requestData = dataAPI.exportAESKeyAPI(username, aesAESKey);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(postAESKeyAPIURL))
                    .headers("Content-Type", "application/json;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(requestData))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject responseData = dataSocket.importData(response.body().toString());
            return responseData;
            
        } catch (URISyntaxException e1) {
        } catch (IOException e2) {
        } catch (InterruptedException e2) {
        }
        return null;
    }
    
    public JSONObject formatData(HashMap<String, String> headers, HashMap<String, String> body){
        JSONObject jo = new JSONObject();

        jo.put("header", new JSONObject(headers));
        if (body != null){
            jo.put("data", new JSONObject(body));
        }
        return jo;
    }
    
    public JSONObject callGETAPI(String URL, HashMap<String, String> headers){
        JSONObject jo = formatData(headers, null);
            
        String encryptData = encryption.encrypt(jo.toString());
        
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(getPublicKeyAPIURL))
                    .headers("Content-Type", "application/json;charset=UTF-8")
                    .headers("encrypted_data", encryptData)
                    .GET()
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject responseData = dataSocket.importData(response.body().toString());
            return responseData;
            
        } catch (URISyntaxException e1) {
        } catch (IOException e2) {
        } catch (InterruptedException e2) {
        }
        return null;
    }
    
    public JSONObject callPOSTAPI(String URL, HashMap<String, String> headers, HashMap<String, String> body){
        JSONObject jo = formatData(headers, body);
            
        String encryptData = encryption.encrypt(jo.toString());
        
        try {
            String requestData = dataAPI.exportAPIData(encryptData);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(postAESKeyAPIURL))
                    .headers("Content-Type", "application/json;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(requestData))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject responseData = dataSocket.importData(response.body().toString());
            return responseData;
            
        } catch (URISyntaxException e1) {
        } catch (IOException e2) {
        } catch (InterruptedException e2) {
        }
        return null;
    }
}
