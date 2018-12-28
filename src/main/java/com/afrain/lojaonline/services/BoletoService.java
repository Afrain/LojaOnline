package com.afrain.lojaonline.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.afrain.lojaonline.domain.PagamentoComBoleto;

@Service
public class BoletoService {

	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date istantePedido) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(istantePedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		pagto.setDataPagamento(cal.getTime());
	}
}
