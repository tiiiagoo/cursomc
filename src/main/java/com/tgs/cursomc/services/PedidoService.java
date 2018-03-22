package com.tgs.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tgs.cursomc.domain.Pedido;
import com.tgs.cursomc.repositories.PedidoRepository;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repository;

	public Pedido buscar(Long id) {
		Optional<Pedido> pedido = repository.findById(id);
		return pedido.orElse(null);
	}
}
