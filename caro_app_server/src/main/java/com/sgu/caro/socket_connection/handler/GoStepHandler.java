package com.sgu.caro.socket_connection.handler;

import com.sgu.caro.socket_connection.SocketConnection;
import com.sgu.caro.socket_connection.DataSocket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONObject;


class ResultMatch {
    public int userID = 0;
    public ArrayList <Integer> posX, posY = new ArrayList<>();

    public ResultMatch(int userID, ArrayList<Integer> posX, ArrayList <Integer> posY) {
        this.userID = userID;
        this.posX = posX;
        this.posY = posY;
    }
}

public class GoStepHandler {
    private static int maxX = 20;
    private static int maxY = 20;
    private static int[][] matrix = new int[maxX][maxY];
    
    public void run(JSONObject data, BufferedReader in, BufferedWriter out){
        try {
            DataSocket dataSocket = new DataSocket();
            int userID = data.getInt("user");
            int posX, posY;
            posX = data.getJSONArray("pos").getInt(0);
            posY = data.getJSONArray("pos").getInt(1);

            System.out.println("User: " + userID);
            System.out.println("posX: " + posX + ", " + "posY: " + posY);
            
            matrix[posX - 1][posY - 1] = userID;
                  
            ResultMatch resultMatch = checkMatrix(posX-1, posY-1);
            int userWin = resultMatch.userID;
            
            Map <String, Socket> userList = new SocketConnection().getSocketClients();
            
            for (Map.Entry<String, Socket> e : userList.entrySet()) {
                Socket socketClient = e.getValue();
                BufferedWriter outClient = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
                String dataSend = dataSocket.exportDataGoStep(userID, posX, posY);
                
                outClient.write(dataSend);
                System.out.println("Sending: " + dataSend);
                outClient.newLine();
                outClient.flush();
                
                if (userWin != 0){
                    dataSend = dataSocket.exportResultMatch(userWin, resultMatch.posX, resultMatch.posY);
                    outClient.write(dataSend);
                    System.out.println("Sending: " + dataSend);
                    outClient.newLine();
                    outClient.flush();
                }
            }

        } catch (IOException e) { System.err.println(e); }
    }
    
    public ResultMatch checkMatrix(int x, int y){
        int userID = matrix[x][y];
        
        int x_l = x;
        int x_r = x;
        int y_l = y;
        int y_r = y;
        int c1 = 0;
        int c2 = 0;
        int d1 = 0;
        int d2 = 0;
        
        while (x_l >= 0 && matrix[x_l][y] == userID){
            x_l--;
        }
        x_l++;
        
        while (x_r < maxX && matrix[x_r][y] == userID){
            x_r++;
        }
        x_r--;
        
        if (x_r - x_l + 1 >= 5){
            ArrayList <Integer> posX = new ArrayList<>();
            ArrayList <Integer> posY = new ArrayList<>();
            for (int i=x_l; i<=x_l + 4; i++){
                posX.add(i);
                posY.add(y);
            }
            return new ResultMatch(userID, posX, posY);
        }
        
        while (y_l >= 0 && matrix[x][y_l] == userID){
            y_l--;
        }
        y_l++;
        
        while (y_r < maxY && matrix[x][y_r] == userID){
            y_r++;
        }
        y_r--;
        
        if (y_r - y_l + 1 >= 5){
            ArrayList <Integer> posX = new ArrayList<>();
            ArrayList <Integer> posY = new ArrayList<>();
            for (int i=y_l; i<=y_l + 4; i++){
                posY.add(i);
                posX.add(x);
            }
            return new ResultMatch(userID, posX, posY);
        }
        
        while (x - c1 >= 0 && y - c1 >= 0 && matrix[x - c1][y - c1] == userID){
            c1++;
        }
        c1--;
        
        while (x + c2 < maxX && y + c2 < maxY && matrix[x + c2][y + c2] == userID){
            c2++;
        }
        c2--;
        
        if (c1 + c2 + 1>= 5){
            ArrayList <Integer> posX = new ArrayList<>();
            ArrayList <Integer> posY = new ArrayList<>();
            for (int i=0; i<=4; i++){
                posX.add(x - c1 + i);
                posY.add(y - c1 + i);
            }
            return new ResultMatch(userID, posX, posY);
        }
        
        while (x - d1 >= 0 && y + d1 < maxY && matrix[x - d1][y + d1] == userID){
            d1++;
        }
        d1--;
        
        while (x + d2 < maxX && y - d2 >= 0 && matrix[x + d2][y - d2] == userID){
            d2++;
        }
        d2--;
        
        if (d1 + d2 + 1>= 5){
            ArrayList <Integer> posX = new ArrayList<>();
            ArrayList <Integer> posY = new ArrayList<>();
            for (int i=0; i<=4; i++){
                posX.add(x - d1 + i);
                posY.add(y + d1 + i);
            }
            return new ResultMatch(userID, posX, posY);
        }
        
        return new ResultMatch(0, null, null);
    }
}
