package br.com.profectum.responseDTO;

/**
 * @author Wendel Ferreira de Mesquita
 * O design pattern DTO (Data Transfer Object) foi usado neste projeto pensando
 * no desacoplamento das entidades com a camada de cliente. Assim, tudo o que a camada
 * de cliente envia e recebe, são os objetos de transferênicia, e não as entidades em si,
 * mantendo um padrão de segurança maior, retirando a responsabilidade da camada de entidades
 * de se comunicar com o cliente, mantendo-se somemte em contato com as regras de negócio e a
 * base de dados.
 */

import br.com.profectum.enums.RoleUsuarios;
import br.com.profectum.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

	public UsuarioResponseDTO(Usuario usuario) {
		this(usuario.getIdUsuario(), usuario.getLogin(), usuario.getNome(), usuario.getSenha(),
				usuario.getRoleUsuario());
	}

	private Long idUsuario;
	private String login;
	private String nome;
	private String senha;
	private RoleUsuarios roleUsuario;
}
