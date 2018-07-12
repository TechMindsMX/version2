package com.modulus.uno

class TwoFactorAuthController {

  def reSynchronizeTwoFactor() {
    log.info "Sincronizar otro dispositivo"
    render view:"reSynchronizeTwoFactor"
  }

  def reSynchronizeUser() {
    twoFactorAuthService.reSynchronizeUser(params.username, params.email)
    render view:"login/auth"
  }

}
