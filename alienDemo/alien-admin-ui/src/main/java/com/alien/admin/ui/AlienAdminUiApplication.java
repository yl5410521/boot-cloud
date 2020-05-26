package com.alien.admin.ui;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableAdminServer
@SpringBootApplication
public class AlienAdminUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlienAdminUiApplication.class, args);
	}

}
