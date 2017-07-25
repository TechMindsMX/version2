package com.modulus.uno.menu

import com.modulus.uno.Role
import grails.core.GrailsApplication

class MenuOperationsController {

  def index() {
    log.debug "${Role.list()}"
    log.debug "${Role.getClassesWitMenues(grailsApplication)}"
    log.debug "${Role.cheers('Juan')}"
    [:]
  }
}
