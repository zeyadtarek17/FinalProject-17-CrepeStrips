package com.crepestrips.fooditemservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit

public class FoodItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodItemServiceApplication.class, args);
	}

}
