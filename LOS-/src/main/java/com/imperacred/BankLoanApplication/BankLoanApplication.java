package com.imperacred.BankLoanApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.imperacred.BankLoanApplication.config.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties.class)
public class BankLoanApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankLoanApplication.class, args);
		
	}
}
