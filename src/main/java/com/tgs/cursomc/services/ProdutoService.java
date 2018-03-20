package com.tgs.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tgs.cursomc.domain.Produto;
import com.tgs.cursomc.repositories.ProdutoRepository;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repository;

	public Produto buscar(Long id) {
		Optional<Produto> produto = repository.findById(id);
		return produto.orElse(null);
	}
}
