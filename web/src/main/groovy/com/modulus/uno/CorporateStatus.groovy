package com.modulus.uno

enum CorporateStatus {
	ENABLED("HABILITADO"),
  DISABLED("DESHABILITADO")

	private final String code

	CorporateStatus(String code){
		this.code = code
	}

	String getCode(){ return this.code }
}
