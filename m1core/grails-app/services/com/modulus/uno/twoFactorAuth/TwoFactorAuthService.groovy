package com.modulus.uno.twoFactorAuth

import groovy.transform.CompileStatic
import grails.transaction.Transactional
import com.modulus.uno.UserService
import com.modulus.uno.EmailSenderService
import com.modulus.uno.User
import com.modulus.uno.UserNotFoundException
import com.modulus.uno.AccountNoActivatedException

@Transactional
@CompileStatic
class TwoFactorAuthService implements TwoFactorAuthValidator {

  UserService userService = new UserService()
  AuthenticatorService authenticatorService = new AuthenticatorService()
  EmailSenderService emailSenderService = new EmailSenderService()

    @Transactional(readOnly = true)
    @Override
    boolean isValidCodeAndUserName(String code, String name) {
      log.error "Validate code and user name: ${code} - ${name}"
        User user = userService.findByUsername(name)
        if (!user) {
          log.error "User not found"
          return false
        }
      if (user.enable2FA) {
        log.error "Validating verification code"
        return (isValidLong(code) && authenticatorService.isValidToken(user.key2FA, code.toInteger()))
      }
      true
    }

  boolean isValidLong(String code) {
    try {
      Long.parseLong(code);
    } catch (NumberFormatException e) {
      return false
    }
    true
  }

  def reSynchronizeUser(String username, String email) {
    User user = userService.findUserFromUsernameAndEmail(username, email)
    if(!user) throw new UserNotFoundException("Usuario no encontrado")
    if(!user.enabled) throw new AccountNoActivatedException("La cuenta está deshabilitada")

    userService.generateKey2FA(user)
    userService.enableTwoFactor(user)
    String qrUrl = userService.generateQRAuthenticatorUrl(user)
    emailSenderService.sendEmailForTwoFactorAuth(user, qrUrl)
  }
}
