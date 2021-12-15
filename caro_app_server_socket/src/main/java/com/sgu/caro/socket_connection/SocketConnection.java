package com.sgu.caro.socket_connection;

import com.sgu.caro.socket_connection.handler.AcceptPairingHandler;
import com.sgu.caro.socket_connection.handler.EndMatchHandler;
import com.sgu.caro.socket_connection.handler.ExitGameHandler;
import com.sgu.caro.socket_connection.handler.GoStepHandler;
import com.sgu.caro.socket_connection.handler.SendMessageHandler;
import com.sgu.caro.socket_connection.handler.GoMatchHandler;
import com.sgu.caro.socket_connection.handler.OutMatchHandler;
import com.sgu.caro.socket_connection.handler.GetInfoHandler;
import com.sgu.caro.socket_connection.handler.GoWatchHandler;
import com.sgu.caro.socket_connection.handler.OutMatchWatcherHandler;
import com.sgu.caro.socket_connection.handler.OutMatchPlayerHandler;
import com.sgu.caro.socket_connection.handler.TimeoutPlayerHandler;
import com.sgu.caro.socket_connection.handler.TimeoutMatchHandler;
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
import com.sgu.caro.logging.Logging;

/**
 *
 * @attr state: Danh sách trạng thái của các handler
 * @attr events: Danh sách thread xử lý các handler
 * @method SocketConnection(): Khởi chạy socket connection
 * @method stopConnection(): Dừng socket connection
 * @method listenConnectionBase(): Xử lý handler (hàm base)
 * @method listenConnection(): khởi chạy handler event (lưu ý: override lại
 * SocketHandler)
 * @method stopEvent(): dừng event cụ thể
 * @method getState(): Lấy danh sách trại thái của các handler
 */
public class SocketConnection {

    private static ServerSocket server = null;
    private static String socketHost = "172.104.108.31";
    private static int socketPort = 5000;
    public static Map<String, Socket> socketClients = new HashMap<String, Socket>();

    public SocketConnection() {
    }

    public void startConnection() {
        try {
            server = new ServerSocket(socketPort);
            System.out.println("===== Socket server has started =====");
            Logging.log(Logging.SOCKET_TYPE, "socket_start", "===== Socket server has started =====");

            Thread thread_go_match = new Thread(new Runnable() {
                @Override
                public void run() {
                    new GoMatchHandler().getPair();
                }
            });
            thread_go_match.start();

            Thread thread_get_group = new Thread(new Runnable() {
                @Override
                public void run() {
                    new GetInfoHandler().getGroup();
                }
            });
            thread_get_group.start();

            Thread thread_get_user = new Thread(new Runnable() {
                @Override
                public void run() {
                    new GetInfoHandler().getUser();
                }
            });
            thread_get_user.start();
            
            Thread thread_get_watcher = new Thread(new Runnable() {
                @Override
                public void run() {
                    new GetInfoHandler().getWatcher();
                }
            });
            thread_get_watcher.start();

            while (true) {
                Socket socket = server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String userID = in.readLine();
                Logging.log(Logging.SOCKET_TYPE, "user_access", "user " + userID + " accessed");
                
                if (!socketClients.containsKey(userID)){
                    socketClients.put(userID, socket);

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handleClient(userID, socket, in, out);
                        }
                    });
                    thread.start();
                }
            }

        } catch (IOException e) {
//            System.err.println(e);
        }
    }

    public void handleClient(String userID, Socket socket, BufferedReader in, BufferedWriter out) {
        try {
            DataSocket dataSocket = new DataSocket();

            while (true) {
                String rawDateReceive = in.readLine();
                Logging.log(Logging.SOCKET_TYPE, "socket_received", "Received: " + rawDateReceive);
                JSONObject dataReceive = dataSocket.importData(rawDateReceive);
                JSONObject data = dataReceive.getJSONObject("data");
                String type = dataReceive.getString("type");

                switch (type) {
                    case "go_step":
                        Logging.log(Logging.SOCKET_TYPE, "socket_type", "go_step");
                        new GoStepHandler().run(data, in, out);
                        break;
                    case "send_message":
                        Logging.log(Logging.SOCKET_TYPE, "socket_type", "send_message");
                        new SendMessageHandler().run(data, in, out);
                        break;
                    case "go_match":
                        Logging.log(Logging.SOCKET_TYPE, "socket_type", "go_match");
                        new GoMatchHandler().run(data, in, out);
                        break;
                    case "out_match":
                        Logging.log(Logging.SOCKET_TYPE, "socket_type", "out_match");
                        new OutMatchHandler().run(data, in, out);
                        break;
                    case "accept_pariring":
                        Logging.log(Logging.SOCKET_TYPE, "socket_type", "accept_pariring");
                        new AcceptPairingHandler().run(data, in, out);
                        break;
                    case "end_match":
                        Logging.log(Logging.SOCKET_TYPE, "socket_type", "end_match");
                        new EndMatchHandler().run(data, in, out);
                        break;
                    case "go_watch":
                        Logging.log(Logging.SOCKET_TYPE, "socket_type", "go_watch");
                        new GoWatchHandler().run(data, in, out);
                        break;
                    case "out_match_watcher":
                        Logging.log(Logging.SOCKET_TYPE, "socket_type", "out_match_watcher");
                        new OutMatchWatcherHandler().run(data, in, out);
                        break;
                    case "out_match_player":
                        Logging.log(Logging.SOCKET_TYPE, "socket_type", "out_match_player");
                        new OutMatchPlayerHandler().run(data, in, out);
                        break;
                    case "timeout_player":
                        Logging.log(Logging.SOCKET_TYPE, "socket_type", "timeout_player");
                        new TimeoutPlayerHandler().run(data, in, out);
                        break;
                    case "timeout_match":
                        Logging.log(Logging.SOCKET_TYPE, "socket_type", "timeout_match");
                        new TimeoutMatchHandler().run(data, in, out);
                        break;
                    case "exit_game":
                        Logging.log(Logging.SOCKET_TYPE, "socket_type", "exit_game");
                        new ExitGameHandler().run(data, in, out);
                        break;
                    case "stop":
                        in.close();
                        out.close();
                        socket.close();
                        break;
                }
            }
        } catch (IOException e) {
            Logging.log(Logging.SOCKET_TYPE, "user_disconnect", userID);
            new AcceptPairingHandler().removeGroup(Integer.valueOf(userID));
            GoMatchHandler.userQueue.remove(Integer.valueOf(userID));
            socketClients.remove(userID);
//            System.err.println(e);
        }
    }

    public void stopConnection() {
        try {
            server.close();
            Logging.log(Logging.SOCKET_TYPE, "socket_close", "===== Closed socket =====");
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void updateSocketClients() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Map<String, Socket> userList = new SocketConnection().getSocketClients();
                    System.out.println(userList.size());

                    for (Map.Entry<String, Socket> e : userList.entrySet()) {
                        Socket socketClient = e.getValue();
                        if (socketClient.isClosed()) {
                            socketClients.remove(e.getKey());
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
            }
        });
        thread.start();
    }

    public static Map<String, Socket> getSocketClients() {
        return socketClients;
    }

    public static void main(String[] args) {
    }
}
