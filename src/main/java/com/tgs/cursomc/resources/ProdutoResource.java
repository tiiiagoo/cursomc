package com.tgs.cursomc.resources;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tgs.cursomc.domain.Produto;
import com.tgs.cursomc.dto.ProdutoDTO;
import com.tgs.cursomc.resources.utils.UTIL;
import com.tgs.cursomc.services.ProdutoService;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ProdutoService service;	
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto>  find(@PathVariable Long id) {
		LOG.info("Getting produto with ID {}.", id);
		Produto produto = service.find(id);
		return ResponseEntity.ok(produto);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>>  search(
			@ RequestParam(value="nome", defaultValue="") String nome, 
			@ RequestParam(value="categorias", defaultValue="") String categorias, 
			@ RequestParam(value="page", defaultValue="0") Integer page, 
			@ RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@ RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@ RequestParam(value="direction", defaultValue="ASC") String direction){
		String nomeDecoded = UTIL.decodeParam(nome);
		List<Long> idsCategorias = UTIL.decodeIntList(categorias);
		Page<Produto> pageList = service.search(nomeDecoded, idsCategorias, page, linesPerPage, orderBy, direction);
		Page<ProdutoDTO> listPageDto = pageList.map(produto -> new ProdutoDTO(produto));
		return ResponseEntity.ok(listPageDto);
	}
}
