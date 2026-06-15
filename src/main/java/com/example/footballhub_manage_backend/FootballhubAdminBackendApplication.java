package com.example.footballhub_manage_backend;

import com.example.lib.auth.EnableAuth;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAuth
public class FootballhubAdminBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballhubAdminBackendApplication.class, args);
	}

}
