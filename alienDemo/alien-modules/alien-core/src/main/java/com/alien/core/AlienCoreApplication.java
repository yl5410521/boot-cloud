package com.alien.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients(basePackages={"com.alien"})
public class AlienCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlienCoreApplication.class, args);
	}

}
