package com.alien.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableConfigServer
@SpringCloudApplication
@EnableEurekaClient     //注册config服务到eureka
public class AlienConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlienConfigApplication.class, args);
	}

}
