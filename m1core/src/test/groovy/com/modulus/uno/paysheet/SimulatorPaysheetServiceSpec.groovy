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
    service.breakdownPaymentEmployeeService = breakdownPaymentEmployeeService
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

    void "create BreakdownPaymentEmployee from map"(){
        given:"One list of maps"
            def paysheet = [CONSECUTIVO:1.0, SA_MENSUAL:300.0, SA_NETO:100.0, IAS_NETO:150.0, SA_BRUTO:null, IAS_BRUTO:null, PERIODO:'Mensua', RIESGO_TRAB:1.4, FACT_INTEGRA:1.1, COMISION:3.0]
        when:"create break"
            def breakdownPaymentEmployee = service.breakdownPaymentEmployee(paysheet)
        then:
          breakdownPaymentEmployee
    }

    void "create paymentSheetEmployee"(){
        given:"give one paysheet map"
            def paysheet = [CONSECUTIVO:1.0, SA_MENSUAL:300.0, SA_NETO:100.0, IAS_NETO:150.0, SA_BRUTO:null, IAS_BRUTO:null, PERIODO:'Semanal', RIESGO_TRAB:1.4, FACT_INTEGRA:1.1, COMISION:3.0]
        and:
            PaysheetEmployee paymentSheetEmployee = service.createPaysheetEmployee(paysheet)
        when:
            //paymentSheetEmployee.breakdownPaymentEmployee = service.breakdownPaymentEmployee(paysheet)
            println paymentSheetEmployee.breakdownPayment
            println paymentSheetEmployee.breakdownPayment.baseQuotation
            println paymentSheetEmployee.socialQuota
            println paymentSheetEmployee.subsidySalary
            println paymentSheetEmployee.incomeTax
            println paymentSheetEmployee.salaryAssimilable
            println paymentSheetEmployee.socialQuotaEmployer

        then:
            1==2

    }

}