package br.com.profectum.dto;

/**
 * @author Wendel Ferreira de Mesquita
 * O design pattern DTO (Data Transfer Object) foi usado neste projeto pensando
 * no desacoplamento das entidades para receber os dados do cliente.
 * Neste projeto, o DTO não está sendo usado nas ResponseEntities porque não
 * foi usado um campo externalId para que as entidades sejam manipuladas nos endpoints.
 * Fica como ponto de melhoria a adição de um campo extenralId e usar objetos DTO na saída
 * de dados, para desacoplar ainda mais a entidade da visão do cliente.
 */

import br.com.profectum.enums.RoleUsuarios;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioDTO {
	
	private String login;
	private String nome;
	private String senha;
	private RoleUsuarios roleUsuario;
}
