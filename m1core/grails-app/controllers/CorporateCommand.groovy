package com.modulus.uno

import grails.validation.Validateable

class CorporateCommand implements Validateable {

  String nameCorporate
  String corporateUrl
  boolean hasQuotationContract
  boolean hasCredit

  Corporate getCorporate(){
    new Corporate(
      nameCorporate: nameCorporate,
      corporateUrl: corporateUrl,
      hasQuotationContract: hasQuotationContract ?: false,
      hasCredit: hasCredit ?: false
    )
  }
}
