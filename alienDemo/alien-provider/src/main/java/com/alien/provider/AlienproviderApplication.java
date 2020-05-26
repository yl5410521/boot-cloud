package com.alien.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableAutoConfiguration
@EnableEurekaClient
@EnableCircuitBreaker         // 开启断路器功能，进行容错管理
@EnableDiscoveryClient        // 开启服务发现功能
public class AlienproviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlienproviderApplication.class, args);
	}

}
