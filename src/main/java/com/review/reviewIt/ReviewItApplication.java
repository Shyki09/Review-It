package com.review.reviewIt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan
@EnableTransactionManagement
public class ReviewItApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewItApplication.class, args);
	}

}
