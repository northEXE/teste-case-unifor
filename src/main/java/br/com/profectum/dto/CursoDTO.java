package br.com.profectum.dto;

import java.util.List;

import br.com.profectum.model.Semestre;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CursoDTO {
	
	private String nomeCurso;
	private List<Semestre> semestres;
}
