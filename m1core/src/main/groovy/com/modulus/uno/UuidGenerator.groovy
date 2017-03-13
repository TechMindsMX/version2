package com.modulus.uno

class UuidGenerator {
  static def generateUuid() {
    UUID.randomUUID().toString().replaceAll('-', '');
  }
}
