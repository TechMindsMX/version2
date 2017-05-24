package com.modulus.uno

import grails.util.Holders

class FixedCommissionJob {

  ManagerApplicationService managerApplicationService

  static triggers = {
    cron name:'fixedCommission', cronExpression: Holders.config.jobs.fixedCommission
  }

  def group = "ModulusUno"
  def description = "Applying fixed commission for all companies"

  def execute() {
    log.info "Applying fixed commission for all companies"
    managerApplicationService.applyFixedCommissionForAllCompanies()
  }
}
