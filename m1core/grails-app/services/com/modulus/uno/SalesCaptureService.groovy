package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class SalesCaptureService {

  def notifyService

  def sendNotify(SalesCaptureCommand command){
    def emails = ["carlo@makingdevs.com", "brandon@makingdevs.com"]
    def idEmailer ="Pendiente"
    def paramsEmailer = prepareDataForEmailer(command)
    notifyService.sendEmailNotifications(emails, idEmailer, paramsEmailer)
  }

  def prepareDataForEmailer(SaleCaptureCommand command){
  }

}
