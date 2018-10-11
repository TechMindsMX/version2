package com.modulus.uno.saleorder

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import com.modulus.uno.MovimientosBancarios
import com.modulus.uno.BankAccount
import com.modulus.uno.Bank
import com.modulus.uno.Company
import com.modulus.uno.Address
import com.modulus.uno.AddressType
import com.modulus.uno.CompanyTaxRegime

@TestFor(PaymentComplementService)
@Mock([MovimientosBancarios, BankAccount, Bank, Company, Address])
class PaymentComplementServiceSpec extends Specification {

  void "Should create a payment complement command"(){
    given:"A banking transaction"
      MovimientosBancarios bankingTransaction = createBankingTransaction()
    and:"The payment complement data"
      Map dataPaymentComplement = createDataPaymentComplement()
    when:"We create the payment"
      def result = service.createPaymentComplementCommand(bankingTransaction, dataPaymentComplement)
    then:"We expect the payment complement command"
  }

  private MovimientosBancarios createBankingTransaction() {
    new MovimientosBancarios(
      dateEvent: Date.parse("dd-MM-yyyy", "19-11-2018"),
      amount: new BigDecimal(2500.00),
      cuenta: new BankAccount(
        banco: new Bank(rfc:"BNM840515VB1").save(validate:false),
        clabe: "002115016003269411"
      ).save(validate:false),
    ).save(validate:false)
  }

  private Map createDataPaymentComplement() {
    [
      company: new Company(
        rfc: "AAA010101AAA",
        bussinessName: "THE_EMITTER",
        addresses: [new Address(zipCode:"03400", addressType:AddressType.FISCAL).save(validate:false)],
        taxRegime: CompanyTaxRegime.MORAL,
      ).save(validate:false),
      paymentWay:"03 - TRANSFERENCIA ELECTRONICA"
    ]
  }

}
