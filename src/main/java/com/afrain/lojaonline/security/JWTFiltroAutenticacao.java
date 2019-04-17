package com.afrain.lojaonline.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.afrain.lojaonline.dto.CredenciaisDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTFiltroAutenticacao extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private JWTUtil jwtUtil;

	public JWTFiltroAutenticacao(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	// TENTA AUTENTICAR LOGIN
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse respose)
			throws AuthenticationException {

		try {
			CredenciaisDTO credDTO = new ObjectMapper().readValue(request.getInputStream(), CredenciaisDTO.class);

			UsernamePasswordAuthenticationToken autencicacaoToken = new UsernamePasswordAuthenticationToken(
					credDTO.getEmail(), credDTO.getSenha(), new ArrayList<>());
			
			// VERIFICA SE USUARIO E SENHA SAO VALIDOS
			Authentication autenticacao = authenticationManager.authenticate(autencicacaoToken); 
			return autenticacao;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// AUTENTICACAO OCORRIDA COM SUCESSO
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		String nomeUsuario = ((UserSS) auth.getPrincipal()).getUsername();
		String token = jwtUtil.geraToken(nomeUsuario);
		response.addHeader("Authorization", "bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
	}
}
