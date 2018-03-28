package com.tgs.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.tgs.cursomc.domain.Cliente;
import com.tgs.cursomc.domain.enums.TipoCliente;
import com.tgs.cursomc.dto.ClientePostDTO;
import com.tgs.cursomc.repositories.ClienteRepository;
import com.tgs.cursomc.resources.exception.FieldMessage;
import com.tgs.cursomc.services.validation.utils.ValidaCPFeCNPJ;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClientePostDTO> {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClientePostDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		if(objDto.getTipoCliente().equals(TipoCliente.PESSOA_FISICA.getCod()) && !ValidaCPFeCNPJ.isValidCpf(objDto.getCpfCnpj())) {
			list.add(new FieldMessage("cpfCnpj", "CPF inválido"));
		}
		
		if(objDto.getTipoCliente().equals(TipoCliente.PESSOA_JURIDICA.getCod()) && !ValidaCPFeCNPJ.isValidCnpj(objDto.getCpfCnpj())) {
			list.add(new FieldMessage("cpfCnpj", "CNPJ inválido"));
		}
		
		Cliente cliente = clienteRepository.findByEmail(objDto.getEmail());
		if(cliente != null) {
			list.add(new FieldMessage("email", "Email ja existente"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
