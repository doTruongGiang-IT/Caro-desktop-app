package com.sgu.caro;

import com.sgu.caro.api_connection.APIConnection;
import com.sgu.caro.socket_connection.SocketConnection;

public class CaroAppServerApplication {
        
    public static void main(String[] args) {
        new APIConnection().getJWT();
        SocketConnection socket = new SocketConnection();
        socket.startConnection();
    }

}
