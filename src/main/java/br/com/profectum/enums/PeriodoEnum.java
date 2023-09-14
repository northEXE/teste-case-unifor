package br.com.profectum.enums;

/**
 * @author Wendel Ferreira de Mesquita
 * Para uma padronização dos períodos e evitar campos inadequados, uma enum
 * foi a melhor resposta
 */

import lombok.Getter;

@Getter
public enum PeriodoEnum {
	MATUTINO("MATUTINO"),
	VESPERTINO("VESPERTINO"),
	NOTURNO("NOTURNO");

	private String periodo;

	PeriodoEnum(String periodo) {
		this.periodo = periodo;
	}
}
