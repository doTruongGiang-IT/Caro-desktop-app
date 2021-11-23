package com.sgu.caro.socket_connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public abstract class SocketHandler {
    public void onHandle(BufferedReader in, BufferedWriter out) {}
}