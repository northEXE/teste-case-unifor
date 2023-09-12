package br.com.profectum.exceptions;

public class RegraNegocioException extends RuntimeException {

	private static final long serialVersionUID = 215L;

	public RegraNegocioException(String mensagem) {
		super(mensagem);
	}

}
