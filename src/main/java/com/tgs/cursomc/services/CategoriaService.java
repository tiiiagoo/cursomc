package com.tgs.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.tgs.cursomc.domain.Categoria;
import com.tgs.cursomc.dto.CategoriaDTO;
import com.tgs.cursomc.repositories.CategoriaRepository;
import com.tgs.cursomc.services.exceptions.DataIntegrityException;

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
		Categoria categoria = find(obj.getId());
		updateData(categoria, obj);
		return repository.save(obj);
	}

	private void updateData(Categoria categoria, Categoria obj) {
		categoria.setNome(obj.getNome());		
	}

	public void delete(Long id) throws ObjectNotFoundException {		
		find(id);
		try {
			repository.deleteById(id);
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma categoria que possui produtos");
		}
	}

	public List<Categoria> findAll() {
		return repository.findAll();
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}
	
	public Categoria fromDtoToObj(CategoriaDTO dto) {
		return new Categoria(dto.getId(), dto.getNome());
	}
}
