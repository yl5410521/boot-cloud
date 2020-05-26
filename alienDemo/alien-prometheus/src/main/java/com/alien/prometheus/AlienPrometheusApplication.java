package com.alien.prometheus;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@ComponentScan("com.alien.prometheus.meter")
@SpringBootApplication
public class AlienPrometheusApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlienPrometheusApplication.class, args);
	}

	@Value("${spring.application.name}")
	private String applicationName;

	MeterRegistryCustomizer<MeterRegistry> metricscommonTags(){
		return  registry -> registry.config().commonTags("application",applicationName);
	}
}
