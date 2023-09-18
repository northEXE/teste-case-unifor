package br.com.profectum.responseDTO;

/**
 * @author Wendel Ferreira de Mesquita
 * O design pattern DTO (Data Transfer Object) foi usado neste projeto pensando
 * no desacoplamento das entidades com a camada de cliente. Assim, tudo o que a camada
 * de cliente envia e recebe, são os objetos de transferênicia, e não as entidades em si,
 * mantendo um padrão de segurança maior, retirando a responsabilidade da camada de entidades
 * de se comunicar com o cliente, mantendo-se somemte em contato com as regras de negócio e a
 * base de dados.
 */

import br.com.profectum.enums.PeriodoEnum;
import br.com.profectum.model.Disciplina;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaResponseDTO {

	public DisciplinaResponseDTO(Disciplina disciplina) {
		this(disciplina.getIdDisciplina(), disciplina.getNomeDisciplina(), disciplina.getCargaHoraria(),
				disciplina.getProfessorResponsavel(), disciplina.getHorario(), disciplina.getDiasSemana(),
				disciplina.getPeriodo(), disciplina.getLocalizacao(), disciplina.getDescricao());
	}

	private Long idDisciplina;
	private String nomeDisciplina;
	private Integer cargaHoraria;
	private String professorResponsavel;
	private String horario;
	private String diasSemana;
	private PeriodoEnum periodo;
	private String localizacao;
	private String descricao;

}
