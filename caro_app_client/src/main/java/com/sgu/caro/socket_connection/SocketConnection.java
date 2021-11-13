package com.sgu.caro.socket_connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
    private static Map <String, Boolean> state = new HashMap<String, Boolean>();
    private static ArrayList<Thread> events = new ArrayList<Thread>();

    public SocketConnection() {
        try {
            socket = new Socket("localhost", 5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("===== Connected to server =====");
        } catch (IOException e) { System.err.println(e); }
    }
    
    public void stopConnection(){
        try {
            in.close();
            out.close();
            socket.close();
            System.out.println("===== Closed connection to server =====");
        } catch (IOException e) { System.err.println(e); }
    }
    
    public void listenConnectionBase(String handlerID, SocketHandler handler){
        if (state.containsKey(handlerID)){
            System.out.println("===== ERROR: handlerID is duplicated =====");
            return ;
        }
        state.put(handlerID, true);

        while(state.get(handlerID)) {
            handler.onHandle(in, out);
        }
    }
     
    public void listenConnection(String handlerID, SocketHandler handler){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                listenConnectionBase(handlerID, handler);
            }
        });  
        events.add(thread);
        thread.start();
    }
        
   public void sendData(String data){
        try {
            out.write(data);
            out.newLine();
            out.flush();
        } catch (IOException e) { System.err.println(e); }
    }
    

    public void stopEvent(String handlerID){
        state.put(handlerID, false);
    }

    public static Map<String, Boolean> getState() {
        return state;
    }
    
    public static void main(String[] args) {
        // Example 
        SocketConnection instance = new SocketConnection();
        instance.listenConnection("send_helloword", new SocketHandler(){
            public void onHandle(BufferedReader in, BufferedWriter out) {
                try {
                    String dataFromServer = in.readLine();
                    System.out.println("Receive: " + dataFromServer);
                    out.write("Hello world from client");
                    out.newLine();
                    out.flush();
                    
                    if (dataFromServer.equals("end_match")){
                        instance.stopEvent("send_helloword");
                    }
                } catch (IOException e) { System.err.println(e); }
            }
        });
    }
}
