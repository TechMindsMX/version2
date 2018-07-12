package com.modulus.uno.twoFactorAuth

import groovy.transform.CompileStatic
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.Authentication
import org.jboss.aerogear.security.otp.Totp

@CompileStatic
class TwoFactorAuthenticationProvider extends DaoAuthenticationProvider {
  
  TwoFactorAuthValidator twoFactorAuthValidator

  protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    super.additionalAuthenticationChecks(userDetails, authentication)
    Object details = authentication.details
    if ( !(details instanceof TwoFactorAuthenticationDetails) ) {
      logger.debug("Authentication failed: authenticationToken principal is not a TwoFactorPrincipal");
      throw new BadCredentialsException(messages.getMessage(
            "AbstractUserDetailsAuthenticationProvider.badCredentials",
            "Bad credentials"))
    }
    def twoFactorAuthenticationDetails = details as TwoFactorAuthenticationDetails
    if ( !twoFactorAuthValidator.isValidCodeAndUserName(twoFactorAuthenticationDetails.verificationCode, authentication.name) ) {
      logger.debug("Authentication failed: coordiante note valid")
      throw new BadCredentialsException(messages.getMessage(
            "AbstractUserDetailsAuthenticationProvider.badCredentials",
            "Bad credentials"))
    }
  }

}

