package br.com.profectum.enums;

/**
 * @author Wendel Ferreira de Mesquita
 * Para que o client-side tenha uma visão do que está ocorrendo na aplicação
 * foi adotado um sistema de aviso de erros, caso aconteça algo errado na manipulação
 * dos dados, usando Enums, pois a manutenibilidade fica mais fácil, visto que se pode
 * acrescentar tipos de erros e códigos para facilitar a leitura.
 * Fica como ponto de melhoria cirar códigos espécíficos para os erros e aumentar a cobertura
 * dos mesmos.
 */

import lombok.Getter;

@Getter
public enum InfoEnums {
	INFO_001("Não há informações a serem listadas");

	private String mensagem;

	InfoEnums(String mensagem) {
		this.mensagem = mensagem;
	}
}
