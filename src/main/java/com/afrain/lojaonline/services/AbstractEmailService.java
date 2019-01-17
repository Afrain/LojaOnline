package com.afrain.lojaonline.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.afrain.lojaonline.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String remetente;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender javaMailSender;

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
		sm.setSentDate(new Date(System.currentTimeMillis()));// PEGA A DATA DO MEU SERVIDOR!
		sm.setText(pedido.toString());
		return sm;
	}

	protected String htmlFromTemplatePedido(Pedido pedido) {
		Context context = new Context();
		context.setVariable("pedido", pedido);
		return templateEngine.process("email/confirmacaoPedido", context);

	}

	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido pedido) {
		try {
		MimeMessage mm = prepareMimeMessageFromPedido(pedido);
		sendHtmlEmail(mm);
		}
		catch(MessagingException e) {
			sendOrderConfirmationEmail(pedido);
		}
	}

	protected MimeMessage prepareMimeMessageFromPedido(Pedido pedido) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
		mmh.setTo(pedido.getCliente().getEmail());
		mmh.setFrom(remetente);
		mmh.setSubject("Pedido nº: " + pedido.getId() + ", confirmado!");
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplatePedido(pedido), true);
		return mimeMessage;
	}

}
