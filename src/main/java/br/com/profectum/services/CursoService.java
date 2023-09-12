package br.com.profectum.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.profectum.dto.CursoDTO;
import br.com.profectum.enums.ErrosEnum;
import br.com.profectum.enums.InfoEnums;
import br.com.profectum.exceptions.RegraNegocioException;
import br.com.profectum.model.Curso;
import br.com.profectum.repositories.CursoRepository;

@Service
public class CursoService {
	private CursoRepository repository;

	public CursoService(CursoRepository repository) {
		this.repository = repository;
	}

	public Curso criarCurso(Curso curso) {
		return repository.save(curso);
	}

	public ResponseEntity<Object> listarTodosOsCursos() {
		List<Curso> cursos = verificarListaDeCursos();
		if (cursos.size() == 0) 
			return ResponseEntity.ok().body(InfoEnums.INFO_001.getMensagem());
		
		return ResponseEntity.ok(cursos);
	}
	
	public Optional<Curso> buscarCursoPorId(UUID idCurso) {
		if (idCurso.toString().isEmpty() || idCurso.toString().isBlank()) 
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());
		
		Optional<Curso> curso = repository.findById(idCurso);
		if (!curso.isPresent())
			throw new RegraNegocioException(ErrosEnum.ERRO_003.getMensagemErro());

		return curso;
	}

	public Curso atualizarCurso(UUID idCurso, Curso cursoModificado) {
		return repository.save(cursoModificado);
	}

	public void deletarCurso(UUID idCurso) {
		if (idCurso.toString().isEmpty() || idCurso.toString().isBlank()) 
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());
		
		Curso curso = repository.findById(idCurso).get();
		if (curso.equals(null))
			throw new RegraNegocioException(ErrosEnum.ERRO_002.getMensagemErro());

		repository.delete(curso);
	}
	
	public Curso converterDeDTO(CursoDTO dto) {
		 
		Curso curso = Curso.builder()
				.nomeCurso(dto.getNomeCurso())
				.semestres(dto.getSemestres()).build();
				
		return curso;
	}
	
	public List<Curso> verificarListaDeCursos() {
		return repository.findAll();
	}
	
	public boolean verificarExistencia(UUID idCurso){
		Optional<Curso> curso = repository.findById(idCurso);
		if(curso.isPresent())
			return true;
		
		return false;
	}
}
