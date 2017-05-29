package com.modulus.uno

import com.github.rahulsom.swaggydoc.*
import com.wordnik.swagger.annotations.*
import static org.springframework.http.HttpStatus.*
import grails.converters.JSON

@Api
class STPController {

  StpDepositService stpDepositService
  StpService stpService
  CompanyService companyService
  ManagerApplicationService managerApplicationService

  static allowedMethods = [stpDepositNotification:"POST", stpDepositNotificationJson:"POST", stpConciliationCompany:"POST"]

  @ApiOperation(value = "Notify deposit",
    response = StpDepositSwagger, hidden=true)
  @ApiResponses([
    @ApiResponse(code = 422, message = 'Bad Entity Received'),
    @ApiResponse(code = 201, message = 'Created')
  ])
  @ApiImplicitParams([
    @ApiImplicitParam(name = 'body', paramType = 'body', required = true, dataType = 'StpDepositSwagger')
  ])
  def stpDepositNotificationJson(StpDepositSwagger stpDepositSwagger) {
    try {
      StpDeposit stpDeposit = stpDepositSwagger.createStpDeposit()
      log.info "Receiving notificaction: ${stpDeposit.dump()}"
      def result = stpDepositService.notificationDepositFromStp(stpDeposit)
      respond result, status: 201, formats: ['json']
    }catch (Exception ex) {
      response.sendError(422, "Missing parameters from notification, error: ${ex.message}")
    }
  }

  @SwaggySave(extraParams = [
    @ApiImplicitParam(name = 'notification', value = '', dataType = 'string',paramType = 'query')
  ])
  def stpDepositNotification() {
    try {
      log.info "Receiving notificaction: ${params.notification}"
        def result = stpDepositService.notificationDepositFromStpOld(params.notification)
        respond result, status: 201, formats: ['json']
    } catch (Exception ex) {
      response.sendError(422, "Missing parameters from notification, error: ${ex.message}")
    }
  }

  @ApiOperation(value = "Conciliation Company",
    response = StpConciliationSwagger, hidden=true)
  @ApiResponses([
    @ApiResponse(code = 422, message = 'Bad Entity Received'),
    @ApiResponse(code = 201, message = 'Ok')
  ])
  @ApiImplicitParams([
    @ApiImplicitParam(name = 'body', paramType = 'body', required = true, dataType = 'StpConciliationSwagger')
  ])
  def stpConciliationCompany(StpConciliationSwagger stpConciliationSwagger) {
    try {
      log.info "Get conciliation for company"
      Company company = Company.get(stpConciliationSwagger.company)
      Period period = new Period(init:Date.parse("yyyy-MM-dd HH:mm:ss", stpConciliationSwagger.initDate), end:Date.parse("yyyy-MM-dd HH:mm:ss", stpConciliationSwagger.endDate))
      String status = companyService.executeOperationsCloseForCompany(company)
      Map result = [status:status]
      respond result, status: 201, formats: ['json']
    }catch (Exception ex) {
      response.sendError(422, "Missing parameters from notification, error: ${ex.message}")
    }
  }

  @ApiOperation(value = "Execute close day for all companies")
  @ApiResponses([
    @ApiResponse(code = 422, message = 'Bad Entity Received'),
    @ApiResponse(code = 201, message = 'Ok')
  ])
  def processFinalTransferForAllCompanies() {
    try {
      log.info "Initializing close operations for all companies"
      String status= managerApplicationService.applyFinalTransferForAllCompanies()
      Map result = [status:status]
      respond result, status: 201, formats: ['json']
    }catch (Exception ex) {
      response.sendError(422, "Missing parameters from notification, error: ${ex.message}")
    }
  }

}
