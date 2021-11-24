package com.sgu.caro.socket_connection.handler;

import com.sgu.caro.api_connection.APIConnection;
import com.sgu.caro.entity.User;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetInfoHandler {

    public void getGroup() {
        while (true) {
            DataSocket dataSocket = new DataSocket();

            String dataSend = dataSocket.exportDataGetGroup(AcceptPairingHandler.groups);

            Map<String, Socket> userList = new SocketConnection().getSocketClients();

            AcceptPairingHandler acceptPairingHandler = new AcceptPairingHandler();
            
            for (Map.Entry<String, Socket> e : userList.entrySet()) {
                if (acceptPairingHandler.getGroup(Integer.valueOf(e.getKey())) == null){
                    try {
                        Socket socketClient = e.getValue();
                        BufferedWriter outClient = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));;

                        outClient.write(dataSend);
                        outClient.newLine();
                        outClient.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(GetInfoHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GetInfoHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getUser() {
        while (true) {
            JSONArray data = new APIConnection().callGetListAPI(APIConnection.getUserListAPIURL);
            ArrayList<User> users = new ArrayList<>();

            SocketConnection socketConnection = new SocketConnection();
            ArrayList<Integer> activeUsers = new ArrayList<>();
            for(String key: socketConnection.getSocketClients().keySet()){
                activeUsers.add(Integer.valueOf(key));
            }

            for (int i = 0; i < data.length(); i++) {
                JSONObject element = (JSONObject) data.get(i);
                int id = element.getInt("id");
                    if (activeUsers.contains(id)){
                    String username = element.getString("username");
                    String firstName = element.getString("firstName");
                    String lastName = element.getString("lastName");
                    int score = element.getInt("score");
                    users.add(new User(id, username, firstName, lastName, score));
                }
            }

            DataSocket dataSocket = new DataSocket();
            String dataSend = dataSocket.exportDataGetUser(users);
            Map<String, Socket> userList = new SocketConnection().getSocketClients();
            
            AcceptPairingHandler acceptPairingHandler = new AcceptPairingHandler();
            for (Map.Entry<String, Socket> e : userList.entrySet()) {
                if (acceptPairingHandler.getGroup(Integer.valueOf(e.getKey())) == null){
                    try {
                        Socket socketClient = e.getValue();
                        BufferedWriter outClient = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));;

                        outClient.write(dataSend);
                        outClient.newLine();
                        outClient.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(GetInfoHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GetInfoHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
