package br.com.profectum.controllers;

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

import br.com.profectum.dto.SemestreDTO;
import br.com.profectum.enums.ErrosEnum;
import br.com.profectum.exceptions.RegraNegocioException;
import br.com.profectum.model.Semestre;
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
	public ResponseEntity<Object> salvarSemestre(@RequestBody SemestreDTO dto) {
		try {
			Semestre semestre = service.converterDeDTO(dto);
			semestre = service.criarSemestre(semestre);
			return new ResponseEntity<>(semestre, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping
	public ResponseEntity<Object> listarTodosOsSemestres() {
		return service.listarTodosOsSemestres();
	}
	
	@GetMapping(path = "/buscar")
	public ResponseEntity<Object> buscarSemestre(@RequestParam String nomeSemestre) {
		try {
			Optional<Semestre> semestre = service.buscarSemestrePorNome(nomeSemestre);
			return ResponseEntity.ok(semestre);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping(path = "/{nomeSemestre}/atualizar")
	public ResponseEntity<? extends Object> atualizarSemestre(@PathVariable String nomeSemestre, @RequestBody SemestreDTO dto) {
		if (service.verificarListaDeSemestres().size() == 0)
			return ResponseErrosUtil.respostaErro004();
		
		if(service.verificarExistencia(nomeSemestre) == false)
			return ResponseErrosUtil.respostaErro002();
		
		return service.buscarSemestrePorNome(nomeSemestre).map(entidade -> {
			try {
				Semestre semestre = service.converterDeDTO(dto);
				semestre.setIdSemestre(entidade.getIdSemestre());
				service.atualizarSemestre(nomeSemestre, semestre);
				return ResponseEntity.ok(semestre);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getLocalizedMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping(path = "/deletar/{nomeSemestre}")
	public ResponseEntity<? extends Object> deletarSemestre(@PathVariable String nomeSemestre) {
		if (service.verificarListaDeSemestres().size() == 0)
			return ResponseErrosUtil.respostaErro004();
		
		if(service.verificarExistencia(nomeSemestre) == false)
			return ResponseErrosUtil.respostaErro002();
		
		return service.buscarSemestrePorNome(nomeSemestre).map(entidade -> {
			try {
				service.deletarSemestre(nomeSemestre);
				return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}
}
