package com.sgu.caro.socket_connection.handler;

import com.sgu.caro.exception.ResourceNotFoundException;
import com.sgu.caro.repository.UserRepository;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;
import com.sgu.caro.socket_connection.handler.AcceptPairingHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;
import java.util.Queue;
import com.sgu.caro.entity.User;
import org.json.JSONObject;

public class GoMatchHandler {
    private static Queue<Integer> userQueue = new LinkedList<>();
    private static DataSocket datasocket = new DataSocket();
    private static UserRepository userRepository;
    
    public void run(JSONObject data, BufferedReader in, BufferedWriter out){
        userQueue.add(data.getInt("user"));
    }
    
    public void getPair(){
        while (true){
            if (userQueue.size() >= 2){
                int user_id_1 = userQueue.remove();
                int user_id_2 = userQueue.remove();
                
                User user1 = userRepository.findById(Long.valueOf(user_id_1)).orElseThrow(() -> new ResourceNotFoundException("User not found"));
                User user2 = userRepository.findById(Long.valueOf(user_id_2)).orElseThrow(() -> new ResourceNotFoundException("User not found"));
                
                String display_name_1 = user1.getName();
                String display_name_2 = user2.getName();
                
                int score_1 = user1.getScore();
                int score_2 = user2.getScore();
                
                Map <String, Socket> userList = new SocketConnection().getSocketClients();
                Socket socketUser1 = userList.get(String.valueOf(user_id_1));
                Socket socketUser2 = userList.get(String.valueOf(user_id_2));
                
                try {
                    BufferedWriter outUser1 = new BufferedWriter(new OutputStreamWriter(socketUser1.getOutputStream()));
                    BufferedWriter outUser2 = new BufferedWriter(new OutputStreamWriter(socketUser2.getOutputStream()));
                    
                    String dataSendUser1 = datasocket.exportDataSendInvitation(user_id_2, display_name_2, score_2);
                    String dataSendUser2 = datasocket.exportDataSendInvitation(user_id_1, display_name_1, score_1);
                    
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
