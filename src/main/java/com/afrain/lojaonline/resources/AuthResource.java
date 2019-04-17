package com.afrain.lojaonline.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.afrain.lojaonline.dto.EmailDTO;
import com.afrain.lojaonline.security.JWTUtil;
import com.afrain.lojaonline.security.UserSS;
import com.afrain.lojaonline.services.AuthService;
import com.afrain.lojaonline.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService authService;

	@RequestMapping(value="/atualiza_token", method = RequestMethod.POST)
	public ResponseEntity<Void> atualizaToken(HttpServletResponse response) {
		UserSS usuario = UserService.retornaUsuarioLogado();
		String token = jwtUtil.geraToken(usuario.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/recupera_senha", method = RequestMethod.POST)
	public ResponseEntity<Void> recuperacaoSenha(@Valid @RequestBody EmailDTO emailDTO) {
		authService.enviaNovaSenha(emailDTO.getEmail());
		return ResponseEntity.noContent().build();
	}
	
}
