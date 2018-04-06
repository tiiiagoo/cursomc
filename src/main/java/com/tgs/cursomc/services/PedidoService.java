package com.tgs.cursomc.services;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgs.cursomc.domain.ItemPedido;
import com.tgs.cursomc.domain.PagamentoComBoleto;
import com.tgs.cursomc.domain.Pedido;
import com.tgs.cursomc.domain.enums.EstadoPagamento;
import com.tgs.cursomc.repositories.ItemPedidoRepository;
import com.tgs.cursomc.repositories.PagamentoRepository;
import com.tgs.cursomc.repositories.PedidoRepository;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository 		repository;
	
	@Autowired
	private BoletoService 			boletoService;
	
	@Autowired
	private PagamentoRepository 	pagamentoRepository;
	
	@Autowired
	private ProdutoService 			produtoService;
	
	@Autowired
	private ItemPedidoRepository 	itemPedidoRepository;
	
	@Autowired
	private ClienteService 			clienteService;
	
	@Autowired
	private EmailService			emailService;

	public Pedido find(Long id) {
		Optional<Pedido> pedido = repository.findById(id);
		return pedido.orElse(null);
	}
	
	@Transactional
	public @Valid Pedido insert(Pedido pedido) throws ObjectNotFoundException {		
		pedido.setId(null);
		pedido.setDataPedido(new Date());
		pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
		pedido.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		if(pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pgtoBoleto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPgtoComBoleto(pgtoBoleto, pedido.getDataPedido());
		}
		pedido = repository.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());
		for(ItemPedido item : pedido.getItens()) {
			item.setDesconto(0.0);
			item.setProduto(produtoService.find(item.getProduto().getId()));
			item.setPreco(item.getProduto().getPreco());
			item.setPedido(pedido);
		}
		itemPedidoRepository.saveAll(pedido.getItens());
		emailService.sendOrderConfirmationEmail(pedido);
		return pedido;
	}
}
