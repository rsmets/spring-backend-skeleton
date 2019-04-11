package com.skeleton.project;

import org.parse4j.Parse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBackendSkeletonApplication {

	public static void main(String[] args) {

		Parse.initialize("bLg3XZ2X8KurMX0h8hKcBvcPvJ6MUz7zq2zP3V8s", "", "localhost:1337");

		SpringApplication.run(SpringBackendSkeletonApplication.class, args);

	}
}
