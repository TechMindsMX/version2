package com.modulus.uno

enum CorporateStatus {
	ENABLED("corporate.enabled"), 
	DISABLED("corporate.disabled")

	private final String code

	CorporateStatus(String code){
		this.code = code
	}

	String getCode(){ return this.code }
}