package com.sgu.caro.socket_connection.handler;

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

public class SendMessageHandler {
    
    public void run(JSONObject data, BufferedReader in, BufferedWriter out){
        Map <String, Socket> userList = new SocketConnection().getSocketClients();
        DataSocket dataSocket = new DataSocket();
        
        for (Map.Entry<String, Socket> e : userList.entrySet()) {
            try {
                Socket socketClient = e.getValue();
                BufferedWriter outClient = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));

                int userClientID = data.getInt("user");
                String message = data.getString("message");
                String dataSend = dataSocket.exportDataSendMessage(userClientID, message);
            
                outClient.write(dataSend);
                outClient.newLine();
                outClient.flush();
            } catch (IOException ex) {
                Logger.getLogger(SendMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
