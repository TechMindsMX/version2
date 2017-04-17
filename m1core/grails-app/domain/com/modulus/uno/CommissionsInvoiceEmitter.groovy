package com.modulus.uno

class CommissionsInvoiceEmitter {

  String uuid = UUID.randomUUID().toString().replace('-','')[0..15]
  String rfc
  String businessName
  Address fiscalAddress
  String stpClabe

}
