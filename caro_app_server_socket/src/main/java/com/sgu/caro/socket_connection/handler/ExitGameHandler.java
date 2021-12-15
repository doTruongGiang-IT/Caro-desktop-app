package com.sgu.caro.socket_connection.handler;

import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;
import com.sgu.caro.entity.Group;
import com.sgu.caro.logging.Logging;
import static com.sgu.caro.socket_connection.handler.GoStepHandler.matrixGoStep;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import org.json.JSONObject;

public class ExitGameHandler {

    public void run(JSONObject data, BufferedReader in, BufferedWriter out) {
        int userID = data.getInt("user");
        String userIDString = Integer.toString(userID);
        Logging.log(Logging.MATCH_TYPE, "exit_game", userIDString);
        if (SocketConnection.socketClients.containsKey(userIDString)) {
            SocketConnection.socketClients.remove(userIDString);
        }
        Group group = new AcceptPairingHandler().getGroup(userID);
        if (group != null) {
            new AcceptPairingHandler().removeGroup(Integer.valueOf(userID));
        }
        if (GoMatchHandler.userQueue.contains(Integer.valueOf(userID))) {
            GoMatchHandler.userQueue.remove(Integer.valueOf(userID));
        }
    }
}
