package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import java.math.RoundingMode

import com.modulus.uno.EmployeeLink
import com.modulus.uno.DataImssEmployee
import com.modulus.uno.DataImssEmployeeService
import com.modulus.uno.Company


@TestFor(BreakdownPaymentEmployeeService)
@Mock([Paysheet, PrePaysheetEmployee, PaysheetEmployee, BreakdownPaymentEmployee, EmployeeLink, PaysheetProject, DataImssEmployee, PrePaysheet, Company, PaysheetContract])
class BreakdownPaymentEmployeeServiceSpec extends Specification {

  PaysheetProjectService paysheetProjectService = Mock(PaysheetProjectService)
  DataImssEmployeeService dataImssEmployeeService = Mock(DataImssEmployeeService)

  def setup() {
    grailsApplication.config.paysheet.uma = "75.49"
    grailsApplication.config.paysheet.quotationDays = "30.4"
    grailsApplication.config.paysheet.fixedFee = "20.4"
    grailsApplication.config.paysheet.diseaseAndMaternityEmployer = "1.10"
    grailsApplication.config.paysheet.diseaseAndMaternityEmployee = "0.40"
    grailsApplication.config.paysheet.pensionEmployer = "1.05"
    grailsApplication.config.paysheet.pensionEmployee = "0.375"
    grailsApplication.config.paysheet.loanEmployer = "0.70"
    grailsApplication.config.paysheet.loanEmployee = "0.25"
    grailsApplication.config.paysheet.disabilityAndLifeEmployer = "1.75"
    grailsApplication.config.paysheet.disabilityAndLifeEmployee = "0.625"
    grailsApplication.config.paysheet.kindergarten = "1.00"
    grailsApplication.config.paysheet.retirementSaving = "2.00"
    grailsApplication.config.paysheet.unemploymentAndEldEmployer = "3.15"
    grailsApplication.config.paysheet.unemploymentAndEldEmployee = "1.125"
    grailsApplication.config.paysheet.infonavit = "5.00"
    service.paysheetProjectService = paysheetProjectService
    service.dataImssEmployeeService = dataImssEmployeeService
  }

  void "Should calculate the integrated daily salary for employee"() {
    given:
      PrePaysheet prePaysheet = new PrePaysheet(paysheetProject:"proyecto").save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract().save(validate:false)
      Paysheet paysheet = new Paysheet(paysheetContract:paysheetContract, prePaysheet:prePaysheet).save(validate:false)
    and:
      PaysheetProject paysheetProject = new PaysheetProject(integrationFactor:new BigDecimal(1.0452)).save(validate:false)
      paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(_, _) >> paysheetProject
      DataImssEmployee dataImssEmployee = new DataImssEmployee(baseImssMonthlySalary:new BigDecimal(5000)).save(validate:false)
      dataImssEmployeeService.getDataImssForEmployee(_) >> dataImssEmployee
    when:
      BigDecimal integratedDailySalary = service.getIntegratedDailySalaryForEmployee(dataImssEmployee, paysheet)
    then:
      integratedDailySalary == 174.20
  }

  void "Should calculate disease and maternity base = #dmb when integrated daily salary = #ids"() {
    given:"The integratedDailySalary"
      BigDecimal integratedDailySalary = ids
    when:
      BigDecimal diseaseAndMaternityBase = service.getDiseaseAndMaternityBase(integratedDailySalary)
    then:
      diseaseAndMaternityBase == dmb
    where:
      ids                     ||        dmb
      new BigDecimal(164.24)  ||  new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(256.23)  ||  new BigDecimal(904.70).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(223.73)  ||  new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(282.73)  ||  new BigDecimal(1710.30).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate disease and maternity employer = #dmemployer when disease and maternity base = #dmbase"() {
    given:"The disease and maternity base"
      BigDecimal diseaseAndMaternityBase = dmbase
    when:
      BigDecimal diseaseAndMaternityEmployer = service.getDiseaseAndMaternityEmployer(diseaseAndMaternityBase)
    then:
      diseaseAndMaternityEmployer == dmemployer
    where:
      dmbase                  ||    dmemployer
      new BigDecimal(0).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(904.73).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(9.95).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate disease and maternity employee = #dmemployee when disease and maternity base = #dmbase"() {
    given:"The disease and maternity base"
      BigDecimal diseaseAndMaternityBase = dmbase
    when:
      BigDecimal diseaseAndMaternityEmployee = service.getDiseaseAndMaternityEmployee(diseaseAndMaternityBase)
    then:
      diseaseAndMaternityEmployee == dmemployee
    where:
      dmbase                  ||    dmemployee
      new BigDecimal(0).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(904.73).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(3.62).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate pension employer = #pensionEmployer when base quotation = #baseQuotation"() {
    given:"The base quotation"
      BigDecimal bQ = baseQuotation
    when:
      BigDecimal per = service.getPensionEmployer(bQ)
    then:
      per == pensionEmployer
    where:
      baseQuotation                  ||    pensionEmployer
      new BigDecimal(4992.98).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(52.43).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4539.25).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(47.66).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7789.42).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(81.79).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate pension employee = #pensionEmployee when base quotation = #baseQuotation"() {
    given:"The base quotation"
      BigDecimal bQ = baseQuotation
    when:
      BigDecimal pee = service.getPensionEmployee(bQ)
    then:
      pee == pensionEmployee
    where:
      baseQuotation                  ||    pensionEmployee
      new BigDecimal(4992.98).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(18.72).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4539.25).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(17.02).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7789.42).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(29.21).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate loan employer = #ler when base quotation = #bq"() {
    given:"The base quotation"
      BigDecimal baseQuotation = bq
    when:
      BigDecimal loanEmployer = service.getLoanEmployer(baseQuotation)
    then:
      loanEmployer == ler
    where:
      bq                  ||    ler
      new BigDecimal(4992.98).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(34.95).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4539.25).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(31.77).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7789.42).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(54.53).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate loan employee = #lee when base quotation = #bq"() {
    given:"The base quotation"
      BigDecimal baseQuotation = bq
    when:
      BigDecimal loanEmployee = service.getLoanEmployee(baseQuotation)
    then:
      loanEmployee == lee
    where:
      bq                  ||    lee
      new BigDecimal(4992.98).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(12.48).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4539.25).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(11.35).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7789.42).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(19.47).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate disability and life for employer = #dler when integrated daily salary = #ids"() {
    given:"The integrated daily salary"
      BigDecimal integratedDailySalary = ids
    when:
      BigDecimal disabilityAndLifeEmployer = service.getDisabilityAndLifeEmployer(integratedDailySalary)
    then:
      disabilityAndLifeEmployer == dler
    where:
      ids                   ||    dler
      new BigDecimal(164.24).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(87.38).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(149.32).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(79.44).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(256.23).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(136.31).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(1004.02).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate disability and life for employee = #dlee when integrated daily salary = #ids"() {
    given:"The integrated daily salary"
      BigDecimal integratedDailySalary = ids
    when:
      BigDecimal disabilityAndLifeEmployee = service.getDisabilityAndLifeEmployee(integratedDailySalary)
    then:
      disabilityAndLifeEmployee == dlee
    where:
      ids                   ||    dlee
      new BigDecimal(164.24).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(31.21).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(149.32).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(28.37).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(256.23).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(48.68).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(358.58).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate kindergarten = #kg when base quotation = #bq"() {
    given:"The base quotation"
      BigDecimal baseQuotation = bq
    when:
      BigDecimal kindergarten = service.getKindergarten(baseQuotation)
    then:
      kindergarten == kg
    where:
      bq                   ||    kg
      new BigDecimal(4992.98).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(49.93).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4539.25).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(45.39).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7789.42).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(77.89).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate occupational risk = #or when base quotation = #bq"() {
    given:"The base quotation"
      BigDecimal baseQuotation = bq
    and:
      PrePaysheet prePaysheet = new PrePaysheet(paysheetProject:"proyecto").save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract().save(validate:false)
      Paysheet paysheet = new Paysheet(paysheetContract:paysheetContract, prePaysheet:prePaysheet).save(validate:false)
    and:
      PaysheetProject paysheetProject = new PaysheetProject(occupationalRiskRate:new BigDecimal(0.54355)).save(validate:false)
      paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(_, _) >> paysheetProject
    when:
      BigDecimal occupationalRisk = service.getOccupationalRisk(baseQuotation, paysheet)
    then:
      occupationalRisk == or
    where:
      bq              ||      or
      new BigDecimal(4992.98).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(27.14).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4539.25).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(24.67).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7789.42).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(42.34).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate retirement saving = #rs when base quotation = #bq"() {
    given:"The base quotation"
      BigDecimal baseQuotation = bq
    when:
      BigDecimal retirementSaving = service.getRetirementSaving(baseQuotation)
    then:
      retirementSaving == rs
    where:
      bq                   ||    rs
      new BigDecimal(4992.98).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(99.86).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4539.25).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(90.79).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7789.42).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(155.79).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate unemployment and eld for employer = #ueer when base quotation = #bq"() {
    given:"The base quotation"
      BigDecimal baseQuotation = bq
    when:
      BigDecimal unemploymentAndEldEmployer = service.getUnemploymentAndEldEmployer(baseQuotation)
    then:
      unemploymentAndEldEmployer == ueer
    where:
      bq                   ||    ueer
      new BigDecimal(4992.98).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(157.28).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4539.25).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(142.99).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7789.42).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(245.37).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate unemployment and eld for employee = #ueee when base quotation = #bq"() {
    given:"The base quotation"
      BigDecimal baseQuotation = bq
    when:
      BigDecimal unemploymentAndEldEmployee = service.getUnemploymentAndEldEmployee(baseQuotation)
    then:
      unemploymentAndEldEmployee == ueee
    where:
      bq                   ||    ueee
      new BigDecimal(4992.98).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(56.17).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4539.25).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(51.07).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7789.42).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(87.63).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should calculate infonavit = #inf when base quotation = #bq"() {
    given:"The base quotation"
      BigDecimal baseQuotation = bq
    when:
      BigDecimal infonavit = service.getInfonavit(baseQuotation)
    then:
      infonavit == inf
    where:
      bq                   ||    inf
      new BigDecimal(4992.98).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(249.65).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(4539.25).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(226.96).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(7789.42).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(389.47).setScale(2, RoundingMode.HALF_UP)
  }

  @Unroll
  void "Should get social quota for employee = #sqee and social quota employer = #sqer when base imss monthly salary = #bimss"() {
    given:"The employee"
      Company company = new Company().save(validate:false)
      PrePaysheetEmployee prePaysheetEmployee = new PrePaysheetEmployee(rfc:"RFC").save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(paysheetProject:"proyecto").save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
      Paysheet paysheet = new Paysheet(paysheetContract:paysheetContract, prePaysheet:prePaysheet).save(validate:false)
      PaysheetEmployee paysheetEmployee = new PaysheetEmployee(prePaysheetEmployee:prePaysheetEmployee, paysheet:paysheet).save(validate:false)
      EmployeeLink employee = new EmployeeLink(employeeRef:"RFC", company:company).save(validate:false)
    and:
      PaysheetProject paysheetProject = new PaysheetProject(integrationFactor:new BigDecimal(1.0452), occupationalRiskRate:new BigDecimal(0.54355)).save(validate:false)
      paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(_, _) >> paysheetProject
      DataImssEmployee dataImssEmployee = new DataImssEmployee(baseImssMonthlySalary:bimss).save(validate:false)
      dataImssEmployeeService.getDataImssForEmployee(_) >> dataImssEmployee
    when:
      BreakdownPaymentEmployee breakdownPaymentEmployee = service.generateBreakdownPaymentEmployee(paysheetEmployee)
    then:
      breakdownPaymentEmployee.socialQuotaEmployeeTotal == sqee
      breakdownPaymentEmployee.socialQuotaEmployer == sqer
    where:
    bimss                                   ||      sqee    ||    sqer
    new BigDecimal(4714.12).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(118.58).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(1226.77).setScale(2, RoundingMode.HALF_UP)
    new BigDecimal(0).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(0).setScale(2, RoundingMode.HALF_UP) || new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
  }

}
