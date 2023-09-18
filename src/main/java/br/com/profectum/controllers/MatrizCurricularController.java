package br.com.profectum.controllers;

/**
 * @author Wendel Ferreira de Mesquita
 * Na camada Controller, podemos ver como os dados serão enviados e recebidos pelo client-side.
 * Como se trata de uma API, está sendo utilizado ResponseEntity. Para recebimento dos dados, está sendo usado
 * o design pattern DTO,  tanto para request, quanto para a response.
 * A ResponseEntity é do tipo Object por causa das diferentes respostas que podem ser dadas, desde um código HTTP 
 * com os objetos, até códigos HTTP com string como respostas..
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
import br.com.profectum.model.MatrizCurricular;
import br.com.profectum.requestDTO.MatrizCurricularRequestDTO;
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
	public ResponseEntity<Object> salvarMatrizCurricular(@RequestBody MatrizCurricularRequestDTO dto) {
		try {
			MatrizCurricular matrizCurricular = service.converterDeDTO(dto);
			matrizCurricular = service.criarMatrizCurricular(matrizCurricular);
			return new ResponseEntity<>(service.converterParaDTO(matrizCurricular), HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping
	public ResponseEntity<Object> listarTodasAsMatrizesCurriculares() {
		return service.listarTodasAsMatrizesCurriculares();
	}
	
	@GetMapping(path = "/buscar")
	public ResponseEntity<Object> buscarMatrizCurricular(@RequestParam Long idMatrizCurricular) {
		try {
			Optional<MatrizCurricular> matrizCurricular = service.buscarMatrizCurricularPorId(idMatrizCurricular);
			return ResponseEntity.ok(service.converterParaDTO(matrizCurricular.get()));
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping(path = "/{idMatrizCurricular}/atualizar")
	public ResponseEntity<? extends Object> atualizarMatrizCurricular(@PathVariable Long idMatrizCurricular, @RequestBody MatrizCurricularRequestDTO dto) {
		if (service.verificarMatrizesCurriculares().size() == 0)
			return ResponseErrosUtil.respostaErro004();
		
		if(service.verificarExistencia(idMatrizCurricular) == false)
			return ResponseErrosUtil.respostaErro002();
		
		return service.buscarMatrizCurricularPorId(idMatrizCurricular).map(entidade -> {
			try {
				MatrizCurricular matrizCurricular = service.converterDeDTO(dto);
				matrizCurricular.setIdMatrizCurricular(entidade.getIdMatrizCurricular());
				service.atualizarMatrizCurricular(idMatrizCurricular, matrizCurricular, dto);
				return ResponseEntity.ok(service.converterParaDTO(matrizCurricular));
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getLocalizedMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping(path = "/deletar/{idMatrizCurricular}")
	public ResponseEntity<? extends Object> deletarMatrizCurricular(@PathVariable Long idMatrizCurricular) {
		if (service.verificarMatrizesCurriculares().size() == 0)
			return ResponseErrosUtil.respostaErro004();
		
		if(service.verificarExistencia(idMatrizCurricular) == false)
			return ResponseErrosUtil.respostaErro002();
		
		return service.buscarMatrizCurricularPorId(idMatrizCurricular).map(entidade -> {
			try {
				service.deletarMatrizCurricular(idMatrizCurricular);
				return new ResponseEntity<Object>("Deletado com sucesso", HttpStatus.OK);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity<Object>(ErrosEnum.ERRO_003.getMensagemErro(), HttpStatus.BAD_REQUEST));
	}
}
