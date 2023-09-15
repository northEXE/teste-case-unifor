package br.com.profectum.services;

/**
 * @author Wendel Ferreira de Mesquita
 * Nesta classe encontramos a camada de serviço. Como o projeto é um CRUD padrão usando Spring Data,
 * JPA e Hibernate, é uma solução padrão de manipulação dos dados, onde há tratamento de dados com chaves
 * estrangeiras, relações de N para N (como pode ser visto na camada de entidade) e manipulação de listas.
 * Fica como ponto de melhoria criar queries personalizadas para diminuir e/ou excluir o uso de estruturas
 * de repetição para a manipulação dos dados, afim de melhorar a complexidade de execução da aplicação.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.profectum.dto.MatrizCurricularDTO;
import br.com.profectum.enums.ErrosEnum;
import br.com.profectum.enums.InfoEnums;
import br.com.profectum.exceptions.RegraNegocioException;
import br.com.profectum.model.Curso;
import br.com.profectum.model.MatrizCurricular;
import br.com.profectum.repositories.MatrizCurricularRepository;

@Service
public class MatrizCurricularService {

	private MatrizCurricularRepository repository;
	private CursoService cursoService;

	public MatrizCurricularService(MatrizCurricularRepository repository, CursoService cursoService) {
		this.repository = repository;
		this.cursoService = cursoService;
	}

	public MatrizCurricular criarMatrizCurricular(MatrizCurricular matrizCurricular) {

		return repository.save(matrizCurricular);
	}

	public ResponseEntity<Object> listarTodosOsMatrizCurriculars() {
		List<MatrizCurricular> matrizCurriculars = verificarMatrizesCurriculares();
		if (matrizCurriculars.size() == 0) 
			return ResponseEntity.ok().body(InfoEnums.INFO_001.getMensagem());
		
		return ResponseEntity.ok(matrizCurriculars);
	}
	
	public Optional<MatrizCurricular> buscarMatrizCurricularPorId(Long idMatrizCurricular) {
		if (idMatrizCurricular.toString().isEmpty() || idMatrizCurricular.toString().isBlank()) 
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());
		
		Optional<MatrizCurricular> matrizCurricular = repository.findById(idMatrizCurricular);
		if (!matrizCurricular.isPresent())
			throw new RegraNegocioException(ErrosEnum.ERRO_003.getMensagemErro());

		return matrizCurricular;
	}

	public MatrizCurricular atualizarMatrizCurricular(Long idMatrizCurricular, MatrizCurricular matrizCurricularModificada, MatrizCurricularDTO dto) {
		Optional<MatrizCurricular> matrizCurricular = buscarMatrizCurricularPorId(idMatrizCurricular);
		if(dto.getNomeMatrizCurricular() == null)
			matrizCurricularModificada.setNomeMatrizCurricular(matrizCurricular.get().getNomeMatrizCurricular());
		if (verificarCursos(matrizCurricular.get()) != 0 && dto.getIdsCursos() == null) {
			matrizCurricularModificada.setCursos(matrizCurricular.get().getCursos());
		} else if (verificarCursos(matrizCurricular.get()) != 0 && dto.getIdsCursos() != null) {
			matrizCurricularModificada.getCursos().addAll(0, matrizCurricular.get().getCursos());
		}
		return repository.save(matrizCurricularModificada);
	}
	
	private List<Curso> adicionarCursos(List<Long> idsCursos) {
		if (cursoService.verificarCursos().size() == 0)
			throw new RegraNegocioException(ErrosEnum.ERRO_004.getMensagemErro());

		List<Curso> matrizes = new ArrayList<Curso>();
		idsCursos.forEach(e -> {
			Optional<Curso> curso = cursoService.buscarCursoPorId(e);
			matrizes.add(curso.get());
		});
		
		return matrizes;
	}

	public void deletarMatrizCurricular(Long idMatrizCurricular) {
		if (idMatrizCurricular.toString().isEmpty() || idMatrizCurricular.toString().isBlank()) 
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());
		
		MatrizCurricular matrizCurricular = repository.findById(idMatrizCurricular).get();
		if (matrizCurricular.equals(null))
			throw new RegraNegocioException(ErrosEnum.ERRO_002.getMensagemErro());

		repository.delete(matrizCurricular);
	}
	
	public MatrizCurricular converterDeDTO(MatrizCurricularDTO dto) {
		
		if(dto.getIdsCursos() != null) {
			MatrizCurricular matrizCurricular = MatrizCurricular.builder()
					.nomeMatrizCurricular(dto.getNomeMatrizCurricular())
					.cursos(adicionarCursos(dto.getIdsCursos())).build();
			
			return matrizCurricular;
		}
		
		MatrizCurricular matrizCurricular = MatrizCurricular.builder()
				.nomeMatrizCurricular(dto.getNomeMatrizCurricular()).build();

		return matrizCurricular;
	}
	
	public List<MatrizCurricular> verificarMatrizesCurriculares() {
		return repository.findAll();
	}
	
	public boolean verificarExistencia(Long idMatrizCurricular){
		Optional<MatrizCurricular> matrizCurricular = repository.findById(idMatrizCurricular);
		if(matrizCurricular.isPresent())
			return true;
		
		return false;
	}
	
	private Integer verificarCursos(MatrizCurricular matrizCurricular) {
		if (matrizCurricular.getCursos() == null)
			return 0;
		return matrizCurricular.getCursos().size();
	}
	
}
