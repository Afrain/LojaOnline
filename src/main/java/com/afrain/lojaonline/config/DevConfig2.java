package com.afrain.lojaonline.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.afrain.lojaonline.services.DBService;

@Configuration
@Profile("dev")
public class DevConfig2 {

	@Autowired
	private DBService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;

	@Bean
	public boolean instanciaDataBase() throws ParseException {

		if (!"create".equals(strategy)) {
			return false;
		}
		dbService.instaciaDataBase();

		return true;
	}

}
