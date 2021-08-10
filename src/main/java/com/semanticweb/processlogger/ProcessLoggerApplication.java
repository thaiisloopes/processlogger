package com.semanticweb.processlogger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
@SpringBootApplication
@Configuration
public class ProcessLoggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessLoggerApplication.class, args);
	}
}
