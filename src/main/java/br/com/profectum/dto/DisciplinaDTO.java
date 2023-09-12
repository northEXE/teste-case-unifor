package br.com.profectum.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisciplinaDTO {

	private String nomeDisciplina;
	private Integer cargaHoraria;
	private String professorResponsavel;
	private String localizacao;
	private String descricao;
	
}
