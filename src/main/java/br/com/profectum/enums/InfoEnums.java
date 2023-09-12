package br.com.profectum.enums;

import lombok.Getter;

@Getter
public enum InfoEnums {
	INFO_001("Não há informações a serem listadas");

	private String mensagem;

	InfoEnums(String mensagem) {
		this.mensagem = mensagem;
	}
}
