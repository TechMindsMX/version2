package com.modulus.uno

import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.TestFor
import grails.test.mixin.Mock

@TestFor(DataImssEmployeeService)
@Mock([DataImssEmployee, EmployeeLink])
class DataImssEmployeeServiceSpec extends Specification {

  void "Should save the data imss employee"() {
    given:"A employee"
      EmployeeLink employee = new EmployeeLink().save(validate:false)
    and:"The data imss"
      DataImssEmployee dataImss = new DataImssEmployee(employee:employee, nss:"NSS", registrationDate:new Date(), baseImssMonthlySalary:new BigDecimal(1000), totalMonthlySalary:new BigDecimal(2500), holidayBonusRate:new BigDecimal(25), annualBonusDays:15, paymentPeriod:PaymentPeriod.BIWEEKLY, department:"DEPARTMENT", job:"HELPER")
    when:
      def result = service.saveDataImss(dataImss)
    then:
      result.id
  }

  @Unroll
  void "Should don't save the data imss employee"() {
    when:
      def result = service.saveDataImss(dataImss)
    then:
      result.hasErrors()
    where:
    dataImss  ||  errors
    new DataImssEmployee(employee:new EmployeeLink().save(validate:false), nss:"NSS", registrationDate:new Date(), baseImssMonthlySalary:new BigDecimal(1000), totalMonthlySalary:new BigDecimal(2500), holidayBonusRate:new BigDecimal(25), annualBonusDays:10, paymentPeriod:PaymentPeriod.BIWEEKLY) ||  true
    new DataImssEmployee(employee:new EmployeeLink().save(validate:false), nss:"NSS", registrationDate:new Date(), baseImssMonthlySalary:new BigDecimal(1000), totalMonthlySalary:new BigDecimal(2500), holidayBonusRate:new BigDecimal(120), annualBonusDays:10, paymentPeriod:PaymentPeriod.BIWEEKLY) ||  true

  }

  void "Should create a data imss employee from row employee massive"() {
    given:"The row employee"
      Map rowEmployee = [RFC:"PAGC770214422", CURP:"PAGC770214HOCLTH00", PATERNO:"ApPaterno", MATERNO:"ApMaterno", NOMBRE:"Nombre", NO_EMPL:"EMP-100", CLABE:"036180009876543217", NUMTARJETA:"1234567890123456", IMSS:"S", NSS:"NO-SEGURO", FECHA_ALTA:"01-01-2018", SA_BRUTO:"5000", NETO:"5000", PRIMA_VAC:"25", DIAS_AGUINALDO:"15", PERIODO_PAGO:"SEMANAL", TIPO_CONTRATO:"01", TIPO_REGIMEN:"02", TIPO_JORNADA:"01", DEPARTAMENTO:"ADMINISTRACION", PUESTO:"AUXILIAR"]
    and:"The employee link"
      EmployeeLink employeeLink = new EmployeeLink().save(validate:false)
    when:
      def dataImss = service.createDataImssForRowEmployee(rowEmployee, employeeLink)
    then:
      dataImss.id
  }

  @Unroll
  void "Should calculate labor old = #theLaborOld when registration date is #theStartDate and discharge date is #theEndDate" () {
    given:"The data imss employee"
      DataImssEmployee dataImssEmployee = new DataImssEmployee(registrationDate:theStartDate).save(validate:false)
    when:
      String laborOld = service.calculateLaborOldInSATFormat(dataImssEmployee)
    then:
      laborOld == theLaborOld
    where:
      theStartDate                            |   theEndDate                              ||    theLaborOld
      Date.parse("dd-MM-yyy", "01-01-2018")   |  Date.parse("dd-MM-yyy", "01-02-2018")    ||    "P1M" 
      Date.parse("dd-MM-yyy", "01-02-2018")   |  Date.parse("dd-MM-yyy", "01-03-2018")    ||    "P1M" 
      Date.parse("dd-MM-yyy", "01-04-2018")   |  Date.parse("dd-MM-yyy", "01-05-2018")    ||    "P1M" 
      Date.parse("dd-MM-yyy", "01-01-2018")   |  Date.parse("dd-MM-yyy", "30-01-2018")    ||    "P30D" 
      Date.parse("dd-MM-yyy", "16-02-2018")   |  Date.parse("dd-MM-yyy", "28-02-2018")    ||    "P12D" 
      Date.parse("dd-MM-yyy", "16-02-2017")   |  Date.parse("dd-MM-yyy", "15-02-2018")    ||    "P1Y" 
  }
}
