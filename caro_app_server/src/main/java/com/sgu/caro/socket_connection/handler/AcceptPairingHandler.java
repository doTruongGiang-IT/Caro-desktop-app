package com.sgu.caro.socket_connection.handler;

import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;
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

class Group {

    private int user_1;
    private int user_2;
    private ArrayList<Integer> watchers;
    private boolean accept_pairing_1 = false;
    private boolean accept_pairing_2 = false;

    public Group(int user_1, int user_2, ArrayList<Integer> watchers) {
        this.user_1 = user_1;
        this.user_2 = user_2;
        this.watchers = watchers;
    }

    public int getUser_1() {
        return user_1;
    }

    public void setUser_1(int user_1) {
        this.user_1 = user_1;
    }

    public int getUser_2() {
        return user_2;
    }

    public void setUser_2(int user_2) {
        this.user_2 = user_2;
    }

    public ArrayList<Integer> getWatchers() {
        return watchers;
    }

    public void setWatchers(ArrayList<Integer> watchers) {
        this.watchers = watchers;
    }

    public boolean isAccept_pairing_1() {
        return accept_pairing_1;
    }

    public void setAccept_pairing_1(int user_1, boolean accept_pairing_1) {
        if (this.user_1 == user_1) {
            this.accept_pairing_1 = accept_pairing_1;
        }
    }

    public boolean isAccept_pairing_2() {
        return accept_pairing_2;
    }

    public void setAccept_pairing_2(int user_2, boolean accept_pairing_2) {
        if (this.user_2 == user_2) {
            this.accept_pairing_2 = accept_pairing_2;
        }
    }
}

public class AcceptPairingHandler {

    private static DataSocket datasocket = new DataSocket();

    static private ArrayList<Group> groups = new ArrayList<>();

    public void run(JSONObject data, BufferedReader in, BufferedWriter out) {

        int userId = data.getInt("user");
        boolean is_accepted = data.getBoolean("is_accepted");
        Group group = getGroup(userId);
        String dataSend;
        
        if (is_accepted) {
            group.setAccept_pairing_1(userId, true);
            group.setAccept_pairing_2(userId, true);

            if (group.isAccept_pairing_1() && group.isAccept_pairing_2()) {
                dataSend = datasocket.exportDataStartMatch(true);
            } else {
                return;
            }
        } else {
            dataSend = datasocket.exportDataStartMatch(false);
        }

        int user_id_1 = group.getUser_1();
        int user_id_2 = group.getUser_2();

        Map<String, Socket> userList = new SocketConnection().getSocketClients();
        Socket socketUser1 = userList.get(String.valueOf(user_id_1));
        Socket socketUser2 = userList.get(String.valueOf(user_id_2));

        try {
            BufferedWriter outUser1 = new BufferedWriter(new OutputStreamWriter(socketUser1.getOutputStream()));
            BufferedWriter outUser2 = new BufferedWriter(new OutputStreamWriter(socketUser2.getOutputStream()));

            outUser1.write(dataSend);
            outUser1.newLine();
            outUser1.flush();

            outUser2.write(dataSend);
            outUser2.newLine();
            outUser2.flush();

        } catch (IOException ex) {
            Logger.getLogger(GoMatchHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Group getGroup(int userId) {
        for (Group g : groups) {
            if (g.getUser_1() == userId || g.getUser_2() == userId) {
                return g;
            }
        }
        return null;
    }

    public void addGroup(int userId1, int userId2) {
        Group group = new Group(userId1, userId2, new ArrayList<>());
        groups.add(group);
    }
}
