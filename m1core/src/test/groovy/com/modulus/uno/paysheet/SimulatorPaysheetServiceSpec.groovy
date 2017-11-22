package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import java.text.*

import com.modulus.uno.Company
import com.modulus.uno.BankAccount
import com.modulus.uno.Bank
import com.modulus.uno.S3Asset
import com.modulus.uno.S3AssetService
import com.modulus.uno.BusinessEntity
import com.modulus.uno.BusinessEntityType
import com.modulus.uno.ComposeName
import com.modulus.uno.NameType
import com.modulus.uno.ModulusUnoAccount

@TestFor(SimulatorPaysheetService)
@Mock([Paysheet, PrePaysheet, Company, PaysheetEmployee, PrePaysheetEmployee, BankAccount, Bank, S3Asset, BusinessEntity, ComposeName, ModulusUnoAccount])
class SimulatorPaysheetServiceSpec extends Specification {

  def grailsAplication
  BreakdownPaymentEmployeeService breakdownPaymentEmployeeService = Mock(BreakdownPaymentEmployeeService)

  def setup() {
    grailsApplication.config.paysheet.quotationDays = 30
    grailsApplication.config.paysheet.uma = 75.9
    service.breakdownPaymentEmployeeService = breakdownPaymentEmployeeService
  }

    void "create BreakdownPaymentEmployee from map"(){
        given:"One list of maps"
            def paysheet = [CONSECUTIVO:1.0, SA_MENSUAL:300.0, SA_NETO:100.0, IAS_NETO:150.0, SA_BRUTO:null, IAS_BRUTO:null, PERIODO:'Mensual', RIESGO_TRAB:1.4, FACT_INTEGRA:1.1, COMISION:3.0]
        when:"create break"
            def breakdownPaymentEmployee = service.breakdownPaymentEmployee(paysheet)
        then:
          println breakdownPaymentEmployee.dump()
          breakdownPaymentEmployee
    }

}