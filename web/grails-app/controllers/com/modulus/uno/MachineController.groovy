package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly=true)
class MachineController {

  static allowedMethods = [save: "POST", update: "PUT",delete:"DELETE"]

  MachineryLinkService machineryLinkService

  def index(){
    render view:"index",model:[entities:machineryLinkService.getClassesWithMachineryInterface()]
  }

  def create(){
    String entity = params.entity ? "${params.entity[0].toLowerCase()}${params.entity[1..params.entity.size()-1]}" : ""
    render view:"create",model:[entity:g.message(code:"${entity}.name")]
  }

}
