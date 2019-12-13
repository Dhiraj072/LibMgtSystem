package com.github.dhiraj072.LibMgtSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Profile("!IntegrationTest")
@SpringBootApplication
public class LibMgtSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(LibMgtSystemApplication.class, args);
	}

}
