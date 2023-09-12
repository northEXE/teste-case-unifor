package br.com.profectum.utils;

import org.springframework.http.ResponseEntity;

import br.com.profectum.enums.ErrosEnum;

public class ResponseErrosUtil {
	public static ResponseEntity<Object> respostaErro002(){
		return ResponseEntity.badRequest().body(ErrosEnum.ERRO_002.getMensagemErro());
	}
	
	public static ResponseEntity<Object> respostaErro004(){
		return ResponseEntity.badRequest().body(ErrosEnum.ERRO_004.getMensagemErro());
	}
	
	public static ResponseEntity<Object> respostaErro010(){
		return ResponseEntity.badRequest().body(ErrosEnum.ERRO_010.getMensagemErro());
	}
}
