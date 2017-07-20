package com.modulus.uno

import grails.transaction.Transactional

class PrePaysheetService {

  @Transactional
  PrePaysheet savePrePaysheet(PrePaysheet prePaysheet) {
    prePaysheet.save()
    log.info "Prepaysheet saved: ${prePaysheet.dump()}"
    prePaysheet
  }

  Map getListAndCountPrePaysheetsForCompany(Company company, Map params) {
    Map prePaysheets = [:]
    prePaysheets.list = PrePaysheet.findAllByCompany(company, params)
    prePaysheets.total = PrePaysheet.countByCompany(company)
    prePaysheets
  }
}
