package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import com.modulus.uno.twoFactorAuth.TwoFactorAuthService

class TwoFactorAuthController {

  TwoFactorAuthService twoFactorAuthService

  def reSynchronizeTwoFactor() {
    log.info "Sincronizar otro dispositivo"
    render view:"reSynchronizeTwoFactor"
  }

  def reSynchronizeUser() {
    twoFactorAuthService.reSynchronizeUser(params.username, params.email)
    redirect controller:"login", action:"auth"
  }

}
