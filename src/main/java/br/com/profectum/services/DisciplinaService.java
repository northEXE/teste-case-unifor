package br.com.profectum.services;

import java.util.ArrayList;

/**
 * @author Wendel Ferreira de Mesquita
 * Nesta classe encontramos a camada de serviço. Como o projeto é um CRUD padrão usando Spring Data,
 * JPA e Hibernate, é uma solução padrão de manipulação dos dados, onde há tratamento de dados com chaves
 * estrangeiras, relações de N para N (como pode ser visto na camada de entidade) e manipulação de listas.
 * Fica como ponto de melhoria criar queries personalizadas para diminuir e/ou excluir o uso de estruturas
 * de repetição para a manipulação dos dados, afim de melhorar a complexidade de execução da aplicação.
 */

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.profectum.enums.ErrosEnum;
import br.com.profectum.enums.InfoEnums;
import br.com.profectum.exceptions.RegraNegocioException;
import br.com.profectum.model.Disciplina;
import br.com.profectum.repositories.DisciplinaRepository;
import br.com.profectum.requestDTO.DisciplinaRequestDTO;
import br.com.profectum.responseDTO.DisciplinaResponseDTO;

@Service
public class DisciplinaService {
	private DisciplinaRepository repository;

	public DisciplinaService(DisciplinaRepository repository) {
		this.repository = repository;
	}

	public Disciplina criarDisciplina(Disciplina disciplina) {
		return repository.save(disciplina);
	}

	public ResponseEntity<Object> listarTodasAsDisciplinas() {
		List<Disciplina> disciplinas = verificarListaDeDisciplinas();
		if (disciplinas.size() == 0) 
			return ResponseEntity.ok().body(InfoEnums.INFO_001.getMensagem());
		
		List<DisciplinaResponseDTO> dto = new ArrayList<DisciplinaResponseDTO>();
		disciplinas.forEach(e -> {
			dto.add(converterParaDTO(e));
		});
		
		return ResponseEntity.ok(dto);
	}
	
	public Optional<Disciplina> buscarDisciplinaPorId(Long idDisciplina) {
		if (idDisciplina.toString().isEmpty() || idDisciplina.toString().isBlank()) 
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());
		
		Optional<Disciplina> disciplina = repository.findById(idDisciplina);
		if (!disciplina.isPresent())
			throw new RegraNegocioException(ErrosEnum.ERRO_003.getMensagemErro());

		return disciplina;
	}

	public Disciplina atualizarDisciplina(Long idDisciplina, Disciplina disciplinaModificado) {
		return repository.save(disciplinaModificado);
	}

	public void deletarDisciplina(Long idDisciplina) {
		if (idDisciplina.toString().isEmpty() || idDisciplina.toString().isBlank()) 
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());
		
		Disciplina disciplina = repository.findById(idDisciplina).get();
		if (disciplina.equals(null))
			throw new RegraNegocioException(ErrosEnum.ERRO_002.getMensagemErro());

		repository.delete(disciplina);
	}
	
	public Disciplina converterDeDTO(DisciplinaRequestDTO dto) {
		 
		Disciplina disciplina = Disciplina.builder()
				.nomeDisciplina(dto.getNomeDisciplina())
				.cargaHoraria(dto.getCargaHoraria())
				.professorResponsavel(dto.getProfessorResponsavel())
				.horario(dto.getHorario())
				.diasSemana(dto.getDiasSemana())
				.periodo(dto.getPeriodo())
				.localizacao(dto.getLocalizacao())
				.descricao(dto.getDescricao()).build();
				
		return disciplina;
	}
	
	public DisciplinaResponseDTO converterParaDTO(Disciplina disciplina) {
		return new DisciplinaResponseDTO(disciplina);
	}
	
	public List<Disciplina> verificarListaDeDisciplinas() {
		return repository.findAll();
	}
	
	public boolean verificarExistencia(Long idDisciplina){
		Optional<Disciplina> disciplina = repository.findById(idDisciplina);
		if(disciplina.isPresent())
			return true;
		
		return false;
	}
}
