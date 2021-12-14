package com.sgu.caro.socket_connection.handler;

import com.sgu.caro.api_connection.APIConnection;
import com.sgu.caro.entity.Group;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;
import static com.sgu.caro.socket_connection.handler.AcceptPairingHandler.groups;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class SendMessageHandler {
    private static APIConnection apiConnection = new APIConnection();
    public void run(JSONObject data, BufferedReader in, BufferedWriter out) {
        Map<String, Socket> userList = new SocketConnection().getSocketClients();
        DataSocket dataSocket = new DataSocket();
        int user_id = data.getInt("user");
        Group group = new AcceptPairingHandler().getGroup(user_id);
        ArrayList <Integer> users = (ArrayList)group.getWatchers().clone();
        users.add(group.getUser_1());
        users.add(group.getUser_2());
        
        for (Map.Entry<String, Socket> e : userList.entrySet()) {
            if (users.contains(Integer.parseInt(e.getKey()))){
                try {
                    Socket socketClient = e.getValue();
                    BufferedWriter outClient = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));

                    
                    int userClientID = data.getInt("user");
                    JSONObject user = apiConnection.callGetAPI(apiConnection.getUserByUsernameAPIURL + userClientID);
                
                    String message = data.getString("message");
                    String dataSend = dataSocket.exportDataSendMessage(userClientID, user.getString("firstName") + ' ' + user.getString("lastName"), message);

                    outClient.write(dataSend);
                    outClient.newLine();
                    outClient.flush();
                } catch (IOException ex) {
                    Logger.getLogger(SendMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
