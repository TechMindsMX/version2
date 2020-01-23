package com.modulus.uno.credit

import grails.validation.Validateable

class CreditCommand implements Validateable {

  Long id
  String name
  ProductType productType
  PortfolioManagementType portfolioManagementType
  FrequencyType frequencyType
  CreditLineType creditLineType
  DispersionType dispersionType
  Boolean enabled

  static constraints = {
    name blank:false, nullable:false
  }

  Credit createCredit() {
    new Credit(
      name: this.name,
      productType: this.productType,
      portfolioManagementType: this.portfolioManagementType,
      frequencyType: this.frequencyType,
      creditLineType: this.creditLineType,
      dispersionType: this.dispersionType,
      enabled: this.enabled
    )
  }

}
