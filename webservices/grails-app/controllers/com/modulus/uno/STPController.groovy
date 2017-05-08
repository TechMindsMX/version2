package com.modulus.uno

import com.github.rahulsom.swaggydoc.*
import com.wordnik.swagger.annotations.*
import static org.springframework.http.HttpStatus.*
import grails.converters.JSON

@Api
class STPController {

  StpDepositService stpDepositService

  static allowedMethods = [stpDepositNotification:"POST"]

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
}
