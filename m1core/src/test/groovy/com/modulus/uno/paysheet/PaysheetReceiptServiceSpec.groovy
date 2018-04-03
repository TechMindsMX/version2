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

import com.modulus.uno.DataImssEmployeeService

@TestFor(PaysheetReceiptService)
@Mock([Paysheet, PrePaysheet, Company, PaysheetEmployee, PrePaysheetEmployee, BankAccount, Bank, PaysheetContract, PayerPaysheetProject, PaysheetProject])
class PaysheetReceiptServiceSpec extends Specification {

  PaysheetProjectService paysheetProjectService = Mock(PaysheetProjectService)
  DataImssEmployeeService dataImssEmployeeService = Mock(DataImssEmployeeService)

  def setup() {
    service.paysheetProjectService = paysheetProjectService
    service.dataImssEmployeeService = dataImssEmployeeService
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
      folio: new Integer(0),
      serie: "TESTNOM"
    )
  }

  private Paysheet createPaysheet() {
    new Paysheet(
      paysheetContract:createPaysheetContract()
    )
  }

  void "Should create the invoice data from paysheetEmployee"() {
    given:"The paysheet employee"
      PaysheetEmployee paysheetEmployee = createPaysheetEmployee()
    when:
      def invoiceData = service.createInvoiceDataFromPaysheetEmployee(paysheetEmployee)
    then:
      invoiceData.folio == "1"
      invoiceData.serie == "TESTNOM"
  }
}
