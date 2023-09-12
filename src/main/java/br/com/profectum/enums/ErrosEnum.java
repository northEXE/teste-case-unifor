package br.com.profectum.enums;

import lombok.Getter;

@Getter
public enum ErrosEnum {
	ERRO_001("Erro ao salvar! Por favor, verifique os dados, e conste se já não há alguma entidade com esta chave. (ERRO_001)"),
	ERRO_002("Não foi possível encontrar um usuário com este login. Tente novamente. (ERRO_002)"),
	ERRO_003("Usuário não encontrado. Por favor, verifique os dados e tente novamente. (ERRO_003)"),
	ERRO_004("Algo saiu mal. Por favor, entre em contato com o suporte. (ERRO_DBIEOD)"), 
	ERRO_005("Erro ao salvar. Verifique os dados e tente novamente. (ERRO_005)"),
	ERRO_006("Campo de busca vazio. Por favor, preencha os dados corretamente (ERRO_006)"),
	ERRO_007("Plano não encontrado. Por favor, verifique os dados e tente novamente. (ERRO_007)"),
	ERRO_008("Erro ao salvar! Um beneficiario com este CPF já existe. (ERRO_008)"),
	ERRO_009("Não foi possível encontrar um beneficiário com este CPF. Tente novamente. (ERRO_002)"),
	ERRO_010("Verifique os campos e tente novamente. (ERRO_010)"),
	ERRO_011("Não foi possível encontrar o plano selecionado. Tente novamente. (ERRO_011)"),
	ERRO_012("Este beneficiário já possui um plano ativo. (ERRO_012)");
	
	private String mensagemErro;
	
	ErrosEnum(String mensagemErro){
		this.mensagemErro = mensagemErro;
	}
	
}
