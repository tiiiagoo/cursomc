package com.tgs.cursomc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tgs.cursomc.domain.Categoria;
import com.tgs.cursomc.domain.Cidade;
import com.tgs.cursomc.domain.Cliente;
import com.tgs.cursomc.domain.Endereco;
import com.tgs.cursomc.domain.Estado;
import com.tgs.cursomc.domain.Produto;
import com.tgs.cursomc.domain.enums.TipoCliente;
import com.tgs.cursomc.repositories.CategoriaRepository;
import com.tgs.cursomc.repositories.CidadeRepository;
import com.tgs.cursomc.repositories.ClienteRepository;
import com.tgs.cursomc.repositories.EnderecoRepository;
import com.tgs.cursomc.repositories.EstadoRepository;
import com.tgs.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository   produtoRepository;
	@Autowired
	private EstadoRepository    estadoRepository;
	@Autowired
	private CidadeRepository    cidadeRepository;
	@Autowired
	private ClienteRepository   clienteRepository;
	@Autowired
	private EnderecoRepository  enderecoRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria(null, "Informatica");
		Categoria cat2 = new Categoria(null, "Escritorio");	
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null,"Mouse" , 80.00);	
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		Estado e1 = new Estado(null, "Minas Gerais");
		Estado e2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlandia",  e1);
		Cidade c2 = new Cidade(null, "São Paulo", e2);
		Cidade c3 = new Cidade(null, "Campinas", e2);	
		
		e1.getCidades().addAll(Arrays.asList(c1));
		e2.getCidades().addAll(Arrays.asList(c2, c3));	
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOA_FISICA);
		cli1.getTelefones().addAll(Arrays.asList("27363323","93838393"));		
		
		Endereco end1 = new Endereco(null, "Rua Flores", "300", "apto 203", "Jardim", "38220834", cli1, c1);
		Endereco end2 = new Endereco(null, "Av Matos", "105", "sala 800", "Centro", "38777012", cli1, c1);
		
		cli1.getEnderecos().addAll(Arrays.asList(end1, end2));
		
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		produtoRepository.saveAll(Arrays.asList(p1,p2,p3));
		estadoRepository.saveAll(Arrays.asList(e1,e2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(end1, end2));
	}
}
