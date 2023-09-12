package br.com.profectum.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.profectum.dto.SemestreDTO;
import br.com.profectum.enums.ErrosEnum;
import br.com.profectum.enums.InfoEnums;
import br.com.profectum.exceptions.RegraNegocioException;
import br.com.profectum.model.Semestre;
import br.com.profectum.repositories.SemestreRepository;

@Service
public class SemestreService {
	private SemestreRepository repository;

	public SemestreService(SemestreRepository repository) {
		this.repository = repository;
	}

	public Semestre criarSemestre(Semestre semestre) {

		return repository.save(semestre);
	}

	public ResponseEntity<Object> listarTodosOsSemestres() {
		List<Semestre> semestres = verificarListaDeSemestres();
		if (semestres.size() == 0) 
			return ResponseEntity.ok().body(InfoEnums.INFO_001.getMensagem());
		
		return ResponseEntity.ok(semestres);
	}
	
	public Optional<Semestre> buscarSemestrePorNome(String nomeSemestre) {
		if (nomeSemestre.isEmpty() || nomeSemestre.isBlank()) 
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());
		
		Optional<Semestre> semestre = repository.findByNomeSemestre(nomeSemestre);
		if (!semestre.isPresent())
			throw new RegraNegocioException(ErrosEnum.ERRO_003.getMensagemErro());

		return semestre;
	}

	public Semestre atualizarSemestre(String nomeSemestre, Semestre semestreModificado) {
		return repository.save(semestreModificado);
	}

	public void deletarSemestre(String nomeSemestre) {
		if (nomeSemestre.isEmpty() || nomeSemestre.isBlank()) 
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());
		
		Semestre semestre = repository.findByNomeSemestre(nomeSemestre).get();
		if (semestre.equals(null))
			throw new RegraNegocioException(ErrosEnum.ERRO_002.getMensagemErro());

		repository.delete(semestre);
	}
	
	public Semestre converterDeDTO(SemestreDTO dto) {
		
		Semestre semestre = Semestre.builder()
				.nomeSemestre(dto.getNomeSemestre())
				.parcialHoras(dto.getParcialHoras())
				.disciplinas(dto.getDisciplinas()).build();

		return semestre;
	}
	
	public List<Semestre> verificarListaDeSemestres() {
		return repository.findAll();
	}
	
	public boolean verificarExistencia(String nomeSemestre){
		Optional<Semestre> semestre = repository.findByNomeSemestre(nomeSemestre);
		if(semestre.isPresent())
			return true;
		
		return false;
	}
	
}
