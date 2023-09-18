package br.com.profectum.controllers;

/**
 * @author Wendel Ferreira de Mesquita
 * Na camada Controller, podemos ver como os dados serão enviados e recebidos pelo client-side.
 * Como se trata de uma API, está sendo utilizado ResponseEntity. Para recebimento dos dados, está sendo usado
 * o design pattern DTO,  tanto para request, quanto para a response.
 * A ResponseEntity é do tipo Object por causa das diferentes respostas que podem ser dadas, desde um código HTTP 
 * com os objetos, até códigos HTTP com string como respostas.
 */

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.profectum.enums.ErrosEnum;
import br.com.profectum.exceptions.RegraNegocioException;
import br.com.profectum.model.Usuario;
import br.com.profectum.requestDTO.UsuarioRequestDTO;
import br.com.profectum.services.UsuarioService;
import br.com.profectum.utils.ResponseErrosUtil;

@RestController
@RequestMapping(path = "/usuarios")
public class UsuarioController {
	private UsuarioService service;

	public UsuarioController(UsuarioService service) {
		this.service = service;
	}
	
	@PostMapping(path = "/salvar")
	public ResponseEntity<Object> salvarUsuario(@RequestBody UsuarioRequestDTO dto) {
		try {
			Usuario usuario = service.converterDeDTO(dto);
			usuario = service.criarUsuario(usuario);
			return new ResponseEntity<>(service.converterParaDTO(usuario), HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping
	public ResponseEntity<Object> listarTodosOsUsuarios() {
		return service.listarTodosOsUsuarios();
	}
	
	@GetMapping(path = "/buscar")
	public ResponseEntity<Object> buscarUsuario(@RequestParam String login) {
		try {
			Optional<Usuario> usuario = service.buscarUsuarioPorLogin(login);
			return ResponseEntity.ok(service.converterParaDTO(usuario.get()));
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping(path = "/{login}/atualizar")
	public ResponseEntity<? extends Object> atualizarUsuario(@PathVariable String login, @RequestBody UsuarioRequestDTO dto) {
		if (service.verificarListaDeUsuarios().size() == 0)
			return ResponseErrosUtil.respostaErro004();
		
		if(service.verificarExistencia(login) == false)
			return ResponseErrosUtil.respostaErro002();
		
		return service.buscarUsuarioPorLogin(login).map(entidade -> {
			try {
				Usuario usuario = service.converterDeDTO(dto);
				usuario.setIdUsuario(entidade.getIdUsuario());
				service.atualizarUsuario(login, usuario);
				return ResponseEntity.ok(service.converterParaDTO(usuario));
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getLocalizedMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping(path = "/deletar/{login}")
	public ResponseEntity<? extends Object> deletarUsuario(@PathVariable String login) {
		if (service.verificarListaDeUsuarios().size() == 0)
			return ResponseErrosUtil.respostaErro004();
		
		if(service.verificarExistencia(login) == false)
			return ResponseErrosUtil.respostaErro002();
		
		return service.buscarUsuarioPorLogin(login).map(entidade -> {
			try {
				service.deletarUsuario(login);
				return new ResponseEntity<Object>("Deletado com sucesso",HttpStatus.OK);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}
	
}
