package com.afrain.lojaonline.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.afrain.lojaonline.domain.Cliente;
import com.afrain.lojaonline.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendOrderConfirmationHtmlEmail(Pedido pedido); 
	
	void sendHtmlEmail(MimeMessage msg);
	
	void enviaNovaSenhaEmail(Cliente cliente, String novaSenha);
	
}
