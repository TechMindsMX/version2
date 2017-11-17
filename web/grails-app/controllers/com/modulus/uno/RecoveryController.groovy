package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

class RecoveryController {

  static allowedMethods = [actionPassword:"POST", save:"POST", update:"POST", activeAccountOfLegalRepresentative:"POST"]

  def recoveryService

  def index() {}

  def show() {
    recoveryService.obtainRegistrationCodeForToken(params.token)
    respond OK
  }

  def forgotPassword() {}

  @Transactional
  def save() {
    String email = params.email
    String username = params.username
    try {
    recoveryService.generateRegistrationCodeForUsernameAndEmail(username, email)
    } catch (Exception e) {
      flash.message = e.message
      redirect action:'forgotPassword'
      return
    }

    flash.message = g.message(code: 'login.email')
    redirect controller:'login', action:'auth'
  }

  @Transactional
  def update(ChangePasswordCommand command) {
    log.info "Change password for token: ${command.dump()}"
    if(command.hasErrors()) {
      respond command.errors, view:'show', id:params.id
      return
    }
    recoveryService.changePasswordForToken(params.id, command.password)
    flash.message = g.message(code: 'login.change.password')
    redirect controller:'login', action:'auth'
  }

  @Transactional
  def activeAccountOfLegalRepresentative() {
    def statusToken = recoveryService.activateAccountByToken(params.token)
    if (statusToken) {
      flash.message = g.message(code: 'active.account.false')
      redirect controller:'login', action:'auth'
    }
    render view:"activePassword", model:['token':params.token]
  }

  @Transactional
  def activePassword(ChangePasswordCommand command) {
    if (command.hasErrors()) {
      render view:"activePassword",model:['token':params.id,'errors':command.errors]
    }
    recoveryService.activeAccountAndChangePassword(params.id,command.password)
    redirect controller:'login', action:'auth'
  }

}
