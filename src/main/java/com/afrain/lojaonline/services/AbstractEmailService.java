package com.afrain.lojaonline.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.afrain.lojaonline.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String remetente;
	
	@Override
	public void sendOrderConfirmationEmail(Pedido pedido) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(pedido);
		sendEmail(sm);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido pedido) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(pedido.getCliente().getEmail());
		sm.setFrom(remetente);
		sm.setSubject("Pedido nº: " + pedido.getId() + ", confirmado!");
		sm.setSentDate(new Date(System.currentTimeMillis()));//PEGA A DATA DO MEU SERVIDOR!
		sm.setText(pedido.toString());
		return sm;
	}
}
