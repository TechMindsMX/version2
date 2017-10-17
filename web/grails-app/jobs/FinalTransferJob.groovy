package com.modulus.uno

import grails.util.Holders

class FinalTransferJob {

  ManagerApplicationService managerApplicationService

  static triggers = {
    cron name:'finalTransfer', cronExpression: Holders.config.finalTransfer
  }

  def group = "ModulusUno"
  def description = "Applying final transfer for all companies"

  def execute() {
    log.info "Initializing close operations for all companies"
    managerApplicationService.applyFinalTransferForAllCompanies()
  }
}
