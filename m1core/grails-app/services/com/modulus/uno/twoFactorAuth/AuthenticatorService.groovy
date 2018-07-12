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
    GoogleAuthenticator gAuth = new GoogleAuthenticator()
    gAuth.authorize(key, token)
  }



}
