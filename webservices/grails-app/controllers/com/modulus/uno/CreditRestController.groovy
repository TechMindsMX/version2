package com.modulus.uno

import com.modulus.uno.credit.Credit
import com.modulus.uno.credit.CreditService
import com.modulus.uno.credit.CreditCommand
import grails.transaction.Transactional
import com.github.rahulsom.swaggydoc.*
import com.wordnik.swagger.annotations.*
import static org.springframework.http.HttpStatus.*
import com.modulus.uno.Company

@Api
class CreditRestController {

  CreditService creditService

  @ApiOperation(value='Credits list companies', response = Credit, responseContainer = 'list')
  @ApiImplicitParams ([
    @ApiImplicitParam(name = 'corporateId', value = '', dataType = 'number',paramType = 'query')
  ])
  def index() {
    def credits = []
    respond credits, status: 200, formats: ['json']
  }

}
