package com.kt.safe2go.platform.common.log.vo;

public class TransactionThreadLocal {
	TransactionLogVo logVo;
	TransactionVo vo;
	String requestBody;
	
	public TransactionThreadLocal() {}
	public TransactionThreadLocal(TransactionLogVo logVo,TransactionVo vo ) {
		this.logVo = logVo;
		this.vo = vo;
	}
	public TransactionThreadLocal(TransactionLogVo logVo,TransactionVo vo,String requestBody) {
		this.logVo = logVo;
		this.vo = vo;
		this.requestBody = requestBody;
	}
	
	public TransactionLogVo getLogVo() {
		return logVo;
	}
	public void setLogVo(TransactionLogVo logVo) {
		this.logVo = logVo;
	}
	public TransactionVo getVo() {
		return vo;
	}
	public void setVo(TransactionVo vo) {
		this.vo = vo;
	}
	public String getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
	
	
}
