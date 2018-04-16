package com.tgs.cursomc.services;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgs.cursomc.domain.Cliente;
import com.tgs.cursomc.domain.ItemPedido;
import com.tgs.cursomc.domain.PagamentoComBoleto;
import com.tgs.cursomc.domain.Pedido;
import com.tgs.cursomc.domain.enums.EstadoPagamento;
import com.tgs.cursomc.repositories.ItemPedidoRepository;
import com.tgs.cursomc.repositories.PagamentoRepository;
import com.tgs.cursomc.repositories.PedidoRepository;
import com.tgs.cursomc.security.UserSpringSecurity;
import com.tgs.cursomc.services.exceptions.AuthorizationException;

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

	public Pedido find(Long id) throws ObjectNotFoundException {
		UserSpringSecurity user = UserService.getAuthenticated();
		Cliente cliente = clienteService.find(user.getId());
		if(user==null || !id.equals(cliente.getId())) {
			throw new AuthorizationException("Acesso negado");
		}	
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
		emailService.sendOrderConfirmationHtmlEmail(pedido);
		return pedido;
	}
	
	public Page<Pedido> findByPage(Integer page, Integer linesPerPage, String orderBy, String direction) throws ObjectNotFoundException{
		UserSpringSecurity user = UserService.getAuthenticated();
		if(user==null) {
			throw new AuthorizationException("Acesso negado");
		}		
		Cliente cliente = clienteService.find(user.getId());		
		Pageable pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findByCliente(cliente, pageRequest);
	}
}
