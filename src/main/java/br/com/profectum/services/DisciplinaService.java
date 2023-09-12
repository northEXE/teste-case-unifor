package br.com.profectum.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.profectum.dto.DisciplinaDTO;
import br.com.profectum.enums.ErrosEnum;
import br.com.profectum.enums.InfoEnums;
import br.com.profectum.exceptions.RegraNegocioException;
import br.com.profectum.model.Disciplina;
import br.com.profectum.repositories.DisciplinaRepository;

@Service
public class DisciplinaService {
	private DisciplinaRepository repository;

	public DisciplinaService(DisciplinaRepository repository) {
		this.repository = repository;
	}

	public Disciplina criarDisciplina(Disciplina disciplina) {
		return repository.save(disciplina);
	}

	public ResponseEntity<Object> listarTodosOsDisciplinas() {
		List<Disciplina> disciplinas = verificarListaDeDisciplinas();
		if (disciplinas.size() == 0) 
			return ResponseEntity.ok().body(InfoEnums.INFO_001.getMensagem());
		
		return ResponseEntity.ok(disciplinas);
	}
	
	public Optional<Disciplina> buscarDisciplinaPorId(UUID idDisciplina) {
		if (idDisciplina.toString().isEmpty() || idDisciplina.toString().isBlank()) 
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());
		
		Optional<Disciplina> disciplina = repository.findById(idDisciplina);
		if (!disciplina.isPresent())
			throw new RegraNegocioException(ErrosEnum.ERRO_003.getMensagemErro());

		return disciplina;
	}

	public Disciplina atualizarDisciplina(UUID idDisciplina, Disciplina disciplinaModificado) {
		return repository.save(disciplinaModificado);
	}

	public void deletarDisciplina(UUID idDisciplina) {
		if (idDisciplina.toString().isEmpty() || idDisciplina.toString().isBlank()) 
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());
		
		Disciplina disciplina = repository.findById(idDisciplina).get();
		if (disciplina.equals(null))
			throw new RegraNegocioException(ErrosEnum.ERRO_002.getMensagemErro());

		repository.delete(disciplina);
	}
	
	public Disciplina converterDeDTO(DisciplinaDTO dto) {
		 
		Disciplina disciplina = Disciplina.builder()
				.nomeDisciplina(dto.getNomeDisciplina())
				.cargaHoraria(dto.getCargaHoraria())
				.descricao(dto.getDescricao()).build();
				

		return disciplina;
	}
	
	public List<Disciplina> verificarListaDeDisciplinas() {
		return repository.findAll();
	}
	
	public boolean verificarExistencia(UUID idDisciplina){
		Optional<Disciplina> disciplina = repository.findById(idDisciplina);
		if(disciplina.isPresent())
			return true;
		
		return false;
	}
}
