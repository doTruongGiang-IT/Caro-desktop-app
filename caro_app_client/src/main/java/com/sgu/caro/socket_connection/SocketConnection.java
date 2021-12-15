package com.sgu.caro.socket_connection;

import com.sgu.caro.GUI.MainScreen.MainScreenDesign;
import com.sgu.caro.api_connection.TokenManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private static Socket socket = null;
    private static BufferedReader in = null;
    private static BufferedWriter out = null;
    private static String socketHost = "localhost";    
    private static int socketPort = 5000;
    private static Map <String, SocketHandler> actions = new HashMap<String, SocketHandler>();
    private static ArrayList<Thread> events = new ArrayList<Thread>();

    public SocketConnection() {}
    
    public void startConnection(){
        try {
            socket = new Socket(socketHost, socketPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("===== Connected to server =====");
            System.out.println(String.valueOf(TokenManager.getUser_id()));
            sendData(String.valueOf(TokenManager.getUser_id()));
            
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    handleServer(socket, in, out);
                }
            });  
            thread.start();
        } catch (IOException e) { System.err.println(e); }
    }
    
    public void handleServer(Socket socket, BufferedReader in, BufferedWriter out){
        try {
            DataSocket dataSocket = new DataSocket();
            while (true){
                String rawDateReceive = in.readLine();
                System.out.println("Receive" + rawDateReceive);
                JSONObject dataReceive = dataSocket.importData(rawDateReceive);
                JSONObject data = dataReceive.getJSONObject("data");
                String type = dataReceive.getString("type");

                switch (type) {
                    case "go_step":
                        System.out.println("go_step");
                        actions.get("go_step").onHandle(data, in, out);
                        break;
                    case "result_match":
                        System.out.println("result_match");
                        actions.get("result_match").onHandle(data, in, out);
                        break;
                    case "send_message":
                        System.out.println("send_message");
                        actions.get("send_message").onHandle(data, in, out);
                        break;
                    case "send_invitation":
                        System.out.println("send_invitation");
                        actions.get("send_invitation").onHandle(data, in, out);
                        break;
                    case "start_match":
                        System.out.println("start_match");
                        actions.get("start_match").onHandle(data, in, out);
                        break;
                    case "get_group":
                        System.out.println("get_group");
                        if (actions.get("get_group") != null)
                            actions.get("get_group").onHandle(data, in, out);
                        break;
                    case "get_user":
                        System.out.println("get_user");
                        if (actions.get("get_user") != null)
                            actions.get("get_user").onHandle(data, in, out);
                        break;
                    case "accept_watch":
                        System.out.println("accept_watch");
                        actions.get("accept_watch").onHandle(data, in, out);
                        break;
                    case "get_watcher":
                        System.out.println("get_watcher");
                        actions.get("get_watcher").onHandle(data, in, out);
                        break;
                    case "watch_achievement":
                        System.out.println("watch_achievement");
                        actions.get("watch_achievement").onHandle(data, in, out);
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
    
    public void addListenConnection(String actionID, SocketHandler handler){
        actions.put(actionID, handler);
    }
            
    public void stopConnection(){
        try {
            in.close();
            out.close();
            socket.close();
            System.out.println("===== Closed connection to server =====");
        } catch (IOException e) { System.err.println(e); }
    }
    
    
        
   public void sendData(String data){
        try {
            out.write(data);
            out.newLine();
            out.flush();
        } catch (IOException e) { System.err.println(e); }
    }
    
    public static void main(String[] args) {}
}
