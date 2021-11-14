package com.sgu.caro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CaroAppServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaroAppServerApplication.class, args);
	}

}
