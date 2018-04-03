package com.tgs.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.tgs.cursomc.domain.PagamentoComBoleto;

@Service
public class BoletoService {

	public void preencherPgtoComBoleto(PagamentoComBoleto pgtoBoleto, Date dataPedido) {
		Calendar dataVencimento = Calendar.getInstance();
		dataVencimento.setTime(dataPedido);
		dataVencimento.add(Calendar.DAY_OF_MONTH, 7);
		pgtoBoleto.setDataVencimento(dataVencimento.getTime());
	}

}
