package com.modulus.uno

import com.modulus.uno.credit.Credit
import com.modulus.uno.credit.CreditCommand
import grails.transaction.Transactional
import com.github.rahulsom.swaggydoc.*
import com.wordnik.swagger.annotations.*
import static org.springframework.http.HttpStatus.*

@Api
class CreditRestController {

  def creditService
  
  def index() {
    respond status: 201, formats: ['json']
  }

}
