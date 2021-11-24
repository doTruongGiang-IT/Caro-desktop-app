package com.sgu.caro;

import com.sgu.caro.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CaroAppServerApplication {
    
    UserRepository userRepository;
        
    public static void main(String[] args) {
        SpringApplication.run(CaroAppServerApplication.class, args);
    }

}
