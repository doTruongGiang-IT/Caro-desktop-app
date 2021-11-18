package com.sgu.caro.socket_connection;

import com.sgu.caro.socket_connection.handler.GoStepHandler;
import com.sgu.caro.socket_connection.handler.SendMessageHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;


/**
 * 
 * @attr state: Danh sách trạng thái của các handler
 * @attr events: Danh sách thread xử lý các handler
 * @method SocketConnection(): Khởi chạy socket connection
 * @method stopConnection(): Dừng socket connection
 * @method listenConnectionBase(): Xử lý handler (hàm base) 
 * @method listenConnection(): khởi chạy handler event (lưu ý: override lại SocketHandler)
 * @method stopEvent(): dừng event cụ thể
 * @method getState(): Lấy danh sách trại thái của các handler
 */
public class SocketConnection {
    private static ServerSocket server = null;
    private static String socketHost = "localhost";    
    private static int socketPort = 5000;
    private static Map <String, Socket> socketClients = new HashMap<String, Socket>();

    public SocketConnection() {}
    
    public void startConnection(){
        try {
            server = new ServerSocket(socketPort); 
            System.out.println("===== Started socket  =====");

            while (true){
                Socket socket = server.accept();
                System.out.println(socket);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String userID = in.readLine(); 
                System.out.println(userID);
                socketClients.put(userID, socket);
                
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handleClient(socket, in, out);
                    }
                });  
                thread.start();
            }
        } catch (IOException e) { System.err.println(e); }
    }
    
    public void handleClient(Socket socket, BufferedReader in, BufferedWriter out){
        try {
            DataSocket dataSocket = new DataSocket();
            while (true){
                String rawDateReceive = in.readLine();
                System.out.println(rawDateReceive);
                JSONObject dataReceive = dataSocket.importData(rawDateReceive);
                JSONObject data = dataReceive.getJSONObject("data");
                String type = dataReceive.getString("type");

                switch (type) {
                    case "go_step":
                        System.out.println("go_step");
                        new GoStepHandler().run(data, in, out);
                        break;
                    case "send_message":
                        System.out.println("send_message");
                        new SendMessageHandler().run(data, in, out);
                        break;
                    case "stop":
                        System.out.println("July");
                        in.close();
                        out.close();
                        socket.close();
                        break;
                }
            }
        } catch (IOException e) { System.err.println(e); }
    }
    
    public void stopConnection(){
        try {
            server.close();
            System.out.println("===== Closed socket =====");
        } catch (IOException e) { System.err.println(e); }
    }
    
    public void updateSocketClients(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    Map <String, Socket> userList = new SocketConnection().getSocketClients();

                    for (Map.Entry<String, Socket> e : userList.entrySet()) {
                        Socket socketClient = e.getValue();
                        if (socketClient.isClosed()){
                            socketClients.remove(e.getKey());
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {System.out.println(e);}
                }
            }
        });  
        thread.start();
    }

    public static Map<String, Socket> getSocketClients() {
        return socketClients;
    }
    
    public static void main(String[] args) {}
}