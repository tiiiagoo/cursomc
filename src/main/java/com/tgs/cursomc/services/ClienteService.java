package com.tgs.cursomc.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tgs.cursomc.domain.Cidade;
import com.tgs.cursomc.domain.Cliente;
import com.tgs.cursomc.domain.Endereco;
import com.tgs.cursomc.domain.enums.Perfil;
import com.tgs.cursomc.domain.enums.TipoCliente;
import com.tgs.cursomc.dto.ClienteDTO;
import com.tgs.cursomc.dto.ClientePostDTO;
import com.tgs.cursomc.repositories.CidadeRepository;
import com.tgs.cursomc.repositories.ClienteRepository;
import com.tgs.cursomc.repositories.EnderecoRepository;
import com.tgs.cursomc.security.UserSpringSecurity;
import com.tgs.cursomc.services.exceptions.AuthorizationException;
import com.tgs.cursomc.services.exceptions.DataIntegrityException;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; 
	@Autowired
	private AmazonS3Service amazonS3Service;

	public Cliente find(Long id) throws ObjectNotFoundException {
		
		UserSpringSecurity user = UserService.getAuthenticated();
		if(user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> cliente = repository.findById(id);
		return cliente.orElseThrow(()-> new ObjectNotFoundException(
							"Objeto não encontrado Id: "+ id +" , Tipo: "+ Cliente.class.getName()));
	}

	public List<Cliente> findAll() {
		return repository.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}


	public Cliente update(Cliente obj) throws ObjectNotFoundException {
		Cliente cliente = find(obj.getId());
		updateData(cliente, obj);
		return repository.save(cliente);
	}

	private void updateData(Cliente cliente, Cliente obj) {
		cliente.setNome(obj.getNome());
		cliente.setEmail(obj.getEmail());		
	}

	public void delete(Long id) throws ObjectNotFoundException {
		find(id);
		try {
			repository.deleteById(id);
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma Cliente que possui pedidos");
		}		
	}	

	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repository.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente fromDtoToObj(ClienteDTO dto) {
		return new Cliente(dto.getId(), dto.getNome(), dto.getEmail(), null, null, null);
	}
	
	public Cliente fromDtoToObj(@Valid ClientePostDTO dto) {
		Cliente cliente =
				new Cliente(null, dto.getNome(), dto.getEmail(), dto.getCpfCnpj(), TipoCliente.toEnum(dto.getTipoCliente()), passwordEncoder.encode(dto.getSenha()));
		Cidade cidade = cidadeRepository.getOne(dto.getIdCidade());
		Endereco endereco = 
				new Endereco(null, dto.getLogradouro(), dto.getNumero(), dto.getComplemento(), dto.getBairro(), dto.getCep(), cliente, cidade);
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(dto.getTelefone1());
		
		if(dto.getTelefone2()!= null) cliente.getTelefones().add(dto.getTelefone2());
		if(dto.getTelefone3()!= null) cliente.getTelefones().add(dto.getTelefone3());		
		
		return cliente;
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		return amazonS3Service.uploadFile(multipartFile);
	}
}
