package com.devlyn.services;

import org.springframework.mail.SimpleMailMessage;

import com.devlyn.domain.Cliente;
import com.devlyn.domain.Pedido;

import jakarta.mail.internet.MimeMessage;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	void sendEmail(SimpleMailMessage msg);
	
	void sendOrderConfirmationHtmlEmail(Pedido obj); 
	void sendHtmlEmail(MimeMessage msg);
	
	void sendNewPasswordEmail(Cliente cliente, String newPass);
	
}
