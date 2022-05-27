package com.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataMigrationApplication {

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(DataMigrationApplication.class, args)));
	}

}
