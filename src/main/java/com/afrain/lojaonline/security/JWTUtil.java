package com.afrain.lojaonline.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Value("${jwt.segredo}")
	private String segredo;
	
	@Value("${jwt.expira}")
	private Long expira;
	
	public String geraToken(String nomeUsuario) {
		return Jwts.builder()
				.setSubject(nomeUsuario)
				.setExpiration(new Date(System.currentTimeMillis() + expira))
				.signWith(SignatureAlgorithm.HS512, segredo.getBytes())
				.compact();
	}
	
}
