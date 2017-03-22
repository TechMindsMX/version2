package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import java.lang.Void as Should

 @TestFor(TransactionService)
 @Mock([Transaction])
 class TransactionServiceSpec extends Specification {

  Should "save new transition"(){
    given:"the transaction"
    Map parameters = [keyTransaction:"123123",trackingKey:"123123",amount:12.12,paymentConcept:"Prestamo",keyAccount:"646180132408900006",referenceNumber:"201703100010"]
      Transaction transaction = new Transaction(parameters)
    when:
      transaction = service.saveTransaction(transaction)
    then:
      assert transaction.id
  }

}
