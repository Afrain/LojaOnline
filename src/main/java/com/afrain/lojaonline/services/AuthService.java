package com.afrain.lojaonline.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.afrain.lojaonline.domain.Cliente;
import com.afrain.lojaonline.repositories.ClienteRepository;
import com.afrain.lojaonline.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private EmailService emailService;

	private Random random = new Random();

	public void enviaNovaSenha(String email) {

		Cliente cliente = clienteRepository.findByEmail(email);

		if (cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado!");
		}

		String novaSenha = geraNovaSenha();
		cliente.setSenha(bCryptPasswordEncoder.encode(novaSenha));

		clienteRepository.save(cliente);
		emailService.enviaNovaSenhaEmail(cliente, novaSenha);

	}

	private String geraNovaSenha() {
		char[] vet = new char[10];
		for (int i = 0; i < 10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = random.nextInt(3);
		if (opt == 0) {								// GERA UM DIGITO
			return (char) (random.nextInt(10) + 48);
		} else if (opt == 1) {						// GERA LETRA MAIUSCULA
			return (char) (random.nextInt(26) + 65);
		} else {									// GERA LETRA MINUSCULA
			return (char) (random.nextInt(26) + 97);
		}

	}

}
