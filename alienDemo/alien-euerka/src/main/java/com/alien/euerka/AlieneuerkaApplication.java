package com.alien.euerka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
@EnableAutoConfiguration
public class AlieneuerkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlieneuerkaApplication.class, args);
	}

}
