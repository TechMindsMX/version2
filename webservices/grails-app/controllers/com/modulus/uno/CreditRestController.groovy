package com.modulus.uno

import com.modulus.uno.credit.Credit
import com.modulus.uno.credit.CreditCommand

@Api
class CreditRestController {

  def creditService
  
  def index() {
    respond [hola: "Hola mundo"], status: 200, formats: ['json']
  }

}
