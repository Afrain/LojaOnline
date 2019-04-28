package com.afrain.lojaonline.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afrain.lojaonline.domain.Cidade;
import com.afrain.lojaonline.repositories.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	public List<Cidade> buscaCidadePorEstado(Integer estado_id){
		return cidadeRepository.findCidades(estado_id);
	}
}
