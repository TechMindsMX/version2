package com.modulus.uno.twoFactorAuth

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.springframework.security.web.authentication.WebAuthenticationDetails

import javax.servlet.http.HttpServletRequest

@Canonical
@CompileStatic
class TwoFactorAuthenticationDetails extends WebAuthenticationDetails {

  String verificationCode

  TwoFactorAuthenticationDetails(HttpServletRequest request) {
    super(request)
    verificationCode = request.getParameter("code")
  }

}
