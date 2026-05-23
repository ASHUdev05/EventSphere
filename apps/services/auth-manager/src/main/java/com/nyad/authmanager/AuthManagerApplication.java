package com.nyad.authmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AuthManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthManagerApplication.class, args);
	}

}
