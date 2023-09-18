package br.com.profectum.controllers;

/**
 * @author Wendel Ferreira de Mesquita
 * Na camada Controller, podemos ver como os dados serão enviados e recebidos pelo client-side.
 * Como se trata de uma API, está sendo utilizado ResponseEntity. Para recebimento dos dados, está sendo usado
 * o design pattern DTO, tanto para request, quanto para a response.
 * A ResponseEntity é do tipo Object por causa das diferentes respostas que podem ser dadas, desde um código HTTP 
 * com os objetos, até códigos HTTP com string como respostas. 
 */


import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import br.com.profectum.model.Disciplina;
import br.com.profectum.requestDTO.DisciplinaRequestDTO;
import br.com.profectum.services.DisciplinaService;
import br.com.profectum.utils.ResponseErrosUtil;

@RestController
@RequestMapping(path = "disciplinas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DisciplinaController {
	private DisciplinaService service;

	public DisciplinaController(DisciplinaService service) {
		this.service = service;
	}
	
	@PostMapping(path = "/salvar")
	public ResponseEntity<Object> salvarDisciplina(@RequestBody DisciplinaRequestDTO dto) {
		try {
			Disciplina disciplina = service.converterDeDTO(dto);
			disciplina = service.criarDisciplina(disciplina);
			return new ResponseEntity<>(service.converterParaDTO(disciplina), HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping
	public ResponseEntity<Object> listarTodosAsDisciplinas() {
		return service.listarTodasAsDisciplinas();
	}
	
	@GetMapping(path = "/buscar")
	public ResponseEntity<Object> buscarDisciplina(@RequestParam Long idDisciplina) {
		try {
			Optional<Disciplina> disciplina = service.buscarDisciplinaPorId(idDisciplina);
			return ResponseEntity.ok(service.converterParaDTO(disciplina.get()));
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping(path = "/{idDisciplina}/atualizar")
	public ResponseEntity<? extends Object> atualizarDisciplina(@PathVariable Long idDisciplina, @RequestBody DisciplinaRequestDTO dto) {
		if (service.verificarListaDeDisciplinas().size() == 0)
			return ResponseErrosUtil.respostaErro004();
		
		if(service.verificarExistencia(idDisciplina) == false)
			return ResponseErrosUtil.respostaErro002();
		
		return service.buscarDisciplinaPorId(idDisciplina).map(entidade -> {
			try {
				Disciplina disciplina = service.converterDeDTO(dto);
				disciplina.setIdDisciplina(entidade.getIdDisciplina());
				service.atualizarDisciplina(idDisciplina, disciplina);
				return ResponseEntity.ok(service.converterParaDTO(disciplina));
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getLocalizedMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping(path = "/deletar/{idDisciplina}")
	public ResponseEntity<? extends Object> deletarDisciplina(@PathVariable Long idDisciplina) {
		if (service.verificarListaDeDisciplinas().size() == 0)
			return ResponseErrosUtil.respostaErro004();
		
		if(service.verificarExistencia(idDisciplina) == false)
			return ResponseErrosUtil.respostaErro002();
		
		return service.buscarDisciplinaPorId(idDisciplina).map(entidade -> {
			try {
				service.deletarDisciplina(idDisciplina);
				return new ResponseEntity<Object>("Disciplina excluída com sucesso!", HttpStatus.OK);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}
}
