package com.modulus.uno.saleorder

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import com.modulus.uno.MovimientosBancarios
import com.modulus.uno.BankAccount
import com.modulus.uno.Bank
import com.modulus.uno.Company
import com.modulus.uno.Address
import com.modulus.uno.Conciliation
import com.modulus.uno.saleorder.SaleOrder
import com.modulus.uno.saleorder.SaleOrderItem
import com.modulus.uno.saleorder.SaleOrderPayment
import com.modulus.uno.AddressType
import com.modulus.uno.CompanyTaxRegime
import com.modulus.uno.PaymentMethod

@TestFor(PaymentComplementService)
@Mock([MovimientosBancarios, BankAccount, Bank, Company, Address, Conciliation, SaleOrder, SaleOrderItem, SaleOrderPayment])
class PaymentComplementServiceSpec extends Specification {

  void "Should create a payment complement command"(){
    given:"A banking transaction"
      MovimientosBancarios bankingTransaction = createBankingTransaction()
    and:"The payment complement data"
      Map dataPaymentComplement = createDataPaymentComplement()
    and:"The source bank of payment"
      Bank bank = new Bank(rfc:"BBA830831LJ2").save(validate:false)
    when:"We create the payment"
      def result = service.createPaymentComplementCommand(bankingTransaction, dataPaymentComplement)
    then:"We expect the payment complement command"
      result.emitter == "AAA010101AAA"
      result.emisor.datosFiscales.codigoPostal == "03400"
      result.receptor.datosFiscales.rfc == "LAN7008173R5"
      result.payment.amount == "2500.00"
      result.payment.relatedDocuments.size() == 2
  }

  private MovimientosBancarios createBankingTransaction() {
    new MovimientosBancarios(
      dateEvent: Date.parse("dd-MM-yyyy", "19-11-2018"),
      amount: new BigDecimal(2500),
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
      paymentWay:"03",
      bankId:"1",
      sourceAccount:"0101010101",
      conciliations: [
        new Conciliation(
          saleOrder: new SaleOrder(
            folio:"UUID-INVOICE-ONE",
            rfc: "LAN7008173R5",
            clientName: "THE-CLIENT-PAYER",
            invoiceSerie: "A",
            invoiceFolio: "10",
            currency:"MXN",
            paymentMethod: PaymentMethod.PPD,
            items: [new SaleOrderItem(quantity: new BigDecimal(1), price: new BigDecimal(3000), iva: new BigDecimal(16)).save(validate:false)],
            payments: [new SaleOrderPayment(amount: new BigDecimal(1000)).save(validate:false)]
          ),
          amount: new BigDecimal(1000)
        ),
        new Conciliation(
          saleOrder: new SaleOrder(
            folio:"UUID-INVOICE-TWO",
            rfc: "LAN7008173R5",
            clientName: "THE-CLIENT-PAYER",
            invoiceSerie: "B",
            invoiceFolio: "10",
            currency:"MXN",
            paymentMethod: PaymentMethod.PPD,
            items: [new SaleOrderItem(quantity: new BigDecimal(1), price: new BigDecimal(2500), iva: new BigDecimal(16)).save(validate:false)],
            payments: [new SaleOrderPayment(amount: new BigDecimal(1000)).save(validate:false), new SaleOrderPayment(amount: new BigDecimal(1500)).save(validate:false)]
          ),
          amount: new BigDecimal(1500)
        )
      ]
    ]
  }

}
