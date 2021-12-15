package com.sgu.caro.logging;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class Logging {

    private static final String HOST = "https://log-api.newrelic.com/log/v1?Api-Key=ca38b594e69c0a8339bbdc10fee93560FFFFNRAL";
    public static final String SOCKET_TYPE = "SOCKET";
    public static final String MATCH_TYPE = "MATCH";
    private static final String OPTION = "LOGGING";

    public static void log(String message_type, String action_type, String message) {
        if (OPTION.equals("LOGGING")) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        JSONObject loggingObj = new JSONObject();
                        loggingObj.put("message_type", message_type);
                        loggingObj.put("action_type", action_type);
                        loggingObj.put("message", message);
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity<String> request = new HttpEntity<>(loggingObj.toString(), headers);
                        RestTemplate restTemplate = new RestTemplate();
                        String resultRestTemplate = restTemplate.postForObject(HOST, request, String.class);
                    } catch (final HttpClientErrorException httpClientErrorException) {
                        System.out.println(httpClientErrorException.getResponseBodyAsString());
                    } catch (HttpServerErrorException httpServerErrorException) {
                        System.out.println(httpServerErrorException);
                    } catch (JSONException | RestClientException exception) {
                        System.out.println(exception);
                    }

                }
            });
            thread.start();
        } else {
            System.out.println("[" + message_type + "]" + "[" + action_type + "]: " + message);
        }
    }
}
