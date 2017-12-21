package com.modulus.uno

import grails.validation.Validateable

class CorporateCommand implements Validateable {

  String nameCorporate
  String corporateUrl
  boolean hasQuotationContract

  Corporate getCorporate(){
    new Corporate(
      nameCorporate: nameCorporate,
      corporateUrl: corporateUrl,
      hasQuotationContract: hasQuotationContract ?: false
    )
  }
}
