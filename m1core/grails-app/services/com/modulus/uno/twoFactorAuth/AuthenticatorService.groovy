package com.modulus.uno.twoFactorAuth

import com.warrenstrange.googleauth.GoogleAuthenticatorKey
import com.warrenstrange.googleauth.GoogleAuthenticator

class AuthenticatorService {

  String generateKey(){
    GoogleAuthenticator gAuth = new GoogleAuthenticator()
    final GoogleAuthenticatorKey key = gAuth.createCredentials()
    key.getKey()
  }

  Boolean isValidToken(String key, Integer token){
    log.error "Validating token for key ${key} and token ${token}"
    GoogleAuthenticator gAuth = new GoogleAuthenticator()
    def result = gAuth.authorize(key, token)
    log.error "Result authorize: ${result}"
    result
  }



}
