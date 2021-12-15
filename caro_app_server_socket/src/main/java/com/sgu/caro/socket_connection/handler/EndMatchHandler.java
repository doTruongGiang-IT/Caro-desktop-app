package com.sgu.caro.socket_connection.handler;

import com.sgu.caro.logging.Logging;
import com.sgu.caro.socket_connection.SocketConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class EndMatchHandler {

    public void run(JSONObject data, BufferedReader in, BufferedWriter out) {

        int user_1 = data.getInt("user_1");
        new AcceptPairingHandler().removeGroup(user_1);
        Logging.log(Logging.MATCH_TYPE, "end_match", Integer.toString(user_1));
    }
}
