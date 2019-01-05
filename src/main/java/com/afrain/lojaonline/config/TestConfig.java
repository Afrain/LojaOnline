package com.afrain.lojaonline.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.afrain.lojaonline.services.DBService;
import com.afrain.lojaonline.services.EmailService;
import com.afrain.lojaonline.services.MockEmailService;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private DBService dbService;

	@Bean
	public boolean instanciaDataBase() throws ParseException {

		dbService.instaciaDataBase();

		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}

}
