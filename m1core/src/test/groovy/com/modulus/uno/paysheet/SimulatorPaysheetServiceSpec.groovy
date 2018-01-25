package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Ignore
import java.text.*

@TestFor(SimulatorPaysheetService)
@Mock([PaysheetEmployee, PrePaysheetEmployee])
class SimulatorPaysheetServiceSpec extends Specification {

  def grailsAplication
  BreakdownPaymentEmployeeService breakdownPaymentEmployeeService = Mock(BreakdownPaymentEmployeeService)
  PaysheetEmployeeService paysheetEmployeeService = Mock(PaysheetEmployeeService)

  def setup() {
    service.breakdownPaymentEmployeeService = breakdownPaymentEmployeeService
    service.paysheetEmployeeService = paysheetEmployeeService
    grailsApplication.config.iva = 16
    grailsApplication.config.paysheet.uma = "75.49"
    grailsApplication.config.paysheet.quotationDays = "30.4"
    grailsApplication.config.paysheet.fixedFee = "20.40"
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
    grailsApplication.config.paysheet.paysheetTax = "3.00"
    grailsApplication.config.paysheet.paymentBankingCode = "012"
  }

  @Ignore
  void "create BreakdownPaymentEmployee from map"(){
    given:"One list of maps"
      def paysheet = [CONSECUTIVO:1.0, IAS_NETO:150.0, SA_BRUTO:400, IAS_BRUTO:null, PERIODO:'Mensual', RIESGO_TRAB:1.4, FACT_INTEGRA:1.1, COMISION:3.0]
    when:"create break"
      def breakdownPaymentEmployee = service.breakdownPaymentEmployee(paysheet)
    then:
      breakdownPaymentEmployee
  }

  @Ignore
  void "create paymentSheetEmployee"(){
    given:"give one paysheet map"
      def paysheet = [CONSECUTIVO:1.0, IAS_NETO:null, SA_BRUTO:6000, IAS_BRUTO:18000, PERIODO:'Quincenal', RIESGO_TRAB:1.3, FACT_INTEGRA:1.5, COMISION:10.0]
    when:"Was create one paysheetEmployee"
      PaysheetEmployee paymentSheetEmployee = service.createPaysheetEmployee(paysheet)
    then:
       paymentSheetEmployee.salaryImss == 3000.00
       paymentSheetEmployee.ivaRate== 16.00
       paymentSheetEmployee.breakdownPayment
       paymentSheetEmployee.breakdownPayment.baseQuotation == 0
       paymentSheetEmployee.breakdownPayment.integratedDailySalary == 300
       paymentSheetEmployee.socialQuota == 0
       paymentSheetEmployee.subsidySalary == 0
       paymentSheetEmployee.incomeTax == 0
       paymentSheetEmployee.netAssimilable == 0
       paymentSheetEmployee.socialQuotaEmployer == 0
       paymentSheetEmployee.paysheetTax == 0
       paymentSheetEmployee.commission == 0
  }

  @Unroll
  void "Should get the result validation=#expectedResult when row to import is #theRow"() {
    given:"The row to import"
      Map row = theRow
    when:
      String result = service.validateRowToImport(row)
    then:
      result == expectedResult
    where:
      theRow                                              ||            expectedResult
      [:]                                                 ||  "TODAS LAS COLUMNAS SON REQUERIDAS, FALTAN DATOS" 
      [CONSECUTIVO:1, SA_BRUTO:1000, IAS_BRUTO:0, IAS_NETO:0] ||  "TODAS LAS COLUMNAS SON REQUERIDAS, FALTAN DATOS" 
      [CONSECUTIVO:1, SA_BRUTO:1000, IAS_BRUTO:0, IAS_NETO:0, PERIODO:"QUINCENAL", RIESGO_TRAB:0.879, FACT_INTEGRA:1.0501, COMISION:4] ||  "OK" 
      [CONSECUTIVO:1, SA_BRUTO:1000, IAS_BRUTO:0, IAS_NETO:0, PERIODO:"QUINCENAL", RIESGO_TRAB:"10%", FACT_INTEGRA:1.0501, COMISION:4] ||  "AL MENOS UNA DE LAS COLUMNAS NO TIENE UN VALOR VÁLIDO" 
      [CONSECUTIVO:1, SA_BRUTO:1000, IAS_BRUTO:100, IAS_NETO:200, PERIODO:"QUINCENAL", RIESGO_TRAB:0.879, FACT_INTEGRA:1.0501, COMISION:4] ||  "LOS DOS CAMPOS DE IAS TIENEN UN NÚMERO MAYOR A CERO, SÓLO UNO DE ELLOS DEBE TENERLO"
      [CONSECUTIVO:1, SA_BRUTO:0, IAS_BRUTO:0, IAS_NETO:0, PERIODO:"QUINCENAL", RIESGO_TRAB:0.879, FACT_INTEGRA:1.0501, COMISION:4] ||  "AL MENOS UNO DE LOS SALARIOS NO DEBE SER CERO"
      [CONSECUTIVO:1, SA_BRUTO:1000, IAS_BRUTO:0, IAS_NETO:0, PERIODO:"DIARIO", RIESGO_TRAB:0.879, FACT_INTEGRA:1.0501, COMISION:4] ||  "EL PERIODO INDICADO NO EXISTE EN EL CATÁLOGO"
  }
}
