package br.com.profectum.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.profectum.dto.MatrizCurricularDTO;
import br.com.profectum.enums.ErrosEnum;
import br.com.profectum.enums.InfoEnums;
import br.com.profectum.exceptions.RegraNegocioException;
import br.com.profectum.model.MatrizCurricular;
import br.com.profectum.repositories.MatrizCurricularRepository;

@Service
public class MatrizCurricularService {

	private MatrizCurricularRepository repository;

	public MatrizCurricularService(MatrizCurricularRepository repository) {
		this.repository = repository;
	}

	public MatrizCurricular criarMatrizCurricular(MatrizCurricular matrizCurricular) {

		return repository.save(matrizCurricular);
	}

	public ResponseEntity<Object> listarTodosOsMatrizCurriculars() {
		List<MatrizCurricular> matrizCurriculars = verificarListaDeMatrizCurriculars();
		if (matrizCurriculars.size() == 0) 
			return ResponseEntity.ok().body(InfoEnums.INFO_001.getMensagem());
		
		return ResponseEntity.ok(matrizCurriculars);
	}
	
	public Optional<MatrizCurricular> buscarMatrizCurricularPorNome(String nomeMatrizCurricular) {
		if (nomeMatrizCurricular.isEmpty() || nomeMatrizCurricular.isBlank()) 
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());
		
		Optional<MatrizCurricular> matrizCurricular = repository.findByNomeMatrizCurricular(nomeMatrizCurricular);
		if (!matrizCurricular.isPresent())
			throw new RegraNegocioException(ErrosEnum.ERRO_003.getMensagemErro());

		return matrizCurricular;
	}

	public MatrizCurricular atualizarMatrizCurricular(String nomeMatrizCurricular, MatrizCurricular matrizCurricularModificado) {
		return repository.save(matrizCurricularModificado);
	}

	public void deletarMatrizCurricular(String nomeMatrizCurricular) {
		if (nomeMatrizCurricular.isEmpty() || nomeMatrizCurricular.isBlank()) 
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());
		
		MatrizCurricular matrizCurricular = repository.findByNomeMatrizCurricular(nomeMatrizCurricular).get();
		if (matrizCurricular.equals(null))
			throw new RegraNegocioException(ErrosEnum.ERRO_002.getMensagemErro());

		repository.delete(matrizCurricular);
	}
	
	public MatrizCurricular converterDeDTO(MatrizCurricularDTO dto) {
		
		MatrizCurricular matrizCurricular = MatrizCurricular.builder()
				.nomeMatrizCurricular(dto.getNomeMatrizCurricular())
				.cursos(dto.getCursos()).build();

		return matrizCurricular;
	}
	
	public List<MatrizCurricular> verificarListaDeMatrizCurriculars() {
		return repository.findAll();
	}
	
	public boolean verificarExistencia(String nomeMatrizCurricular){
		Optional<MatrizCurricular> matrizCurricular = repository.findByNomeMatrizCurricular(nomeMatrizCurricular);
		if(matrizCurricular.isPresent())
			return true;
		
		return false;
	}
	
}
