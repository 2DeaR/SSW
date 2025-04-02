package com.example.demo777;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestDemo777Application {

	public static void main(String[] args) {
		SpringApplication.from(Demo777Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
