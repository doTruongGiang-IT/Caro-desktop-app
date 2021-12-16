package com.sgu.caro.socket_connection.handler;

import com.sgu.caro.api_connection.APIConnection;
import com.sgu.caro.entity.Group;
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
    private static APIConnection apiConnection = new APIConnection();
    
    public void getGroup() {
        while (true) {
            DataSocket dataSocket = new DataSocket();

            String dataSend = dataSocket.exportDataGetGroup(AcceptPairingHandler.groups);

            Map<String, Socket> userList = new SocketConnection().getSocketClients();

            AcceptPairingHandler acceptPairingHandler = new AcceptPairingHandler();
            
            for (Map.Entry<String, Socket> e : userList.entrySet()) {
                Group group = acceptPairingHandler.getGroup(Integer.valueOf(e.getKey()));
                if (group == null){
                    try {
                        Socket socketClient = e.getValue();
                        BufferedWriter outClient = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));

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
                        BufferedWriter outClient = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));

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
    
    public void getWatcher() {
        while (true) {
            DataSocket dataSocket = new DataSocket();
            
            Map<String, Socket> userList = new SocketConnection().getSocketClients();

            AcceptPairingHandler acceptPairingHandler = new AcceptPairingHandler();
            
            for (Group group : acceptPairingHandler.groups){
                if (group != null && group.isAccept_pairing_1() && group.isAccept_pairing_2()){
                    try {
                        ArrayList <String> watchers = new ArrayList<>();
                        for (int watcher_id : group.getWatchers()){
                            JSONObject watcher = apiConnection.callGetAPI(apiConnection.getUserByUsernameAPIURL + watcher_id);
                            watchers.add(watcher.getString("firstName") + " " + watcher.getString("lastName"));
                        }
                        String dataSend = dataSocket.exportDataGetWatcher(watchers);

                        int user_1 = group.getUser_1();
                        int user_2 = group.getUser_2();
                        Socket socket_1 = userList.get(Integer.toString(user_1));
                        Socket socket_2 = userList.get(Integer.toString(user_2));

                        BufferedWriter out_socket_1 = new BufferedWriter(new OutputStreamWriter(socket_1.getOutputStream()));
                        out_socket_1.write(dataSend);
                        out_socket_1.newLine();
                        out_socket_1.flush();

                        BufferedWriter out_socket_2 = new BufferedWriter(new OutputStreamWriter(socket_2.getOutputStream()));
                        out_socket_2.write(dataSend);
                        out_socket_2.newLine();
                        out_socket_2.flush();

                        for (int watcher_id : group.getWatchers()){
                            Socket socket_client = userList.get(Integer.toString(watcher_id));
                            if (socket_client != null){
                                BufferedWriter out_client = new BufferedWriter(new OutputStreamWriter(socket_client.getOutputStream()));
                                out_client.write(dataSend);
                                out_client.newLine();
                                out_client.flush();
                            }
                        }

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
