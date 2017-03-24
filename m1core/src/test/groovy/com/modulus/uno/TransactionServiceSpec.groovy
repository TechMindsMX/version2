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
    Map parameters = [keyTransaction:"123123",trackingKey:"123123",amount:12.12,
    paymentConcept:"Prestamo",keyAccount:"646180132408900006",referenceNumber:"201703100010",
    transactionType:TransactionType.WITHDRAW,transactionStatus:TransactionStatus.AUTHORIZED]
      Transaction transaction = new Transaction(parameters)
    when:
      transaction = service.saveTransaction(transaction)
    then:
      assert transaction.id
  }

  Should "get the transactions in period for account"(){
    given:
      (123123..123124).each{
        new Transaction([keyTransaction:it,trackingKey:"123123",amount:12.12,
                        paymentConcept:"Prestamo",keyAccount:"646180132408900006",referenceNumber:"201703100010",
                        transactionType:TransactionType.WITHDRAW,transactionStatus:TransactionStatus.AUTHORIZED]).save(flush:true)
      }
      (123127..123128).each{
        new Transaction([keyTransaction:it,trackingKey:"123123",amount:12.12,
                        paymentConcept:"Prestamo",keyAccount:"646180132408900007",referenceNumber:"201703100010",
                        transactionType:TransactionType.WITHDRAW,transactionStatus:TransactionStatus.AUTHORIZED]).save(flush:true)
      }
    when:
      def transactions = service.getTransactionsAccountForPeriod("646180132408900006",(new Date()-10),(new Date()+5))
    then:
      transactions.size() == 2
  }

}
