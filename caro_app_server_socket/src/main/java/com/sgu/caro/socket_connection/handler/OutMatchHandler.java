package com.sgu.caro.socket_connection.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import org.json.JSONObject;

public class OutMatchHandler {
    public void run(JSONObject data, BufferedReader in, BufferedWriter out){
        GoMatchHandler.userQueue.remove(Integer.valueOf(data.getInt("user")));
    }
}
