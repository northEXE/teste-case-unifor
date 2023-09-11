package br.com.profectum.model;

import java.util.Date;
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
public class Semestre {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.UUID)
	@Column(name = "id_semestre")
	private UUID idSemestre;
	
	@Column(name = "nome_semestre")
	private String nomeSemestre;
	
	@Column(name = "inicio_periodo")
	private Date inicio_periodo;
	
	@Column(name = "final_periodo")
	private Date finalPeriodo;
	
	@Column (name = "parcial_horas")
	private Integer parcialHoras;
	
	@OneToMany
	private List<Disciplina> disciplinas;
}
