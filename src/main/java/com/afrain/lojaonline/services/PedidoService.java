package com.afrain.lojaonline.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afrain.lojaonline.domain.Pedido;
import com.afrain.lojaonline.repositories.PedidoRepository;
import com.afrain.lojaonline.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;

	public Pedido buscar(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! id:" + id + "Tipo: " + Pedido.class.getName()));
	}
}
