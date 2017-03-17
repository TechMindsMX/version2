package com.modulus.uno

class SalesCaptureController {

  def salesCaptureService

  def index(){
    render view:'index'
  }

  //def save(SalesCaptureCommand command){
  def save(SalesCaptureCommand salesCaptureCommand){
    log.info "*"*20
    salesCaptureCommand.articles.each{
      log.debug(it.dump())
    }
    log.info "*"*20

    //log.info command
    //salesCaptureService.sendNotify(command)
    render "Probando"
  }

}

