package com.sgu.caro.api_connection;

import com.sgu.caro.socket_connection.DataSocket;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.json.JSONObject;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.springframework.http.MediaType;

import org.springframework.web.client.RestTemplate;

public class APIConnection {

    private static final String username = "socket@caro.com";
    private static final String password = "socket123!@#";
    public static final String HOST = "http://172.104.108.31:8080";
    public static final String authAPIURL = HOST + "/caro_api/authAdmin";
    public static final String getUserByUsernameAPIURL = HOST + "/caro_api/users/";
    public static final String getUserListAPIURL = HOST + "/caro_api/users";
    public static final String postMatchAPIURL = HOST + "/caro_api/matches";
    public static final String getRatingScoreAPIURL = HOST + "/caro_api/rating/score";
    public static final String getRatingWinRateAPIURL = HOST + "/caro_api/rating/win_rate";
    public static final String getRatingWinLengthAPIURL = HOST + "/caro_api/rating/win_length";
    public static final String getRatingShortestMatchAPIURL = HOST + "/caro_api/rating/shortest_matches";
    public static final String getRatingLongestMatchAPIURL = HOST + "/caro_api/rating/longest_matches";
    public static final String patchBlockUserAPIURL = HOST + "/caro_api/block_user/";
    public static final String patchUnblockUserAPIURL = HOST + "/caro_api/unblock_user/";
    public static final String patchRejectMatchAPIURL = HOST + "/caro_api/reject_invite/";
    private static String jwt = "";

    public APIConnection() {

    }

    public void getJWT() {
        DataSocket datasocket = new DataSocket();

        JSONObject authAdminObject = new JSONObject();
        authAdminObject.put("username", username);
        authAdminObject.put("password", password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(authAdminObject.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        String resultRestTemplate = restTemplate.postForObject(authAPIURL, request, String.class);
        JSONObject user = datasocket.importData(resultRestTemplate);
        this.jwt = user.getString("access_token");
    }

    public JSONObject callGetAPI(String URL) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", jwt);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> resultRestTemplate = restTemplate.exchange(URL, HttpMethod.GET, entity, String.class);

        DataSocket datasocket = new DataSocket();
        JSONObject jsonObj = datasocket.importData(resultRestTemplate.getBody());
        return jsonObj;
    }

    public JSONArray callGetListAPI(String URL) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", jwt);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> resultRestTemplate = restTemplate.exchange(URL, HttpMethod.GET, entity, String.class);

        DataSocket datasocket = new DataSocket();
        JSONArray jsonObj = datasocket.importDataList(resultRestTemplate.getBody());
        return jsonObj;
    }

    public void callPatchAPI(String URL) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .method("PATCH", BodyPublishers.ofString(""))
                    .header("Authorization", jwt)
                    .header("Content-Type", "application/json")
                    .build();
            
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException ex) {
            Logger.getLogger(APIConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(APIConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
