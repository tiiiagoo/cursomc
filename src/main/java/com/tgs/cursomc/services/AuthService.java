package com.tgs.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tgs.cursomc.domain.Cliente;
import com.tgs.cursomc.repositories.ClienteRepository;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswor;
	
	private Random random = new Random();

	public void sendNewPassword(String email) throws ObjectNotFoundException {
		Cliente cliente = clienteRepository.findByEmail(email);
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não cadastrado");
		}
		
		String newPassword = newPassword();
		cliente.setSenha(bCryptPasswor.encode(newPassword));
		
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPassword);
	}

	private String newPassword() {
		char[] chars = new char[8];
		for(int i=0; i<8; i++ ) {
			chars[i] = randomChar();
		}
		return new String(chars);
	}
	
	/**
	 * 
	 * @return Um char aleatorio de (0 ate 9) ou ('A' ate 'Z') ou ('a' ate 'z') de acordo com valores unicode
	 */
	private char randomChar() {
		int value = random.nextInt(3);
		
		if(value == 0) {// Gera um digito
			return (char) (random.nextInt(10) + 48);
		}
		else if(value == 1) { // Gera letra maiuscula
			return (char) (random.nextInt(26) + 65);
		}else { // Gera letra minuscula
			return (char) (random.nextInt(26) + 97);
		}		
	}
}
