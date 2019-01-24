package com.afrain.lojaonline.resources;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.afrain.lojaonline.security.JWTUtil;
import com.afrain.lojaonline.security.UserSS;
import com.afrain.lojaonline.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired
	private JWTUtil jwtUtil;

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/atualiza_token", method = RequestMethod.POST)
	public ResponseEntity<Void> atualizaToken(HttpServletResponse response) {
		UserSS usuario = UserService.retornaUsuarioLogado();
		String token = jwtUtil.geraToken(usuario.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		return ResponseEntity.noContent().build();
	}
}
