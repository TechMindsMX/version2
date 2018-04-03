package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll

import com.modulus.uno.invoice.paysheetReceipt.*
import com.modulus.uno.invoice.*

import com.modulus.uno.Company
import com.modulus.uno.BankAccount
import com.modulus.uno.Bank
import com.modulus.uno.DataImssEmployee
import com.modulus.uno.EmployeeLink
import com.modulus.uno.Address
import com.modulus.uno.AddressType

import com.modulus.uno.DataImssEmployeeService

@TestFor(PaysheetReceiptService)
@Mock([Paysheet, PrePaysheet, Company, PaysheetEmployee, PrePaysheetEmployee, BankAccount, Bank, PaysheetContract, PayerPaysheetProject, PaysheetProject, Address])
class PaysheetReceiptServiceSpec extends Specification {

  PaysheetProjectService paysheetProjectService = Mock(PaysheetProjectService)
  DataImssEmployeeService dataImssEmployeeService = Mock(DataImssEmployeeService)
  PaysheetContract paysheetContract

  def setup() {
    service.paysheetProjectService = paysheetProjectService
    service.dataImssEmployeeService = dataImssEmployeeService
    paysheetContract = createPaysheetContract()
  }

  private PaysheetEmployee createPaysheetEmployee() {
    new PaysheetEmployee (
      paysheet: createPaysheet(),
      prePaysheetEmployee: new PrePaysheetEmployee().save(validate:false),
      salaryImss: new BigDecimal(2500),
      socialQuota: new BigDecimal(63.44),
      subsidySalary: new BigDecimal(162.44),
      incomeTax: new BigDecimal(166.57),
      crudeAssimilable: new BigDecimal(17468.60),
      incomeTaxAssimilable: new BigDecimal(3401.03),
      netAssimilable: new BigDecimal(14067.57),
      socialQuotaEmployer: new BigDecimal(633.62),
      paysheetTax: new BigDecimal(75.00),
      commission: new BigDecimal(516.26),
      ivaRate: new BigDecimal(16)
    ).save(validate:false)
  }

  private PaysheetContract createPaysheetContract() {
    new PaysheetContract (
      employerRegistration:"REGPATRONAL",
      folio: new Integer(0),
      serie: "TESTNOM"
    ).save(validate:false)
  }

  private Paysheet createPaysheet() {
    new Paysheet(
      paysheetContract: paysheetContract,
      prePaysheet: new PrePaysheet(paysheetProject:"TESTPROJECT").save(validate:false)
    ).save(validate:false)
  }

  private PaysheetProject createPaysheetProject() {
    new PaysheetProject(
      name:"TESTPROJECT",
      paysheetContract: paysheetContract,
      payers:createPayersPaysheetProject()
    ).save(validate:false)
  }

  private List<PayerPaysheetProject> createPayersPaysheetProject() {
    Address address = new Address(street:"BOSQUES DE DURAZNOS", streetNumber:"140", zipCode:"11700", colony:"BOSQUES DE LAS LOMAS", country:"MEXICO", city:"MIGUEL HIDALGO", federalEntity:"CIUDAD DE MEXICO", addressType:AddressType.FISCAL).save(validate:false)
    [
      new PayerPaysheetProject(company:new Company(bussinessName:"SA-PAYER", rfc:"AAA010101AAA", addresses:[address]).save(validate:false), paymentSchema:PaymentSchema.IMSS).save(validate:false),
      new PayerPaysheetProject(company:new Company(bussinessName:"IAS-PAYER", rfc:"AAA010101AAA", addresses:[address]).save(validate:false), paymentSchema:PaymentSchema.ASSIMILABLE).save(validate:false)
    ]
  }

  void "Should create the paysheet receipt invoice data from paysheet employee"() {
    given:"The paysheet employee"
      PaysheetEmployee paysheetEmployee = createPaysheetEmployee()
    when:
      def invoiceData = service.createInvoiceDataFromPaysheetEmployee(paysheetEmployee)
    then:
      invoiceData.folio == "1"
      invoiceData.serie == "TESTNOM"
  }

  @Unroll
  void "Should create the paysheet receipt emitter from paysheet employee and schema = #theSchema"() {
    given:"The paysheet employee"
      PaysheetEmployee paysheetEmployee = createPaysheetEmployee()
    and:"The schema"
      PaymentSchema schema = theSchema
    and:
      paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(_, _) >> createPaysheetProject()
    when:
      def emitter = service.createEmitterFromPaysheetEmployee(paysheetEmployee, schema)
    then:
      emitter.registroPatronal == "REGPATRONAL"
      emitter.datosFiscales.razonSocial == theBusinessName
      emitter.datosFiscales.rfc == theRfc
      emitter.datosFiscales.codigoPostal == theZipCode
    where:
      theSchema     || theBusinessName  | theRfc  | theZipCode
      PaymentSchema.IMSS  || "SA-PAYER" | "AAA010101AAA" | "11700"
      PaymentSchema.ASSIMILABLE  || "IAS-PAYER" | "AAA010101AAA" | "11700"
  }
}
