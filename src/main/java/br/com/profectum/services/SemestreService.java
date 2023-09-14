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

import br.com.profectum.dto.SemestreDTO;
import br.com.profectum.enums.ErrosEnum;
import br.com.profectum.enums.InfoEnums;
import br.com.profectum.exceptions.RegraNegocioException;
import br.com.profectum.model.Disciplina;
import br.com.profectum.model.Semestre;
import br.com.profectum.repositories.SemestreRepository;

@Service
public class SemestreService {
	private SemestreRepository repository;
	private DisciplinaService disciplinaService;

	public SemestreService(SemestreRepository repository, DisciplinaService disciplinaService) {
		this.repository = repository;
		this.disciplinaService = disciplinaService;
	}

	public Semestre criarSemestre(Semestre semestre) {
		return repository.save(semestre);
	}

	public ResponseEntity<Object> listarTodosOsSemestres() {
		List<Semestre> semestres = verificarSemestres();
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

	public Optional<Semestre> buscarSemestrePorId(Long idSemestre) {
		if (idSemestre.toString().isEmpty() || idSemestre.toString().isBlank())
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());

		Optional<Semestre> semestre = repository.findById(idSemestre);
		if (!semestre.isPresent())
			throw new RegraNegocioException(ErrosEnum.ERRO_003.getMensagemErro());

		return semestre;
	}

	public Semestre atualizarSemestre(Long idSemestre, Semestre semestreModificado, SemestreDTO dto) {
		Optional<Semestre> semestre = buscarSemestrePorId(idSemestre);
		
		if(dto.getNomeSemestre() == null || dto.getCurso() == null) {
			semestreModificado.setNomeSemestre(semestre.get().getNomeSemestre());
			semestreModificado.setCurso(semestre.get().getCurso());
		}
		if (verificarDisciplinas(semestre.get()) != 0 && dto.getIdsDisciplinas() == null) {
			semestreModificado.setDisciplinas(semestre.get().getDisciplinas());
		} else if (verificarDisciplinas(semestre.get()) != 0 && dto.getIdsDisciplinas() != null) {
			semestreModificado.getDisciplinas().addAll(0, semestre.get().getDisciplinas());
			semestreModificado.setParcialHoras(0);
			calcularParcialHoras(semestreModificado.getDisciplinas(), semestreModificado);
		}
		return repository.save(semestreModificado);
	}

	public void deletarSemestre(Long idSemestre) {
		if (idSemestre.toString().isEmpty() || idSemestre.toString().isBlank())
			throw new RegraNegocioException(ErrosEnum.ERRO_006.getMensagemErro());

		Semestre semestre = repository.findById(idSemestre).get();
		if (semestre.equals(null))
			throw new RegraNegocioException(ErrosEnum.ERRO_002.getMensagemErro());

		repository.delete(semestre);
	}

	private List<Disciplina> adicionarDisciplinas(List<Long> idsDisciplinas) {
		if (disciplinaService.verificarListaDeDisciplinas().size() == 0)
			throw new RegraNegocioException(ErrosEnum.ERRO_004.getMensagemErro());
		List<Disciplina> disciplinas = new ArrayList<Disciplina>();
		idsDisciplinas.forEach(e -> {
			Optional<Disciplina> disciplina = disciplinaService.buscarDisciplinaPorId(e);
			disciplinas.add(disciplina.get());
		});

		return disciplinas;
	}

	public Semestre removerDisciplinas(List<Long> idsDisciplinas, Semestre semestre) {
		if (disciplinaService.verificarListaDeDisciplinas().size() == 0)
			throw new RegraNegocioException(ErrosEnum.ERRO_004.getMensagemErro());

		for (int i = 0; i < idsDisciplinas.size(); i++) {
			for (int j = 0; j < semestre.getDisciplinas().size(); j++) {
				if (semestre.getDisciplinas().get(j).getIdDisciplina() == idsDisciplinas.get(i)) {
					Integer parcialMenos = semestre.getDisciplinas().get(j).getCargaHoraria();
					semestre.setParcialHoras(semestre.getParcialHoras() - parcialMenos);
					semestre.getDisciplinas().remove(j);
				}
			}
		}

		return repository.save(semestre);
	}

	public Semestre converterDeDTO(SemestreDTO dto) {

		if (dto.getIdsDisciplinas() != null) {
			Semestre semestre = Semestre.builder()
					.nomeSemestre(dto.getNomeSemestre())
					.curso(dto.getCurso())
					.disciplinas(adicionarDisciplinas(dto.getIdsDisciplinas())).parcialHoras(0)
					.build();

			calcularParcialHoras(semestre.getDisciplinas(), semestre);
			return semestre;
		}
		
		Semestre semestre = Semestre.builder()
				.nomeSemestre(dto.getNomeSemestre())
				.curso(dto.getCurso())
				.parcialHoras(0).build();
		
		return semestre;
	}

	public List<Semestre> verificarSemestres() {
		return repository.findAll();
	}

	public boolean verificarExistencia(Long idSemestre) {
		Optional<Semestre> semestre = repository.findById(idSemestre);
		if (semestre.isPresent())
			return true;

		return false;
	}

	private Integer calcularParcialHoras(List<Disciplina> disciplinas, Semestre semestre) {
		if (semestre.getDisciplinas() == null || disciplinas == null) {
			semestre.setParcialHoras(0);
			return semestre.getParcialHoras();
		}

		disciplinas.forEach(e -> {
			semestre.setParcialHoras(semestre.getParcialHoras() + e.getCargaHoraria());
		});

		return semestre.getParcialHoras();
	}

	private Integer verificarDisciplinas(Semestre semestre) {
		if (semestre.getDisciplinas() == null)
			return 0;
		return semestre.getDisciplinas().size();
	}

}
