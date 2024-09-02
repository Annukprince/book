package com.example.book;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;

@SpringBootApplication
public class BookApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}

	@Bean
	CommandLineRunner init() {
		return args -> {
			SecretKey secretKey = Jwts.SIG.HS256.key().build();
			System.out.println("Secret key: " + DatatypeConverter.printHexBinary(secretKey.getEncoded()));
		};
	}

}
