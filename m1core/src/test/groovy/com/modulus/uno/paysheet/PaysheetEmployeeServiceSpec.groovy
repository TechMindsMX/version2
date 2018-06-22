package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import java.math.RoundingMode

import com.modulus.uno.DataImssEmployee
import com.modulus.uno.DataImssEmployeeService
import com.modulus.uno.PaymentPeriod
import com.modulus.uno.EmployeeLink
import com.modulus.uno.Company
import com.modulus.uno.Bank
import com.modulus.uno.BusinessEntity
import com.modulus.uno.ComposeName
import com.modulus.uno.BusinessEntityType
import com.modulus.uno.NameType

@TestFor(PaysheetEmployeeService)
@Mock([PaysheetEmployee, Paysheet, PrePaysheet, PrePaysheetEmployee, DataImssEmployee, EmployeeLink, Company, BreakdownPaymentEmployee, PaysheetProject, Bank, PaysheetContract, BusinessEntity, ComposeName])
class PaysheetEmployeeServiceSpec extends Specification {

  DataImssEmployeeService dataImssEmployeeService = Mock(DataImssEmployeeService)
  BreakdownPaymentEmployeeService breakdownPaymentEmployeeService = Mock(BreakdownPaymentEmployeeService)
  PaysheetProjectService paysheetProjectService = Mock(PaysheetProjectService)

  def setup() {
    service.dataImssEmployeeService = dataImssEmployeeService
    service.breakdownPaymentEmployeeService = breakdownPaymentEmployeeService
    service.paysheetProjectService = paysheetProjectService
    grailsApplication.config.paysheet.paysheetTax = "3.00"
    grailsApplication.config.paysheet.banks = "40002,40012,40014"
    grailsApplication.config.iva = 16
  }

  void "Should calculate imss salary for employee"() {
    given:"The paysheet employee"
      Company company = new Company().save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(paymentPeriod:PaymentPeriod.WEEKLY, paysheetContract:paysheetContract).save(validate:false)
      Paysheet paysheet = new Paysheet(prePaysheet:prePaysheet, paysheetContract:paysheetContract).save(validate:false)
      PrePaysheetEmployee prePaysheetEmployee = new PrePaysheetEmployee(rfc:"RFC").save(validate:false)
      PaysheetEmployee paysheetEmployee = new PaysheetEmployee(paysheet:paysheet, prePaysheetEmployee:prePaysheetEmployee).save(validate:false)
    and:
      dataImssEmployeeService.getDataImssForEmployee(_) >> new DataImssEmployee(baseImssMonthlySalary:new BigDecimal(4714.12))
    when:
      BigDecimal salaryImss = service.calculateImssSalary(paysheetEmployee)
    then:
      salaryImss == new BigDecimal(1099.96).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate the salary subsidy = #subs for employee with base salary = #bs"() {
    given:"The paysheet employee"
      Company company = new Company().save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(paymentPeriod:PaymentPeriod.WEEKLY, paysheetContract:paysheetContract).save(validate:false)
      Paysheet paysheet = new Paysheet(prePaysheet:prePaysheet, paysheetContract:paysheetContract).save(validate:false)
      PrePaysheetEmployee prePaysheetEmployee = new PrePaysheetEmployee(rfc:"RFC").save(validate:false)
      PaysheetEmployee paysheetEmployee = new PaysheetEmployee(paysheet:paysheet, prePaysheetEmployee:prePaysheetEmployee).save(validate:false)
    and:
      dataImssEmployeeService.getDataImssForEmployee(_) >> new DataImssEmployee(baseImssMonthlySalary:bs)
    when:
      BigDecimal subsidy = service.calculateSubsidySalary(paysheetEmployee)
    then:
      subsidy == subs
    where:
      bs                                                         ||  subs
      new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(0.01).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.97).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(777.06).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.97).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(1768.96).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.97).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(1768.97).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.93).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.93).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(2653.38).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.93).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(2653.39).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.88).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.88).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3472.84).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(94.88).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3472.85).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(91.65).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3500).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(91.65).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3537.87).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(91.65).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(3537.88).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(89.24).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(89.24).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4446.15).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(89.24).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4446.16).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(82.65).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4600).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(82.65).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4717.18).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(82.65).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4717.19).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(75.80).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4777.06).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(75.80).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(5335.42).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(75.80).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(5335.43).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(68.75).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(6000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(68.75).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(6224.67).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(68.75).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(6224.68).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(59.16).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(59.16).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7113.90).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(59.16).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7113.91).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(50.78).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7300).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(50.78).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7382.33).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(50.78).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7382.34).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(10000).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate the income tax = #it for employee with base salary = #bs"() {
    given:"The payment"
      BigDecimal payment = bs
    and:"The payment period"
      PaymentPeriod period = PaymentPeriod.WEEKLY
    when:
      BigDecimal incomeTax = service.calculateIncomeTax(payment, period)
    then:
      incomeTax == it
    where:
      bs                                                         ||  it
      new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4777.06).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(72.07).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4342.94).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(61.05).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7452.56).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(140.64).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(6507.12).setScale(2, RoundingMode.HALF_UP)  || new BigDecimal(115.99).setScale(2, RoundingMode.HALF_UP)
  }

  void "Should create a paysheet employee from a prepaysheet employee"() {
    given:"the paysheet"
      PrePaysheet prePaysheet = new PrePaysheet(paymentPeriod:PaymentPeriod.WEEKLY).save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract(company:new Company().save(validate:false)).save(validate:false)
      Paysheet paysheet = new Paysheet(prePaysheet:prePaysheet, paysheetContract:paysheetContract).save(validate:false)
    and:"the prePaysheet Employee"
      PrePaysheetEmployee prePaysheetEmployee = new PrePaysheetEmployee(prePaysheet:prePaysheet, rfc:"RFC", netPayment:new BigDecimal(1099.96), bank:new Bank(bankingCode:"40002").save(validate:false), account:"201801011").save(validate:false)
      PaysheetEmployee paysheetEmployee = new PaysheetEmployee(paysheet:paysheet, prePaysheetEmployee:prePaysheetEmployee).save(validate:false)
    and:
      BreakdownPaymentEmployee breakdownPaymentEmployee = new BreakdownPaymentEmployee(diseaseAndMaternity:new BigDecimal(0), pension:new BigDecimal(18.72), loan:new BigDecimal(12.48), disabilityAndLife: new BigDecimal(31.21), unemploymentAndEld:new BigDecimal(56.17), fixedFee:new BigDecimal(468.16), diseaseAndMaternityEmployer:new BigDecimal(0), pensionEmployer:new BigDecimal(52.43), loanEmployer:new BigDecimal(34.95), disabilityAndLifeEmployer:new BigDecimal(87.38), kindergarten:new BigDecimal(49.93), occupationalRisk:new BigDecimal(27.14), retirementSaving:new BigDecimal(99.86), unemploymentAndEldEmployer:new BigDecimal(157.28), infonavit:new BigDecimal(249.65), paysheetEmployee:paysheetEmployee).save(validate:false)
      PaysheetProject paysheetProject = new PaysheetProject(commission:new BigDecimal(5)).save(validate:false)
      DataImssEmployee dataImssEmployee = new DataImssEmployee(baseImssMonthlySalary:new BigDecimal(4714.12), totalMonthlySalary:new BigDecimal(4714.12))
    and:
      breakdownPaymentEmployeeService.generateBreakdownPaymentEmployee(_) >> breakdownPaymentEmployee
      dataImssEmployeeService.getDataImssForEmployee(_) >> dataImssEmployee
      paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(_, _) >> paysheetProject
    when:
      PaysheetEmployee result = service.createPaysheetEmployeeFromPrePaysheetEmployee(paysheet, prePaysheetEmployee)
    then:
      result.id
      result.salaryImss == 1099.96
      result.socialQuota == 27.67
      result.socialQuotaEmployer == 286.25
			result.paymentWay == PaymentWay.BANKING
  }

  @Unroll
  void "Should get the rate tax = #expectedRateTax for a monthly salary=#theSalary"() {
    given:"The monthly salary"
      BigDecimal monthlySalary = theSalary
    when:
      RateTax rateTax = service.getRateTaxForMonthlySalary(monthlySalary)
    then:
      rateTax == expectedRateTax
    where:
      theSalary                                                       ||    expectedRateTax
      new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)             ||      null
      new BigDecimal(0.01).setScale(2, RoundingMode.HALF_UP)          ||      RateTax.R1
      new BigDecimal(249.57).setScale(2, RoundingMode.HALF_UP)        ||      RateTax.R1
      new BigDecimal(496.07).setScale(2, RoundingMode.HALF_UP)        ||      RateTax.R1
      new BigDecimal(10298.36).setScale(2, RoundingMode.HALF_UP)      ||      RateTax.R6
      new BigDecimal(15670.89).setScale(2, RoundingMode.HALF_UP)      ||      RateTax.R6
      new BigDecimal(20770.29).setScale(2, RoundingMode.HALF_UP)      ||      RateTax.R6
      new BigDecimal(22100.00).setScale(2, RoundingMode.HALF_UP)      ||      RateTax.R7
      new BigDecimal(75000.00).setScale(2, RoundingMode.HALF_UP)      ||      RateTax.R9
      new BigDecimal(72570890.10).setScale(2, RoundingMode.HALF_UP)   ||      RateTax.R11
  }

  @Unroll
  void "Should calculate crude IAS=#expectedCrudeIAS from net IAS=#theNetIAS"() {
    given:"The net IAS"
      BigDecimal netIAS = theNetIAS
    when:
      BigDecimal crudeIAS = service.calculateCrudeIASFromNetIAS(netIAS)
    then:
      (crudeIAS - expectedCrudeIAS).abs() < 0.5
    where:
      theNetIAS                                                       ||    expectedCrudeIAS
      new BigDecimal(486.74).setScale(2, RoundingMode.HALF_UP)        ||      new BigDecimal(496.27).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(250.00).setScale(2, RoundingMode.HALF_UP)        ||      new BigDecimal(254.89).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(6050.79).setScale(2, RoundingMode.HALF_UP)       ||      new BigDecimal(6552.89).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(10000.00).setScale(2, RoundingMode.HALF_UP)      ||      new BigDecimal(11305.80).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(15850.80).setScale(2, RoundingMode.HALF_UP)      ||      new BigDecimal(18745.78).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(20000.00).setScale(2, RoundingMode.HALF_UP)      ||      new BigDecimal(24113.81).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(27000.00).setScale(2, RoundingMode.HALF_UP)      ||      new BigDecimal(33315.57).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(31000.00).setScale(2, RoundingMode.HALF_UP)      ||      new BigDecimal(39029.85).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(43000.00).setScale(2, RoundingMode.HALF_UP)      ||      new BigDecimal(56172.71).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(59000.00).setScale(2, RoundingMode.HALF_UP)      ||      new BigDecimal(79516.02).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(64710.00).setScale(2, RoundingMode.HALF_UP)      ||      new BigDecimal(88051.87).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(72950.00).setScale(2, RoundingMode.HALF_UP)      ||      new BigDecimal(100536.72).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(85103.00).setScale(2, RoundingMode.HALF_UP)      ||      new BigDecimal(118950.36).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate crude assimilable salary=#expectedCrudeAssimilable for a employee from net assimilable salary=#theNetAssimilable and the paysheet payment period=#thePaymentPeriod"() {
    given:"The paysheet employee"
      PrePaysheet prePaysheet = new PrePaysheet(paymentPeriod:thePaymentPeriod).save(validate:false)
      Paysheet paysheet = new Paysheet(prePaysheet:prePaysheet).save(validate:false)
      PaysheetEmployee paysheetEmployee = new PaysheetEmployee(paysheet:paysheet, netAssimilable:theNetAssimilable).save(validate:false)
    when:
      BigDecimal crudeAssimilable = service.calculateCrudeAssimilableSalary(paysheetEmployee)
    then:
      (crudeAssimilable - expectedCrudeAssimilable).abs() < 0.5
    where:
      theNetAssimilable                                       |     thePaymentPeriod       ||    expectedCrudeAssimilable
      new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)     |   PaymentPeriod.BIWEEKLY   || new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(10000).setScale(2, RoundingMode.HALF_UP) |   PaymentPeriod.BIWEEKLY   || new BigDecimal(12056.91).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(8750.80).setScale(2, RoundingMode.HALF_UP) |   PaymentPeriod.BIWEEKLY   || new BigDecimal(10423.54).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(52500).setScale(2, RoundingMode.HALF_UP) |   PaymentPeriod.BIWEEKLY   || new BigDecimal(74548.66).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(100250.75).setScale(2, RoundingMode.HALF_UP) |   PaymentPeriod.BIWEEKLY   || new BigDecimal(147235.17).setScale(2, RoundingMode.HALF_UP)

   }

  @Unroll
  void "Should set stamped status to employee when employee status = #theCurrentStatus and schema = #theSchema"() {
    given:"The paysheet employee"
      PaysheetEmployee paysheetEmployee = new PaysheetEmployee(status:theCurrentStatus, prePaysheetEmployee:new PrePaysheetEmployee().save(validate:false), salaryImss:theImssSalary, netAssimilable:theNetAssimilable).save(validate:false)
    and:"The schema"
      PaymentSchema schema = theSchema
    when:
      def result = service.setStampedStatusToEmployee(paysheetEmployee, schema)
    then:
      result.status == theExpectedStatus
    where:
      theCurrentStatus              |     theSchema                   |  theImssSalary     |  theNetAssimilable    ||  theExpectedStatus
      PaysheetEmployeeStatus.PAYED   |   PaymentSchema.IMSS           | new BigDecimal(1000)  |  new BigDecimal(2000) ||  PaysheetEmployeeStatus.IMSS_STAMPED_XML
      PaysheetEmployeeStatus.PAYED   |   PaymentSchema.IMSS           | new BigDecimal(1000)  |  new BigDecimal(0) ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.PAYED   |   PaymentSchema.IMSS           | new BigDecimal(0)  |  new BigDecimal(1000) ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.PAYED   |   PaymentSchema.ASSIMILABLE  | new BigDecimal(1000)  |  new BigDecimal(2000)   ||  PaysheetEmployeeStatus.ASSIMILABLE_STAMPED_XML
      PaysheetEmployeeStatus.PAYED   |   PaymentSchema.ASSIMILABLE  | new BigDecimal(1000)  |  new BigDecimal(0)   ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.PAYED   |   PaymentSchema.ASSIMILABLE  | new BigDecimal(0)  |  new BigDecimal(2000)   ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.IMSS_PAYED   |   PaymentSchema.IMSS   | new BigDecimal(1000)  |  new BigDecimal(2000)         ||  PaysheetEmployeeStatus.IMSS_STAMPED_XML
      PaysheetEmployeeStatus.IMSS_PAYED   |   PaymentSchema.IMSS   | new BigDecimal(1000)  |  new BigDecimal(0)         ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.IMSS_PAYED   |   PaymentSchema.IMSS   | new BigDecimal(0)  |  new BigDecimal(2000)         ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.IMSS_PAYED   |   PaymentSchema.ASSIMILABLE   | new BigDecimal(1000)  |  new BigDecimal(2000)  ||  PaysheetEmployeeStatus.ASSIMILABLE_STAMPED_XML
      PaysheetEmployeeStatus.IMSS_PAYED   |   PaymentSchema.ASSIMILABLE   | new BigDecimal(1000)  |  new BigDecimal(0)  ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.IMSS_PAYED   |   PaymentSchema.ASSIMILABLE   | new BigDecimal(0)  |  new BigDecimal(2000)  ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.ASSIMILABLE_PAYED   |   PaymentSchema.IMSS   | new BigDecimal(1000)  |  new BigDecimal(2000)        ||  PaysheetEmployeeStatus.IMSS_STAMPED_XML
      PaysheetEmployeeStatus.ASSIMILABLE_PAYED   |   PaymentSchema.IMSS   | new BigDecimal(1000)  |  new BigDecimal(0)        ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.ASSIMILABLE_PAYED   |   PaymentSchema.IMSS   | new BigDecimal(0)  |  new BigDecimal(2000)        ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.ASSIMILABLE_PAYED   |   PaymentSchema.ASSIMILABLE   | new BigDecimal(1000)  |  new BigDecimal(2000)  ||  PaysheetEmployeeStatus.ASSIMILABLE_STAMPED_XML
      PaysheetEmployeeStatus.ASSIMILABLE_PAYED   |   PaymentSchema.ASSIMILABLE   | new BigDecimal(1000)  |  new BigDecimal(0)  ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.ASSIMILABLE_PAYED   |   PaymentSchema.ASSIMILABLE   | new BigDecimal(0)  |  new BigDecimal(2000)  ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.IMSS_STAMPED_XML   |   PaymentSchema.IMSS         | new BigDecimal(1000)  |  new BigDecimal(2000)   ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.IMSS_STAMPED_XML   |   PaymentSchema.IMSS         | new BigDecimal(1000)  |  new BigDecimal(0)   ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.IMSS_STAMPED_XML   |   PaymentSchema.IMSS         | new BigDecimal(0)  |  new BigDecimal(2000)   ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.IMSS_STAMPED_XML   |   PaymentSchema.ASSIMILABLE  | new BigDecimal(1000)  |  new BigDecimal(2000)   ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.IMSS_STAMPED_XML   |   PaymentSchema.ASSIMILABLE  | new BigDecimal(1000)  |  new BigDecimal(0)   ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.IMSS_STAMPED_XML   |   PaymentSchema.ASSIMILABLE  | new BigDecimal(0)  |  new BigDecimal(2000)   ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.ASSIMILABLE_STAMPED_XML   |   PaymentSchema.IMSS  | new BigDecimal(1000)  |  new BigDecimal(2000)          ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.ASSIMILABLE_STAMPED_XML   |   PaymentSchema.IMSS  | new BigDecimal(1000)  |  new BigDecimal(0)          ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.ASSIMILABLE_STAMPED_XML   |   PaymentSchema.IMSS  | new BigDecimal(0)  |  new BigDecimal(2000)          ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.ASSIMILABLE_STAMPED_XML   |   PaymentSchema.ASSIMILABLE  | new BigDecimal(1000)  |  new BigDecimal(2000)   ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.ASSIMILABLE_STAMPED_XML   |   PaymentSchema.ASSIMILABLE  | new BigDecimal(1000)  |  new BigDecimal(0)   ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.ASSIMILABLE_STAMPED_XML   |   PaymentSchema.ASSIMILABLE  | new BigDecimal(0)  |  new BigDecimal(2000)   ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.FULL_STAMPED_XML   |   PaymentSchema.IMSS         | new BigDecimal(1000)  |  new BigDecimal(2000)   ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
      PaysheetEmployeeStatus.FULL_STAMPED_XML   |   PaymentSchema.ASSIMILABLE  | new BigDecimal(1000)  |  new BigDecimal(2000)   ||  PaysheetEmployeeStatus.FULL_STAMPED_XML
  }

  void "Should reload data for employee"() {
    given:"The paysheet employee"
      Company company = new Company().save(validate:false)
      PaysheetEmployee paysheetEmployee = new PaysheetEmployee (
        paysheet: new Paysheet(paysheetContract:new PaysheetContract(company:company).save(validate:false)).save(validate:false),
        prePaysheetEmployee: new PrePaysheetEmployee(rfc:"rfc", curp:"old-curp", nameEmployee:"old name", numberEmployee:"old number").save(validate:false)
      ).save(validate:false)
    and:"The business entity updated"
      BusinessEntity businessEntity = new BusinessEntity (
        rfc:"rfc",
        type: BusinessEntityType.FISICA,
        names: [
          new ComposeName(value:"new", type:NameType.NOMBRE).save(validate:false),
          new ComposeName(value:"name", type:NameType.APELLIDO_PATERNO).save(validate:false),
          new ComposeName(value:"employee", type:NameType.APELLIDO_MATERNO).save(validate:false)
        ]
      ).save(validate:false)
      company.addToBusinessEntities(businessEntity)
      company.save(validate:false)
    and:"The employee link"
      EmployeeLink employeeLink = new EmployeeLink(
        employeeRef:"rfc",
        curp: "newCurp",
        number: "newNumber",
        company: company
      ).save(validate:false)
    when:
      def result = service.reloadDataEmployee(paysheetEmployee)
    then:
      result.prePaysheetEmployee.curp == "newCurp"
      result.prePaysheetEmployee.nameEmployee == "new name employee"
      result.prePaysheetEmployee.numberEmployee == "newNumber"
  }

  @Unroll
  void "Should set the status #newStatus to employee with current status #currentStatus when generate pdf file for schema #theSchema to paysheet employee"() {
    given:"The employee"
      PaysheetEmployee paysheetEmployee = new PaysheetEmployee(status:currentStatus).save(validate:false)
    when:
      PaysheetEmployee employee = service.setStatusForPdfGeneratedToEmployee(paysheetEmployee, theSchema)
    then:
      employee.status == newStatus
    where:
      theSchema                 |     currentStatus                       ||      newStatus
      PaymentSchema.IMSS        | PaysheetEmployeeStatus.IMSS_STAMPED_XML ||  PaysheetEmployeeStatus.IMSS_STAMPED
      PaymentSchema.IMSS        | PaysheetEmployeeStatus.FULL_STAMPED_XML ||  PaysheetEmployeeStatus.FULL_STAMPED_XML_IMSS_PDF
      PaymentSchema.IMSS        | PaysheetEmployeeStatus.FULL_STAMPED_XML_IMSS_PDF ||  PaysheetEmployeeStatus.FULL_STAMPED
      PaymentSchema.ASSIMILABLE | PaysheetEmployeeStatus.ASSIMILABLE_STAMPED_XML ||  PaysheetEmployeeStatus.ASSIMILABLE_STAMPED
      PaymentSchema.ASSIMILABLE | PaysheetEmployeeStatus.FULL_STAMPED_XML ||  PaysheetEmployeeStatus.FULL_STAMPED_XML_ASSIMILABLE_PDF
      PaymentSchema.ASSIMILABLE | PaysheetEmployeeStatus.FULL_STAMPED_XML_ASSIMILABLE_PDF ||  PaysheetEmployeeStatus.FULL_STAMPED
  }
}
