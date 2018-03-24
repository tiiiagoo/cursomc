package com.tgs.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tgs.cursomc.domain.Cliente;
import com.tgs.cursomc.repositories.ClienteRepository;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;

	public Cliente find(Long id) throws ObjectNotFoundException {
		Optional<Cliente> cliente = repository.findById(id);
		return cliente.orElseThrow(()-> new ObjectNotFoundException(
							"Objeto não encontrado Id: "+ id +" , Tipo: "+ Cliente.class.getName()));
	}
}
