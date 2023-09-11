package br.com.profectum.model;

import java.util.List;
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
public class MatrizCurricular {

	@Id
	@GeneratedValue(strategy =  GenerationType.UUID)
	@Column(name = "id_matriz_curricular")
	private UUID idMatrizCurricular;
	
	@Column(name = "nome_matriz_curricular")
	private String nomeMatrizCurricular;
	
	private List<Curso> cursos;
}
