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
public enum ErrosEnum {
	ERRO_001("Erro ao salvar! Por favor, verifique os dados, e conste se já não há alguma entidade com esta chave. (ERRO_001)"),
	ERRO_002("Não foi possível encontrar os dados. Verifique as informações e tente novamente. (ERRO_002)"),
	ERRO_003("Usuário não encontrado. Por favor, verifique os dados e tente novamente. (ERRO_003)"),
	ERRO_004("Algo saiu mal. Por favor, entre em contato com o suporte. (ERRO_DBIEOD)"), 
	ERRO_005("Erro ao salvar. Verifique os dados e tente novamente. (ERRO_005)"),
	ERRO_006("Campo de busca vazio. Por favor, preencha os dados corretamente (ERRO_006)"),
	ERRO_007("Esta disciplina já está adicionada ao semestre. Chamar o Agente James Bond para resolver o problema (ERRO_007)"),
	ERRO_008("Erro ao salvar! Um beneficiario com este CPF já existe. (ERRO_008)"),
	ERRO_010("Verifique os campos e tente novamente. (ERRO_010)"),
	ERRO_012("O Semestre já possui um curso selecionado. (ERRO_012)");
	
	private String mensagemErro;
	
	ErrosEnum(String mensagemErro){
		this.mensagemErro = mensagemErro;
	}
	
}
