package com.sgu.caro.socket_connection.handler;

import com.sgu.caro.entity.Group;
import com.sgu.caro.entity.User;
import com.sgu.caro.logging.Logging;
import com.sgu.caro.socket_connection.DataSocket;
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

public class GoWatchHandler {
    public void run(JSONObject data, BufferedReader in, BufferedWriter out){
        int user = data.getInt("user");
        int user_1 = data.getInt("user_1");
        
        Group group = new AcceptPairingHandler().getGroup(user_1);
        
        if (group != null){
            try {
                group.addWatcher(user);
                
                User dataUser1 = group.getDataUser1();
                User dataUser2 = group.getDataUser2();
                
                String username_1 = dataUser1.getName();
                int score_1 = dataUser1.getScore();
                
                int user_2 = dataUser2.getId();
                String username_2 = dataUser2.getName();
                int score_2 = dataUser2.getScore();
                
                int who_x;
                if (user_1 == group.getWho_x()){
                    who_x = 1;
                }
                else{
                    who_x = 2;
                }
                
                Matrix matrix = new GoStepHandler().matrixGoStep.get(group.toString());
                
                SocketConnection socket = new SocketConnection();
                DataSocket dataSocket = new DataSocket();
                String dataSend = dataSocket.exportDataAcceptWatch(true, user_1, username_1, score_1, user_2, username_2, score_2, who_x, matrix.matrix);
                out.write(dataSend);
                Logging.log(Logging.SOCKET_TYPE, "socket_send", dataSend);
                out.newLine();
                out.flush();
            } catch (IOException ex) {
                Logger.getLogger(GoWatchHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
