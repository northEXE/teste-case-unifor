package br.com.profectum.requestDTO;

/**
 * @author Wendel Ferreira de Mesquita
 * O design pattern DTO (Data Transfer Object) foi usado neste projeto pensando
 * no desacoplamento das entidades para receber os dados do cliente.
 * Neste projeto, o DTO não está sendo usado nas ResponseEntities porque não
 * foi usado um campo externalId para que as entidades sejam manipuladas nos endpoints.
 * Fica como ponto de melhoria a adição de um campo extenralId e usar objetos DTO na saída
 * de dados, para desacoplar ainda mais a entidade da visão do cliente.
 */

import br.com.profectum.enums.PeriodoEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisciplinaRequestDTO {

	private String nomeDisciplina;
	private Integer cargaHoraria;
	private String professorResponsavel;
	private String horario;
	private String diasSemana;
	private PeriodoEnum periodo;
	private String localizacao;
	private String descricao;
	
}
