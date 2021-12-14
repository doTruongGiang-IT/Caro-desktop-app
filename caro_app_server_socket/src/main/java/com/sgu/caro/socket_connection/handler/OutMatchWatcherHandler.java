package com.sgu.caro.socket_connection.handler;

import com.sgu.caro.entity.Group;
import com.sgu.caro.entity.User;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class OutMatchWatcherHandler {
    public void run(JSONObject data, BufferedReader in, BufferedWriter out){        
        int user = data.getInt("user");
        int user_1 = data.getInt("user_1");
        
        Group group = new AcceptPairingHandler().getGroup(user_1);
        if (group != null){
            group.delWatch(user);
        }
    }
}
