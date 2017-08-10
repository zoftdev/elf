package com.rmv.mse.microengine.exampleproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan(basePackages = "com.rmv.mse.microengine")
public class ExampleProjectApplication {
//	@Configuration
//	static class Config{
//		//create service
//		@Bean
//		public ExampleService service(){
//			return new ExampleService();
//		}
//
//	}


	public static void main(String[] args) {
		SpringApplication.run(ExampleProjectApplication.class, args);
	}
}
