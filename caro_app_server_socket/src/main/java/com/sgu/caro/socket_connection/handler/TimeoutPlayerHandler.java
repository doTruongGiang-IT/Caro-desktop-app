package com.sgu.caro.socket_connection.handler;

import com.sgu.caro.api_connection.APIConnection;
import com.sgu.caro.api_connection.DataAPI;
import com.sgu.caro.entity.Group;
import com.sgu.caro.socket_connection.DataSocket;
import com.sgu.caro.socket_connection.SocketConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class TimeoutPlayerHandler {
    public void run(JSONObject data, BufferedReader in, BufferedWriter out) {
        try {
            int user_1 = data.getInt("user_1");
                Group group = new AcceptPairingHandler().getGroup(user_1);
                if (group != null){
                DataSocket dataSocket = new DataSocket();
                Map<String, Socket> userList = new SocketConnection().getSocketClients();
                String dataSend;
                int result;
                AcceptPairingHandler acceptPairingHandler = new AcceptPairingHandler();

                int user_id_1 = group.getUser_1();
                int user_id_2 = group.getUser_2();
                Socket socket_1 = userList.get(Integer.toString(user_id_1));
                Socket socket_2 = userList.get(Integer.toString(user_id_2));

                if (user_1 == user_id_1) {
                    dataSend = dataSocket.exportResultMatch(user_id_2, null, null);
                    result = user_id_2;
                } else {
                    dataSend = dataSocket.exportResultMatch(user_id_1, null, null);
                    result = user_id_1;
                }

                BufferedWriter out_socket_1 = new BufferedWriter(new OutputStreamWriter(socket_1.getOutputStream()));
                out_socket_1.write(dataSend);
                out_socket_1.newLine();
                out_socket_1.flush();

                BufferedWriter out_socket_2 = new BufferedWriter(new OutputStreamWriter(socket_2.getOutputStream()));
                out_socket_2.write(dataSend);
                out_socket_2.newLine();
                out_socket_2.flush();

                for (int watcher_id : group.getWatchers()) {
                    Socket socket_client = userList.get(Integer.toString(watcher_id));
                    BufferedWriter out_client = new BufferedWriter(new OutputStreamWriter(socket_client.getOutputStream()));
                    out_client.write(dataSend);
                    out_client.newLine();
                    out_client.flush();
                }

                new AcceptPairingHandler().removeGroup(user_1);

                group.setEnd_date(LocalDateTime.now());
                DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

                DataAPI dataAPI = new DataAPI();

                int result_type = 2;
                String start_date = dateFormater.format(group.getStart_date());
                String end_date = dateFormater.format(group.getEnd_date());

                String requestData = dataAPI.exportMatchAPI(user_id_1, user_id_2, result, result_type, start_date, end_date);
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request;
                try {
                    request = HttpRequest.newBuilder()
                            .uri(new URI(APIConnection.postMatchAPIURL))
                            .headers("Content-Type", "application/json;charset=UTF-8")
                            .POST(HttpRequest.BodyPublishers.ofString(requestData))
                            .build();
                    HttpResponse response;
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    JSONObject responseData = dataAPI.importData(response.body().toString());
                } catch (URISyntaxException ex) {
                    Logger.getLogger(GoStepHandler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GoStepHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Create Match with" + requestData);
            }
        } catch (IOException ex) {
            Logger.getLogger(OutMatchPlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
