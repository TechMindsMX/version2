package com.modulus.uno

class SalesCaptureController {

  def salesCaptureService

  def index(){
    render view:'index'
  }

  //def save(SalesCaptureCommand command){
  def save(){
    log.info "*"*20
    log.info params
    //log.info command
    //salesCaptureService.sendNotify(command)
  }

}

