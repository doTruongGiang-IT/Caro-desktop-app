package com.sgu.caro.socket_connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import org.json.JSONObject;

public abstract class SocketHandler {
    public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {}
}
