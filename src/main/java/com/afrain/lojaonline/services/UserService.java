package com.afrain.lojaonline.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.afrain.lojaonline.security.UserSS;

public class UserService {

	//RETORNA USUARIO LOGADO
	public static UserSS retornaUsuarioLogado() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}

}
