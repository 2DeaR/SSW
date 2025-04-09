package com.example.lab8;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestLab8Application {

	public static void main(String[] args) {
		SpringApplication.from(Lab8Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
