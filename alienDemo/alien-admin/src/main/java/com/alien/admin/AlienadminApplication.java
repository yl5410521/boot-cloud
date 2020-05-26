package com.alien.admin;

import com.github.xiaoymin.knife4j.spring.annotations.EnableSwaggerBootstrapUi;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alien.base.repository.BaseRepositoryFactoryBean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,SecurityAutoConfiguration.class,
		ManagementWebSecurityAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaRepositories(repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)//指定自己的工厂类
@EnableTransactionManagement
@MapperScan("com.alien.admin.*.mapper")
//启用JPA审计
@EnableJpaAuditing
//启用缓存
@EnableCaching
//启用异步
@EnableAsync
@EnableSwaggerBootstrapUi
public class AlienadminApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlienadminApplication.class, args);
	}

}
