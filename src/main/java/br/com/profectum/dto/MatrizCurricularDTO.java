package br.com.profectum.dto;

import java.util.List;

import br.com.profectum.model.Curso;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatrizCurricularDTO {

	private String nomeMatrizCurricular;
	private List<Curso> cursos;

}
