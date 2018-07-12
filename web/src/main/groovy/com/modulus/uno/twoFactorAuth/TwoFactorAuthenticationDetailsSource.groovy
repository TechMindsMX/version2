package com.modulus.uno.twoFactorAuth

import groovy.transform.CompileStatic
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource

import javax.servlet.http.HttpServletRequest

@CompileStatic
class TwoFactorAuthenticationDetailsSource extends WebAuthenticationDetailsSource {

  @Override
  WebAuthenticationDetails buildDetails(HttpServletRequest context) {
    def details = new TwoFactorAuthenticationDetails(context)
  }

}
