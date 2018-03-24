package com.tgs.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tgs.cursomc.domain.Categoria;
import com.tgs.cursomc.repositories.CategoriaRepository;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;

	public Categoria find(Long id) throws ObjectNotFoundException {
		Optional<Categoria> categoria = repository.findById(id);
		return categoria.orElseThrow(()-> new ObjectNotFoundException(
							"Objeto não encontrado Id: "+ id +" , Tipo: "+ Categoria.class.getName()));
	}

	public Categoria insert(Categoria obj) {	
		obj.setId(null);
		return repository.save(obj);
	}

	public Categoria update(Categoria obj) throws ObjectNotFoundException {	
		find(obj.getId());
		return repository.save(obj);
	}
}
