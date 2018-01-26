package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Ignore
import java.text.*
import java.math.RoundingMode

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
      [CONSECUTIVO:1, SA_BRUTO:1000, IAS_BRUTO:2000, IAS_NETO:0, PERIODO:"QUINCENAL", RIESGO_TRAB:0.879, FACT_INTEGRA:1.0501, COMISION:4] ||  "OK" 
      [CONSECUTIVO:1, SA_BRUTO:1000, IAS_BRUTO:0, IAS_NETO:2000, PERIODO:"QUINCENAL", RIESGO_TRAB:0.879, FACT_INTEGRA:1.0501, COMISION:4] ||  "OK" 
      [CONSECUTIVO:1, SA_BRUTO:0, IAS_BRUTO:1000, IAS_NETO:0, PERIODO:"QUINCENAL", RIESGO_TRAB:0.879, FACT_INTEGRA:1.0501, COMISION:4] ||  "OK" 
      [CONSECUTIVO:1, SA_BRUTO:0, IAS_BRUTO:0, IAS_NETO:2000, PERIODO:"QUINCENAL", RIESGO_TRAB:0.879, FACT_INTEGRA:1.0501, COMISION:4] ||  "OK" 
      [CONSECUTIVO:1, SA_BRUTO:1000, IAS_BRUTO:0, IAS_NETO:0, PERIODO:"QUINCENAL", RIESGO_TRAB:"10%", FACT_INTEGRA:1.0501, COMISION:4] ||  "AL MENOS UNA DE LAS COLUMNAS NO TIENE UN VALOR VÁLIDO" 
      [CONSECUTIVO:1, SA_BRUTO:1000, IAS_BRUTO:100, IAS_NETO:200, PERIODO:"QUINCENAL", RIESGO_TRAB:0.879, FACT_INTEGRA:1.0501, COMISION:4] ||  "LOS DOS CAMPOS DE IAS TIENEN UN NÚMERO MAYOR A CERO, SÓLO UNO DE ELLOS DEBE TENERLO"
      [CONSECUTIVO:1, SA_BRUTO:0, IAS_BRUTO:0, IAS_NETO:0, PERIODO:"QUINCENAL", RIESGO_TRAB:0.879, FACT_INTEGRA:1.0501, COMISION:4] ||  "AL MENOS UNO DE LOS SALARIOS NO DEBE SER CERO"
      [CONSECUTIVO:1, SA_BRUTO:1000, IAS_BRUTO:0, IAS_NETO:0, PERIODO:"DIARIO", RIESGO_TRAB:0.879, FACT_INTEGRA:1.0501, COMISION:4] ||  "EL PERIODO INDICADO NO EXISTE EN EL CATÁLOGO"
  }

  @Unroll
  void "Should calculate de integrated daily salary=#expectedIDS for crude monthly salary=#theCrudeSA and integration factor=#theIF"() {
    given:"The crude SA"
      BigDecimal crudeSA = theCrudeSA
    and:"The integration factor"
      BigDecimal integrationFactor = theIF
    when:
      BigDecimal ids = service.getIntegratedDailySalary(crudeSA, integrationFactor)
    then:
      ids == expectedIDS
    where:
      theCrudeSA                    |       theIF             ||          expectedIDS
      new BigDecimal(1000)          | new BigDecimal(1.0501)  ||  new BigDecimal(35.00).setScale(2, RoundingMode.HALF_UP)
      new BigDecimal(2500)          | new BigDecimal(1.0501)  ||  new BigDecimal(87.51).setScale(2, RoundingMode.HALF_UP)
  }

}
