package com.alien.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker         // 开启断路器功能，进行容错管理
@EnableDiscoveryClient        // 开启服务发现功能
public class AliencustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AliencustomerApplication.class, args);
	}

}
