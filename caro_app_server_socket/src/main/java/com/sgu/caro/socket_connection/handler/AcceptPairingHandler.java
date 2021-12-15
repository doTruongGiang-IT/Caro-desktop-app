package com.sgu.caro.socket_connection.handler;

import com.sgu.caro.api_connection.APIConnection;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;
import com.sgu.caro.entity.Group;
import com.sgu.caro.entity.User;
import com.sgu.caro.logging.Logging;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import java.lang.Math;
import java.time.LocalDateTime;

public class AcceptPairingHandler {

    private static DataSocket datasocket = new DataSocket();

    static public ArrayList<Group> groups = new ArrayList<>();

    public void run(JSONObject data, BufferedReader in, BufferedWriter out) {

        int userId = data.getInt("user");
        boolean is_accepted = data.getBoolean("is_accepted");
        Group group = getGroup(userId);
        
        if (group == null){
            return ;
        }
        
        String dataSend1, dataSend2;
        boolean is_success = false;
        if (is_accepted) {
            group.setAccept_pairing_1(userId, true);
            group.setAccept_pairing_2(userId, true);

            if (group.isAccept_pairing_1() && group.isAccept_pairing_2()) {
                dataSend1 = datasocket.exportDataStartMatch(true, "X");
                dataSend2 = datasocket.exportDataStartMatch(true, "O");
                is_success = true;
                new GoStepHandler().addMatrix(group);
            } else {
                return;
            }
        } else {
            dataSend1 = datasocket.exportDataStartMatch(false, "");
            dataSend2 = datasocket.exportDataStartMatch(false, "");
            removeGroup(userId);
        }
        
        int user_id_1 = group.getUser_1();
        int user_id_2 = group.getUser_2();

        if (is_success){
            group.setWho_x(user_id_1);
            APIConnection apiConnection = new APIConnection();
            JSONObject user1 = apiConnection.callGetAPI(apiConnection.getUserByUsernameAPIURL + user_id_1);
            JSONObject user2 = apiConnection.callGetAPI(apiConnection.getUserByUsernameAPIURL + user_id_2);
            group.setDataUser1(new User(
                    user_id_1, 
                    user1.getString("username"), 
                    user1.getString("firstName"),
                    user1.getString("lastName"),
                    user1.getInt("score")
            ));
            
            group.setDataUser2(new User(
                    user_id_2, 
                    user2.getString("username"), 
                    user2.getString("firstName"),
                    user2.getString("lastName"),
                    user2.getInt("score")
            ));
            group.setStart_date(LocalDateTime.now());
                
        }
        
        Map<String, Socket> userList = new SocketConnection().getSocketClients();
        Socket socketUser1 = userList.get(String.valueOf(user_id_1));
        Socket socketUser2 = userList.get(String.valueOf(user_id_2));

        try {
            BufferedWriter outUser1 = new BufferedWriter(new OutputStreamWriter(socketUser1.getOutputStream()));
            BufferedWriter outUser2 = new BufferedWriter(new OutputStreamWriter(socketUser2.getOutputStream()));
            Logging.log(Logging.SOCKET_TYPE, "socket_send", dataSend1);
            outUser1.write(dataSend1);
            outUser1.newLine();
            outUser1.flush();

            outUser2.write(dataSend2);
            outUser2.newLine();
            outUser2.flush();
            
        } catch (IOException ex) {
            Logger.getLogger(GoMatchHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Group getGroup(int userId) {
        for (Group g : groups) {
            if (g.getUser_1() == userId || g.getUser_2() == userId || g.getWatchers().contains(userId)) {
                return g;
            }
        }
        return null;
    }

    public void addGroup(int userId1, int userId2) {
        Group group = new Group(userId1, userId2, new ArrayList<>());
        groups.add(group);
    }
    
    public void removeGroup(int userId){
        for (Group g : groups) {
            if (g.getUser_1() == userId || g.getUser_2() == userId) {
                groups.remove(g);
                break;
            }
        }
    }
}
