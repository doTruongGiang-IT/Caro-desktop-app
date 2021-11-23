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
import java.util.HashMap;
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

class Matrix {
    public int maxX = 20;
    public int maxY = 20;
    public int[][] matrix = new int[maxX][maxY];
}

public class GoStepHandler {
    private static Map<String, Matrix> matrixGoStep = new HashMap<>();
    
    public void run(JSONObject data, BufferedReader in, BufferedWriter out){
        try {
            System.out.println(matrixGoStep.size());
            DataSocket dataSocket = new DataSocket();
            int userID = data.getInt("user");
            int posX, posY;
            posX = data.getJSONArray("pos").getInt(0);
            posY = data.getJSONArray("pos").getInt(1);

            System.out.println("User: " + userID);
            System.out.println("posX: " + posX + ", " + "posY: " + posY);
            Group group = new AcceptPairingHandler().getGroup(userID);
            
            Matrix matrixGroup = matrixGoStep.get(group.toString());
            
            matrixGroup.matrix[posX - 1][posY - 1] = userID;
                  
            ResultMatch resultMatch = checkMatrix(matrixGroup, posX-1, posY-1);
            int userWin = resultMatch.userID;
            
            Map <String, Socket> userList = new SocketConnection().getSocketClients();
            
            for (Map.Entry<String, Socket> e : userList.entrySet()) {
                if (group.inGroup(Integer.parseInt(e.getKey())) != 0){
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
            }

        } catch (IOException e) { System.err.println(e); }
    }
    
    public ResultMatch checkMatrix(Matrix matrixGroup, int x, int y){
        int userID = matrixGroup.matrix[x][y];
        
        int x_l = x;
        int x_r = x;
        int y_l = y;
        int y_r = y;
        int c1 = 0;
        int c2 = 0;
        int d1 = 0;
        int d2 = 0;
        
        while (x_l >= 0 && matrixGroup.matrix[x_l][y] == userID){
            x_l--;
        }
        x_l++;
        
        while (x_r < matrixGroup.maxX && matrixGroup.matrix[x_r][y] == userID){
            x_r++;
        }
        x_r--;
        
        if (x_r - x_l + 1 >= 5){
            ArrayList <Integer> posX = new ArrayList<>();
            ArrayList <Integer> posY = new ArrayList<>();
            for (int i=x_l; i<=x_l + 4; i++){
                posX.add(i+1);
                posY.add(y+1);
            }
            return new ResultMatch(userID, posX, posY);
        }
        
        while (y_l >= 0 && matrixGroup.matrix[x][y_l] == userID){
            y_l--;
        }
        y_l++;
        
        while (y_r < matrixGroup.maxY && matrixGroup.matrix[x][y_r] == userID){
            y_r++;
        }
        y_r--;
        
        if (y_r - y_l + 1 >= 5){
            ArrayList <Integer> posX = new ArrayList<>();
            ArrayList <Integer> posY = new ArrayList<>();
            for (int i=y_l; i<=y_l + 4; i++){
                posY.add(i+1);
                posX.add(x+1);
            }
            return new ResultMatch(userID, posX, posY);
        }
        
        while (x - c1 >= 0 && y - c1 >= 0 && matrixGroup.matrix[x - c1][y - c1] == userID){
            c1++;
        }
        c1--;
        
        while (x + c2 < matrixGroup.maxX && y + c2 < matrixGroup.maxY && matrixGroup.matrix[x + c2][y + c2] == userID){
            c2++;
        }
        c2--;
        
        if (c1 + c2 + 1>= 5){
            ArrayList <Integer> posX = new ArrayList<>();
            ArrayList <Integer> posY = new ArrayList<>();
            for (int i=0; i<=4; i++){
                posX.add(x - c1 + i + 1);
                posY.add(y - c1 + i + 1);
            }
            return new ResultMatch(userID, posX, posY);
        }
        
        while (x - d1 >= 0 && y + d1 < matrixGroup.maxY && matrixGroup.matrix[x - d1][y + d1] == userID){
            d1++;
        }
        d1--;
        
        while (x + d2 < matrixGroup.maxX && y - d2 >= 0 && matrixGroup.matrix[x + d2][y - d2] == userID){
            d2++;
        }
        d2--;
        
        if (d1 + d2 + 1>= 5){
            ArrayList <Integer> posX = new ArrayList<>();
            ArrayList <Integer> posY = new ArrayList<>();
            for (int i=0; i<=4; i++){
                posX.add(x - d1 + i + 1);
                posY.add(y + d1 - i + 1);
            }
            return new ResultMatch(userID, posX, posY);
        }
        
        return new ResultMatch(0, null, null);
    }
    
    public void addMatrix(Group group){
        matrixGoStep.put(group.toString(), new Matrix());
    }
}
