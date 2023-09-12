package br.com.profectum.dto;

import java.util.List;

import br.com.profectum.model.Disciplina;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SemestreDTO {
	
	private String nomeSemestre;
	private Integer parcialHoras;
	private List<Disciplina> disciplinas;
}
