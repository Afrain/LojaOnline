package com.afrain.lojaonline.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Value("${jwt.segredo}")
	private String segredo;

	@Value("${jwt.expira}")
	private Long expira;

	public String geraToken(String nomeUsuario) {
		return Jwts.builder().setSubject(nomeUsuario).setExpiration(new Date(System.currentTimeMillis() + expira))
				.signWith(SignatureAlgorithm.HS512, segredo.getBytes()).compact();
	}

	public boolean tokenValido(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			String nomeUsuario = claims.getSubject();
			Date expiracaoData = claims.getExpiration();
			Date dataAtual = new Date(System.currentTimeMillis());
			if (nomeUsuario != null && expiracaoData != null && dataAtual.before(expiracaoData)) {
				return true;
			}
		}
		return false;
	}
	
	public String getNomeUsuario(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(segredo.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}
	}

}
