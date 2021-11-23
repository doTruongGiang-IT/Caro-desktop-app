package com.sgu.caro;

import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;

import com.sgu.caro.socket_connection.SocketConnection;

//@ComponentScan("com.sgu.caro.repository")
//@Configuration
//@EnableAutoConfiguration(
//exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, JmxAutoConfiguration.class})
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CaroAppServerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CaroAppServerApplication.class, args);
        SocketConnection socket = new SocketConnection();
        socket.startConnection();
    }

}
