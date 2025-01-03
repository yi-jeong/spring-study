package com.jeong.simplex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SimplexApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplexApplication.class, args);
	}

}
