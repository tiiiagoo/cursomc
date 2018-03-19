package com.tgs.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tgs.cursomc.domain.Categoria;
import com.tgs.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;

	public Categoria buscar(Long id) {
		Optional<Categoria> categoria = repository.findById(id);
		return categoria.orElse(null);
	}
}
