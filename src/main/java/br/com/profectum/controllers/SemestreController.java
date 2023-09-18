package br.com.profectum.controllers;

/**
 * @author Wendel Ferreira de Mesquita
 * Na camada Controller, podemos ver como os dados serão enviados e recebidos pelo client-side.
 * Como se trata de uma API, está sendo utilizado ResponseEntity. Para recebimento dos dados, está sendo usado
 * o design pattern DTO,  tanto para request, quanto para a response.
 * A ResponseEntity é do tipo Object por causa das diferentes respostas que podem ser dadas, desde um código HTTP 
 * com os objetos, até códigos HTTP com string como respostas.
 */

import java.util.List;
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
import br.com.profectum.model.Semestre;
import br.com.profectum.requestDTO.SemestreRequestDTO;
import br.com.profectum.services.SemestreService;
import br.com.profectum.utils.ResponseErrosUtil;

@RestController
@RequestMapping(path = "/semestres")
public class SemestreController {
	private SemestreService service;

	public SemestreController(SemestreService service) {
		this.service = service;
	}

	@PostMapping(path = "/salvar")
	public ResponseEntity<Object> salvarSemestre(@RequestBody SemestreRequestDTO dto) {
		try {
			Semestre semestre = service.converterDeDTO(dto);
			semestre = service.criarSemestre(semestre);
			return new ResponseEntity<>(service.converterParaDTO(semestre), HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<Object> listarTodosOsSemestres() {
		return service.listarTodosOsSemestres();
	}

	@GetMapping(path = "/buscar")
	public ResponseEntity<Object> buscarSemestre(@RequestParam Long idSemestre) {
		try {
			Optional<Semestre> semestre = service.buscarSemestrePorId(idSemestre);
			return ResponseEntity.ok(service.converterParaDTO(semestre.get()));
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping(path = "/{idSemestre}/atualizar")
	public ResponseEntity<? extends Object> atualizarSemestre(@PathVariable Long idSemestre,
			@RequestBody SemestreRequestDTO dto) {
		if (service.verificarSemestres().size() == 0)
			return ResponseErrosUtil.respostaErro004();

		if (service.verificarExistencia(idSemestre) == false)
			return ResponseErrosUtil.respostaErro002();

		return service.buscarSemestrePorId(idSemestre).map(entidade -> {
			try {
				Semestre semestre = service.converterDeDTO(dto);
				semestre.setIdSemestre(entidade.getIdSemestre());
				service.atualizarSemestre(idSemestre, semestre, dto);
				return ResponseEntity.ok(service.converterParaDTO(semestre));
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getLocalizedMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}

	@PutMapping(path = "/{idSemestre}/remover-disciplinas")
	public ResponseEntity<? extends Object> adicionarDisciplina(@PathVariable Long idSemestre,
			@RequestBody List<Long> idDisciplina) {
		if (service.verificarSemestres().size() == 0)
			return ResponseErrosUtil.respostaErro004();

		if (service.verificarExistencia(idSemestre) == false)
			return ResponseErrosUtil.respostaErro002();

		return service.buscarSemestrePorId(idSemestre).map(entidade -> {
			try {
				Semestre semestre = service.removerDisciplinas(idDisciplina, entidade);
				return ResponseEntity.ok(service.converterParaDTO(semestre));
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getLocalizedMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}

	@DeleteMapping(path = "/deletar/{idSemestre}")
	public ResponseEntity<? extends Object> deletarSemestre(@PathVariable Long idSemestre) {
		if (service.verificarSemestres().size() == 0)
			return ResponseErrosUtil.respostaErro004();

		if (service.verificarExistencia(idSemestre) == false)
			return ResponseErrosUtil.respostaErro002();

		return service.buscarSemestrePorId(idSemestre).map(entidade -> {
			try {
				service.deletarSemestre(idSemestre);
				return new ResponseEntity<Object>("Deletado com sucesso", HttpStatus.OK);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}
}
