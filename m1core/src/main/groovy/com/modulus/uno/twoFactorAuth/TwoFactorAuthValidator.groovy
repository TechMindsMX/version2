package com.modulus.uno.twoFactorAuth

import groovy.transform.CompileStatic

@CompileStatic
interface TwoFactorAuthValidator {

  boolean isValidCodeAndUserName(String code, String name)

}
