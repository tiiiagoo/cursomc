package com.tgs.cursomc.domain.enums;

public enum EstadoPagamento {

	PENDENTE(1, "Pagamento pendente"),
	QUITADO(2, "Pagamento Quitado"),
	CANCELADO(3, "Pagamento Cancelado");
	
	private int cod;
	private String descricao;
	
	private EstadoPagamento(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public int getCod() {
		return cod;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static EstadoPagamento toEnum(Integer cod) {
		
		if(cod == null ) {
			return null;
		}else {
			for(EstadoPagamento ep : EstadoPagamento.values()) {
				if(cod.equals(ep.getCod())) {
					return ep;
				}
			}
		}
		
		 throw new IllegalArgumentException("O codigo informado não é valido: "+cod);
	}
}