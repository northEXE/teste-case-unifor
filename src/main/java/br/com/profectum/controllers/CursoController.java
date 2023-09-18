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
import br.com.profectum.model.Curso;
import br.com.profectum.requestDTO.CursoRequestDTO;
import br.com.profectum.services.CursoService;
import br.com.profectum.utils.ResponseErrosUtil;

@RestController
@RequestMapping(path = "/cursos")
public class CursoController {
	private CursoService service;

	public CursoController(CursoService service) {
		this.service = service;
	}
	
	@PostMapping(path = "/salvar")
	public ResponseEntity<Object> salvarCurso(@RequestBody CursoRequestDTO dto) {
		try {
			Curso curso = service.converterDeDTO(dto);
			curso = service.criarCurso(curso);
			return new ResponseEntity<>(service.converterParaDTO(curso), HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping
	public ResponseEntity<Object> listarTodosOsCursos() {
		return service.listarTodosOsCursos();
	}
	
	@GetMapping(path = "/buscar")
	public ResponseEntity<Object> buscarCurso(@RequestParam Long idCurso) {
		try {
			Optional<Curso> curso = service.buscarCursoPorId(idCurso);
			return ResponseEntity.ok(service.converterParaDTO(curso.get()));
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping(path = "/{idCurso}/atualizar")
	public ResponseEntity<? extends Object> atualizarCurso(@PathVariable Long idCurso, @RequestBody CursoRequestDTO dto) {
		if (service.verificarCursos().size() == 0)
			return ResponseErrosUtil.respostaErro004();
		
		if(service.verificarExistencia(idCurso) == false)
			return ResponseErrosUtil.respostaErro002();
		
		return service.buscarCursoPorId(idCurso).map(entidade -> {
			try {
				Curso curso = service.converterDeDTO(dto);
				curso.setIdCurso(entidade.getIdCurso());
				service.atualizarCurso(idCurso, curso, dto);
				return ResponseEntity.ok(service.converterParaDTO(curso));
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getLocalizedMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping(path = "/deletar/{idCurso}")
	public ResponseEntity<? extends Object> deletarCurso(@PathVariable Long idCurso) {
		if (service.verificarCursos().size() == 0)
			return ResponseErrosUtil.respostaErro004();
		
		if(service.verificarExistencia(idCurso) == false)
			return ResponseErrosUtil.respostaErro002();
		
		return service.buscarCursoPorId(idCurso).map(entidade -> {
			try {
				service.deletarCurso(idCurso);
				return new ResponseEntity<Object>("Deletado com sucesso", HttpStatus.OK);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}
}
