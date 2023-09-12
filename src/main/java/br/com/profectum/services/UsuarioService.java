package br.com.profectum.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.profectum.dto.UsuarioDTO;
import br.com.profectum.enums.ErrosEnum;
import br.com.profectum.enums.InfoEnums;
import br.com.profectum.exceptions.RegraNegocioException;
import br.com.profectum.model.Usuario;
import br.com.profectum.repositories.UsuarioRepository;

@Service
public class UsuarioService {
	private UsuarioRepository repository;

	public UsuarioService(UsuarioRepository repository) {
		this.repository = repository;
	}

	public Usuario criarUsuario(Usuario usuario) {
		Optional<Usuario> usuarioCheck = repository.findByLogin(usuario.getLogin());
		if (usuarioCheck.isPresent())
			throw new RegraNegocioException(ErrosEnum.ERRO_001.getMensagemErro());

		return repository.save(usuario);
	}

	public ResponseEntity<Object> listarTodosOsUsuarios() {
		List<Usuario> usuarios = verificarListaDeUsuarios();
		if (usuarios.size() == 0) 
			return ResponseEntity.ok().body(InfoEnums.INFO_001.getMensagem());
		
		return ResponseEntity.ok(usuarios);
	}
	
	public Optional<Usuario> buscarUsuarioPorLogin(String login) {
		if (login.isEmpty() || login.isBlank()) 
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());
		
		Optional<Usuario> usuario = repository.findByLogin(login);
		if (!usuario.isPresent())
			throw new RegraNegocioException(ErrosEnum.ERRO_003.getMensagemErro());

		return usuario;
	}

	public Usuario atualizarUsuario(String login, Usuario usuarioModificado) {
		return repository.save(usuarioModificado);
	}

	public void deletarUsuario(String login) {
		if (login.isEmpty() || login.isBlank()) 
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());
		
		Usuario usuario = repository.findByLogin(login).get();
		if (usuario.equals(null))
			throw new RegraNegocioException(ErrosEnum.ERRO_002.getMensagemErro());

		repository.delete(usuario);
	}
	
	public Usuario converterDeDTO(UsuarioDTO dto) {
		Usuario usuario = Usuario.builder()
				.login(dto.getLogin())
				.nome(dto.getNome())
				.senha(dto.getSenha())
				.roleUsuario(dto.getRoleUsuario()).build();

		return usuario;
	}
	
	public List<Usuario> verificarListaDeUsuarios() {
		return repository.findAll();
	}
	
	public boolean verificarExistencia(String login){
		Optional<Usuario> usuario = repository.findByLogin(login);
		if(usuario.isPresent())
			return true;
		
		return false;
	}
	
}
