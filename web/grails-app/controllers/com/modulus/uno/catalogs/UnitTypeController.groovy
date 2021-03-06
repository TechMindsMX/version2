package com.modulus.uno.catalogs

import com.modulus.uno.Company
import grails.transaction.Transactional

@Transactional(readOnly = true)
class UnitTypeController {
  
  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def index() {
    params.max = 25
    params.sort = "name"
    Company company = Company.get(session.company)
    [unitTypeList:UnitType.findAllByCompany(company, params), unitTypeCount:UnitType.countByCompany(company)]
  }

  def create() {
    respond new UnitType()
  }

  @Transactional
  def save(UnitType unitType) {
    if (unitType == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if (unitType.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond unitType.errors, view:'create'
      return
    }

    unitType.save flush:true

    redirect action:"index"
  }

  def show(UnitType unitType) {
    respond unitType
  }

  def edit(UnitType unitType) {
    respond unitType
  }

  @Transactional
  def update(UnitType unitType) {
    if (unitType == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if (unitType.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond unitType.errors, view:'create'
      return
    }

    unitType.save flush:true

    redirect action:"index"
  }

}
