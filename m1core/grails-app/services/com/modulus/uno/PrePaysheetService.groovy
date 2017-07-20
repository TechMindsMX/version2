package com.modulus.uno

import grails.transaction.Transactional

class PrePaysheetService {

  @Transactional
  PrePaysheet savePrePaysheet(PrePaysheet prePaysheet) {
    prePaysheet.save()
    log.info "Prepaysheet saved: ${prePaysheet.dump()}"
    prePaysheet
  }

}
