package br.com.profectum.dto;

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
