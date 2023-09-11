package br.com.profectum.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.UUID)
	@Column(name = "id_curso")
	private UUID idCurso;
	
	@Column(name = "nome_curso")
	private String nomeCurso;
	
	@OneToMany
	private List<Semestre> semestres;
}
