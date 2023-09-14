package br.com.profectum.enums;

/**
 * @author Wendel Ferreira de Mesquita
 * Para definir as roles, é bastante comum usar enums justamente por conseguir
 * padronizar os dados. Gerlamente, as roles são dados estáticos, logo, quanto
 * mais padronizado, mais fácil fica de controlar e manter.
 */

import lombok.Getter;

@Getter
public enum RoleUsuarios {
	USUARIO_MASTER("USUARIO"),
	ADMIN("ADMIN"),
	COORDENADOR_CURSO("COORDENADOR"),
	PROFESSOR("PROFESSOR"),
	ALUNO("ALUNO");
	
	private String role;
	
	RoleUsuarios(String role){
		this.role = role;
	}
}
