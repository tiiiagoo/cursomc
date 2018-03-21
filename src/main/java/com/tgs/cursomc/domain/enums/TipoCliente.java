package com.tgs.cursomc.domain.enums;

public enum TipoCliente {

	PESSOA_FISICA(1,  "Pessoa Fisica"),
	PESSOA_JURIDICA(2,"Pessoa Juridica");
	
	private int cod;
	private String descricao;
	
	private TipoCliente(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public int getCod() {
		return cod;
	}

	public String getDescricao() {
		return descricao;
	}	
	
	public static TipoCliente toEnum(Integer cod) {
		
		if(cod == null ) {
			return null;
		}else {
			for(TipoCliente tp : TipoCliente.values()) {
				if(cod.equals(tp.getCod())) {
					return tp;
				}
			}
		}
		
		 throw new IllegalArgumentException("O codigo informado não é valido: "+cod);
	}
}
