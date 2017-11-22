
package com.modulus.uno.paysheet
import pl.touk.excel.export.WebXlsxExporter
import com.modulus.uno.XlsImportService
import java.math.RoundingMode

class SimulatorPaysheetService {

  XlsImportService xlsImportService
  def grailsApplication
  BreakdownPaymentEmployeeService breakdownPaymentEmployeeService

    def generateLayoutForSimulator() {
       def headers = ['CONSECUTIVO','SA_MENSUAL','SA_NETO','IAS_NETO','SA_BRUTO','IAS_BRUTO','PERIODO','RIESGO_TRAB',"FACT_INTEGRA","COMISION"]
       def descriptions = ['NUMERO','','','','','',"Semanal, Catorcenal, Quincenal, Mensual"]
       new WebXlsxExporter().with {
          fillRow(headers, 2)
          fillRow(descriptions, 3)
        }
    }

    def processXlsSimulator(file) {
        log.info "Processing massive registration for Employee"
        List data = xlsImportService.parseXlsPaysheetSimulator(file)
        println data.class
        data.each{ row ->
            println row
            if(row.SA_NETO && row.IAS_NETO){ processForSalaryNetoAndIASNeto(row)}
            if(row.IAS_NETO && row.SA_BRUTO){ processForIASNetoAndSalaryBruto(row) }
            if(row.SA_BRUTO && row.IAS_BRUTO){ processForSalaryBrutoAndIASBruto(row) }
            if(row.SA_NETO && row.IAS_BRUTO){ processForSalaryNetoAndIASBruto(row)}
            if(row.IAS_NETO && row.IAS_BRUTO){processIASNetoAndIASBruto(row) }
        }
        data
    }

    def processForSalaryNetoAndIASNeto(def row){
      println "SA y IA Netos"
      PaysheetEmployee paysheetEmployee = createPaysheetEmployee() 
      println "********"
      println row.SA_MENSUAL.class
      println paysheetEmployee.dump()
    }

    def processForIASNetoAndSalaryBruto(def row){
      println "IAS_NETO y SA_BRUTO" 
    }

    def processForSalaryBrutoAndIASBruto(def row){
      println "SA_BRUTO y IAS_BRUTO" 
    }

    def processForSalaryNetoAndIASBruto(def row){
      println "SA_NETO y IAS_BRUTO"
    }

    def processIASNetoAndIASBruto(def row){
      println "IAS_NETO y IAS_BRUTO"
    }

    
  List processDataFromXls(List data) {
    List results = []
    data.each { employee ->
      String result = saveEmployeeImportData(employee, company)
      results.add(result)
      if (result == "Registrado") {
        addEmployeeToCompany(employee.RFC, company)
      }
    }
    results
  }

  PaysheetEmployee createPaysheetEmployee(def row){
     PaysheetEmployee paysheetEmployee = new PaysheetEmployee(
       breakdownPayment: new BreakdownPaymentEmployee(),
       ivaRate:new BigDecimal(grailsApplication.config.iva).setScale(2, RoundingMode.HALF_UP),
       paymentWay: PaymentWay.BANKING
     )
  }

  BreakdownPaymentEmployee breakdownPaymentEmployee(def row){
    BigDecimal integratedDailySalary =  getIntegratedDailySalary(row.SA_MENSUAL, row.FACT_INTEGRA)
    println integratedDailySalary 
    BigDecimal baseQuotation = getBaseQuotation(integratedDailySalary)
    BigDecimal diseaseAndMaternityBase = getDiseaseAndMaternityBase(integratedDailySalary)
    BreakdownPaymentEmployee breakdownPaymentEmployee = new BreakdownPaymentEmployee(
      integratedDailySalary: integratedDailySalary,
      baseQuotation: baseQuotation,
      fixedFee: breakdownPaymentEmployeeService.getFixedFee(),
      diseaseAndMaternityBase: diseaseAndMaternityBase,
      diseaseAndMaternityEmployer: breakdownPaymentEmployeeService.getDiseaseAndMaternityEmployer(diseaseAndMaternityBase),
      diseaseAndMaternity: breakdownPaymentEmployeeService.getDiseaseAndMaternityEmployee(diseaseAndMaternityBase),
      pension: breakdownPaymentEmployeeService.getPensionEmployee(baseQuotation),
      pensionEmployer: breakdownPaymentEmployeeService.getPensionEmployer(baseQuotation),
      loan: breakdownPaymentEmployeeService.getLoanEmployee(baseQuotation),
      loanEmployer: breakdownPaymentEmployeeService.getLoanEmployer(baseQuotation),
      disabilityAndLife: breakdownPaymentEmployeeService.getDisabilityAndLifeEmployee(integratedDailySalary),
      disabilityAndLifeEmployer: breakdownPaymentEmployeeService.getDisabilityAndLifeEmployer(integratedDailySalary),
      kindergarten: breakdownPaymentEmployeeService.getKindergarten(baseQuotation),
      occupationalRisk: getOccupationalRisk(baseQuotation, row.RIESGO_TRAB),
      retirementSaving: breakdownPaymentEmployeeService.getRetirementSaving(baseQuotation),
      unemploymentAndEld: breakdownPaymentEmployeeService.getUnemploymentAndEldEmployee(baseQuotation),
      unemploymentAndEldEmployer: breakdownPaymentEmployeeService.getUnemploymentAndEldEmployer(baseQuotation),
      infonavit: breakdownPaymentEmployeeService.getInfonavit(baseQuotation)
    )
  }

  BigDecimal getIntegratedDailySalary(BigDecimal SA_MENSUAL, BigDecimal FACT_INTEGRA){
    ((new BigDecimal(SA_MENSUAL)) / 30 * (new BigDecimal(FACT_INTEGRA))).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getBaseQuotation(BigDecimal integratedDailySalary){
    integratedDailySalary * new BigDecimal(grailsApplication.config.paysheet.quotationDays)
  } 
  
  BigDecimal getDiseaseAndMaternityBase(BigDecimal integratedDailySalary) {
    BigDecimal limit = 3 * new BigDecimal(grailsApplication.config.paysheet.uma)
    BigDecimal diseaseAndMaternityBase = new BigDecimal(0)
    if (integratedDailySalary > limit) {
      diseaseAndMaternityBase = (integratedDailySalary - limit) * new BigDecimal(grailsApplication.config.paysheet.quotationDays)
    }
    diseaseAndMaternityBase.setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal getOccupationalRisk(BigDecimal baseQuotation, BigDecimal riskJob) {
    (baseQuotation * (riskJob/100)).setScale(2, RoundingMode.HALF_UP)
  }



}