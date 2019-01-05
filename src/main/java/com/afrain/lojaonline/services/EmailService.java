package com.afrain.lojaonline.services;

import org.springframework.mail.SimpleMailMessage;

import com.afrain.lojaonline.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);
	
}
