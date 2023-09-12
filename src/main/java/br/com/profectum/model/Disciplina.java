package br.com.profectum.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	@GeneratedValue(strategy =  GenerationType.UUID)
	@Column(name = "id_disciplina")
	private UUID idDisciplina;
	
	@Column(name = "nome_disciplina")
	private String nomeDisciplina;
	
	@Column(name = "professor_responsavel")
	private String professorResponsavel;
	
	@Column(name = "localizacao")
	private String localizacao;
	
	@Column(name = "carga_horaria")
	private Integer cargaHoraria;
	
	@Column(name = "descricao")
	private String descricao;
}
