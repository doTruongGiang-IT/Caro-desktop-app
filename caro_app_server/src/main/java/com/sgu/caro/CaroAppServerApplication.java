package com.sgu.caro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.sgu.caro.socket_connection.SocketConnection;

@SpringBootApplication
public class CaroAppServerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CaroAppServerApplication.class, args);
        SocketConnection socket = new SocketConnection();
        socket.startConnection();
    }

}
