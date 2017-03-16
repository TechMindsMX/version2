package com.modulus.uno

class SalesCaptureController {

  def salesCaptureService

  def index(){
    render view:'index'
  }

  //def save(SalesCaptureCommand command){
  def save(){
    log.info "*"*20
    log.debug params.name
    log.info "*"*20

    //log.info command
    //salesCaptureService.sendNotify(command)
  }

}

