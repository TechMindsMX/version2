package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly=true)
class MachineController {

  static allowedMethods = [save: "POST", update: "PUT",delete:"DELETE"]

  MachineryLinkService machineryLinkService

  def index(){
    ArrayList<Map> entities = [] 
    machineryLinkService.getNamesOfClassesWithMachineryInterface().each{ entity ->
      entities << [key:entity,value:entity]
    }

    respond new Machine(),model:[entities:entities]
  }

}
