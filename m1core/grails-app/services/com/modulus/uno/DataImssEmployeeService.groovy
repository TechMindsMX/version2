package com.modulus.uno

import grails.transaction.Transactional

class DataImssEmployeeService {

  @Transactional
  DataImssEmployee saveDataImss(DataImssEmployee dataImss) {
    dataImss.save()
    log.info "Data Imss saving: ${dataImss?.dump()}"
    dataImss
  }

}
