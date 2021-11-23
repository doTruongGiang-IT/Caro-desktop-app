package com.sgu.caro.socket_connection.handler;

import com.sgu.caro.api_connection.APIConnection;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;
import org.springframework.web.client.RestTemplate;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class GoMatchHandler {
    public static ArrayList<Integer> userQueue = new ArrayList<>();
    private static DataSocket datasocket = new DataSocket();
    private static final String getUserByUsernameURL = "http://localhost:8080/caro_api/users/";
    private static APIConnection apiConnection = new APIConnection();
            
    public void run(JSONObject data, BufferedReader in, BufferedWriter out){
        userQueue.add(data.getInt("user"));
        JSONObject user1 = apiConnection.callGetAPI(getUserByUsernameURL + data.getInt("user"));
        System.out.println(user1.toString());
        System.out.println(userQueue.size());
    }
    
    public void getPair(){
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(GoMatchHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (userQueue.size() >= 2){
                int user_id_1 = userQueue.remove(0);
                int user_id_2 = userQueue.remove(0);
                
                JSONObject user1 = apiConnection.callGetAPI(getUserByUsernameURL + user_id_1);
                JSONObject user2 = apiConnection.callGetAPI(getUserByUsernameURL + user_id_2);
                
                System.out.println(user1.toString());
                System.out.println(user2.toString());
                
                String display_name_1 = user1.getString("firstName") + user1.getString("lastName");
                String display_name_2 = user2.getString("firstName") + user2.getString("lastName");
                
                int score_1 = user1.getInt("score");
                int score_2 = user2.getInt("score");
                
                Map <String, Socket> userList = new SocketConnection().getSocketClients();
                Socket socketUser1 = userList.get(String.valueOf(user_id_1));
                Socket socketUser2 = userList.get(String.valueOf(user_id_2));
                
                try {
                    BufferedWriter outUser1 = new BufferedWriter(new OutputStreamWriter(socketUser1.getOutputStream()));
                    BufferedWriter outUser2 = new BufferedWriter(new OutputStreamWriter(socketUser2.getOutputStream()));
                    
                    String dataSendUser1 = datasocket.exportDataSendInvitation(user_id_2, display_name_2, score_2);
                    String dataSendUser2 = datasocket.exportDataSendInvitation(user_id_1, display_name_1, score_1);
                    
                    System.out.println(dataSendUser1);
                    System.out.println(dataSendUser2);
                    
                    outUser1.write(dataSendUser1);
                    outUser1.newLine();
                    outUser1.flush();
                    
                    outUser2.write(dataSendUser2);
                    outUser2.newLine();
                    outUser2.flush();
                
                    new AcceptPairingHandler().addGroup(user_id_1, user_id_2);
                } catch (IOException ex) {
                    Logger.getLogger(GoMatchHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
