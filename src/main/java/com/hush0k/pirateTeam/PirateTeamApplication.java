package com.hush0k.pirateTeam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class PirateTeamApplication {

	public static void main(String[] args) {
		SpringApplication.run(PirateTeamApplication.class, args);
	}

}
