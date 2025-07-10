package com.devlyn.services;

import org.springframework.mail.SimpleMailMessage;

import com.devlyn.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	void sendEmail(SimpleMailMessage msg);
	
}
