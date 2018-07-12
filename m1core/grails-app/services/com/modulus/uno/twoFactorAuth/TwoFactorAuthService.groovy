package com.modulus.uno.twoFactorAuth

import groovy.transform.CompileStatic
import grails.transaction.Transactional
import org.jboss.aerogear.security.otp.Totp
import com.modulus.uno.UserService
import com.modulus.uno.User

@Transactional
@CompileStatic
class TwoFactorAuthService implements TwoFactorAuthValidator {

  UserService userService = new UserService()

    @Transactional(readOnly = true)
    @Override
    boolean isValidCodeAndUserName(String code, String name) {
      log.info "Validate code and user name: ${code} - ${name}"
        User user = userService.findByUsername(name)
        if (!user) {
          log.info "User not found"
          return false
        }
      if (user.enable2FA) {
        log.info "Validating verification code"
        Totp totp = new Totp(user.key2FA)
        return (isValidLong(code) && totp.verify(code))
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

}
