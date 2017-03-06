package com.modulus.uno

enum CompanyStatus {

  CREATED("company.created"),
  VALIDATE("company.validate"),
  ACCEPTED("company.accepted"),
  REJECTED("company.rejected")

  private final String code

  CompanyStatus(String code){
    this.code = code
  }

  String getCode(){ return this.code }
}
