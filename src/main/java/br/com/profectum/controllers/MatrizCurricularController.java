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

import br.com.profectum.dto.MatrizCurricularDTO;
import br.com.profectum.enums.ErrosEnum;
import br.com.profectum.exceptions.RegraNegocioException;
import br.com.profectum.model.MatrizCurricular;
import br.com.profectum.services.MatrizCurricularService;
import br.com.profectum.utils.ResponseErrosUtil;

@RestController
@RequestMapping(path = "/matrizes")
public class MatrizCurricularController {
	private MatrizCurricularService service;

	public MatrizCurricularController(MatrizCurricularService service) {
		this.service = service;
	}
	
	@PostMapping(path = "/salvar")
	public ResponseEntity<Object> salvarMatrizCurricular(@RequestBody MatrizCurricularDTO dto) {
		try {
			MatrizCurricular matrizCurricular = service.converterDeDTO(dto);
			matrizCurricular = service.criarMatrizCurricular(matrizCurricular);
			return new ResponseEntity<>(matrizCurricular, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping
	public ResponseEntity<Object> listarTodosOsMatrizCurriculars() {
		return service.listarTodosOsMatrizCurriculars();
	}
	
	@GetMapping(path = "/buscar")
	public ResponseEntity<Object> buscarMatrizCurricular(@RequestParam String nomeMatrizCurricular) {
		try {
			Optional<MatrizCurricular> matrizCurricular = service.buscarMatrizCurricularPorNome(nomeMatrizCurricular);
			return ResponseEntity.ok(matrizCurricular);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping(path = "/{nomeMatrizCurricular}/atualizar")
	public ResponseEntity<? extends Object> atualizarMatrizCurricular(@PathVariable String nomeMatrizCurricular, @RequestBody MatrizCurricularDTO dto) {
		if (service.verificarListaDeMatrizCurriculars().size() == 0)
			return ResponseErrosUtil.respostaErro004();
		
		if(service.verificarExistencia(nomeMatrizCurricular) == false)
			return ResponseErrosUtil.respostaErro002();
		
		return service.buscarMatrizCurricularPorNome(nomeMatrizCurricular).map(entidade -> {
			try {
				MatrizCurricular matrizCurricular = service.converterDeDTO(dto);
				matrizCurricular.setIdMatrizCurricular(entidade.getIdMatrizCurricular());
				service.atualizarMatrizCurricular(nomeMatrizCurricular, matrizCurricular);
				return ResponseEntity.ok(matrizCurricular);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getLocalizedMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping(path = "/deletar/{nomeMatrizCurricular}")
	public ResponseEntity<? extends Object> deletarMatrizCurricular(@PathVariable String nomeMatrizCurricular) {
		if (service.verificarListaDeMatrizCurriculars().size() == 0)
			return ResponseErrosUtil.respostaErro004();
		
		if(service.verificarExistencia(nomeMatrizCurricular) == false)
			return ResponseErrosUtil.respostaErro002();
		
		return service.buscarMatrizCurricularPorNome(nomeMatrizCurricular).map(entidade -> {
			try {
				service.deletarMatrizCurricular(nomeMatrizCurricular);
				return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}
}
