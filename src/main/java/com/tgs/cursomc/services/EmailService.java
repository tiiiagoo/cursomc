package com.tgs.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.tgs.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);	
}
