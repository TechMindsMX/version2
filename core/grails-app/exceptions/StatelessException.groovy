package com.modulus.uno

class StatelessException extends RuntimeException {

  StatelessException(String msg){
    super(msg)
  }

  String getMessage(){
    super.message
  }
}
