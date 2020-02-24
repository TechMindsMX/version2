package com.modulus.uno.credit

enum PortfolioManagementType {
  OUTSTANDING_BALANCE("Saldo Insoluto")

  final String value

  PortfolioManagementType(String value){
    this.value = value
  }

  String getValue(){
    value
  }
}
