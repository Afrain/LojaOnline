package com.afrain.lojaonline.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.afrain.lojaonline.domain.Cliente;
import com.afrain.lojaonline.domain.enums.TipoCliente;
import com.afrain.lojaonline.dto.ClienteNewDTO;
import com.afrain.lojaonline.repositories.ClienteRepository;
import com.afrain.lojaonline.resources.exception.FieldMessage;
import com.afrain.lojaonline.services.validation.utils.BR;

public class ClienteInsertValidation implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("CpfOuCnpj", "CPF inválido!"));
		}

		if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("CpfOuCnpj", "CNPJ inválido!"));
		}

		Cliente aux = clienteRepository.findByEmail(objDto.getEmail());
		if (aux != null) {
			list.add(new FieldMessage("Email", "Email já existente"));
		}

		Cliente aux2 = clienteRepository.findByCpfOuCnpj(objDto.getCpfOuCnpj());
		if (aux2 != null && aux.getTipo().equals(TipoCliente.PESSOAFISICA)) {
			list.add(new FieldMessage("CpfOuCnpj", "CPF já cadastrado!"));
		}  
		
		if (aux2 != null && aux.getTipo().equals(TipoCliente.PESSOAJURIDICA)) {
			list.add(new FieldMessage("CpfOuCnpj", "CNPJ já cadastrado!"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}