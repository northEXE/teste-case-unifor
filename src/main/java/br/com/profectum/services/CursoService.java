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

import br.com.profectum.dto.CursoDTO;
import br.com.profectum.enums.ErrosEnum;
import br.com.profectum.enums.InfoEnums;
import br.com.profectum.exceptions.RegraNegocioException;
import br.com.profectum.model.Curso;
import br.com.profectum.model.Semestre;
import br.com.profectum.repositories.CursoRepository;

@Service
public class CursoService {
	private CursoRepository repository;
	private SemestreService semestreService;

	public CursoService(CursoRepository repository, SemestreService semestreService) {
		this.repository = repository;
		this.semestreService = semestreService;
	}

	public Curso criarCurso(Curso curso) {
		return repository.save(curso);
	}

	public ResponseEntity<Object> listarTodosOsCursos() {
		List<Curso> cursos = verificarCursos();
		if (cursos.size() == 0)
			return ResponseEntity.ok().body(InfoEnums.INFO_001.getMensagem());

		return ResponseEntity.ok(cursos);
	}

	public Optional<Curso> buscarCursoPorId(Long idCurso) {
		if (idCurso.toString().isEmpty() || idCurso.toString().isBlank())
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());

		Optional<Curso> curso = repository.findById(idCurso);
		if (!curso.isPresent())
			throw new RegraNegocioException(ErrosEnum.ERRO_003.getMensagemErro());

		return curso;
	}

	public Curso atualizarCurso(Long idCurso, Curso cursoModificado, CursoDTO dto) {
		Optional<Curso> curso = buscarCursoPorId(idCurso);
		if(dto.getNomeCurso() == null) {
			cursoModificado.setNomeCurso(curso.get().getNomeCurso());
		}
		if (verificarSemestres(curso.get()) != 0 && dto.getIdsSemestres() == null) {
			cursoModificado.setSemestres(curso.get().getSemestres());
		} else if (verificarSemestres(curso.get()) != 0 && dto.getIdsSemestres() != null) {
			cursoModificado.getSemestres().addAll(0, curso.get().getSemestres());
		}
		return repository.save(cursoModificado);
	}

	private List<Semestre> adicionarSemestres(List<Long> idsSemestres) {
		if (semestreService.verificarSemestres().size() == 0)
			throw new RegraNegocioException(ErrosEnum.ERRO_004.getMensagemErro());
		
		List<Semestre> semestres = new ArrayList<Semestre>();
		idsSemestres.forEach(e -> {
			Optional<Semestre> semestre = semestreService.buscarSemestrePorId(e);
			semestres.add(semestre.get());
		});

		return semestres;
	}

	public void deletarCurso(Long idCurso) {
		if (idCurso.toString().isEmpty() || idCurso.toString().isBlank())
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());

		Curso curso = repository.findById(idCurso).get();
		if (curso.equals(null))
			throw new RegraNegocioException(ErrosEnum.ERRO_002.getMensagemErro());

		repository.delete(curso);
	}

	public Curso converterDeDTO(CursoDTO dto) {

		if (dto.getIdsSemestres() != null) {
			Curso curso = Curso.builder()
					.nomeCurso(dto.getNomeCurso())
					.semestres(adicionarSemestres(dto.getIdsSemestres())).build();
			return curso;
		}
		Curso curso = Curso.builder().nomeCurso(dto.getNomeCurso()).build();
		return curso;
	}

	public List<Curso> verificarCursos() {
		return repository.findAll();
	}

	public boolean verificarExistencia(Long idCurso) {
		Optional<Curso> curso = repository.findById(idCurso);
		if (curso.isPresent())
			return true;

		return false;
	}
	private Integer verificarSemestres(Curso curso) {
		if (curso.getSemestres() == null)
			return 0;
		return curso.getSemestres().size();
	}
}
