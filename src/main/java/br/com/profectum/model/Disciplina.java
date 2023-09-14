package br.com.profectum.model;

import br.com.profectum.enums.PeriodoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Disciplina {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Column(name = "id_disciplina")
	private Long idDisciplina;
	
	@Column(name = "nome_disciplina")
	private String nomeDisciplina;
	
	@Column(name = "professor_responsavel")
	private String professorResponsavel;
	
	@Column(name = "horario")
	private String horario;
	
	@Column(name = "diasSemana")
	private String diasSemana;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "periodo")
	private PeriodoEnum periodo;
	
	@Column(name = "localizacao")
	private String localizacao;
	
	@Column(name = "carga_horaria")
	private Integer cargaHoraria;
	
	@Column(name = "descricao")
	private String descricao;
}
