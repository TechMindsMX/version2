package com.modulus.uno.stp

import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.TestFor
import grails.test.mixin.Mock

import com.modulus.uno.Company

@TestFor(FinalTransactionResultService)
@Mock([FinalTransactionResult, Company])
class FinalTransactionResultServiceSpec extends Specification {

  @Unroll
  void "Should save the final transaction result with execution mode #mode"() {
    given:"The data result"
      Map dataResult = [company:new Company().save(validate:false), dateTransaction:theDate, comment:"The comment", status:FinalTransactionResultStatus.SUCCESSFUL]
    when:
      def result = service.createFinalTransactionResult(dataResult)
    then:
      result
      result.executionMode == mode
    where:
      theDate      ||  mode
      new Date()   ||  ExecutionMode.AUTOMATIC
      new Date()-1 ||  ExecutionMode.MANUAL
      new Date()-5 ||  ExecutionMode.MANUAL
  }

}
