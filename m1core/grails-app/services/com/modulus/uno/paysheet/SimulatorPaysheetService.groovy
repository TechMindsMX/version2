
package com.modulus.uno.paysheet
import pl.touk.excel.export.WebXlsxExporter
import com.modulus.uno.XlsImportService
import java.math.RoundingMode

class SimulatorPaysheetService {

  XlsImportService xlsImportService
  def grailsApplication

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

    def processForSalaryNetoAndIASNeto(row){
      println "SA y IA Netos"
      PaysheetEmployee paysheetEmployee = createPaysheetEmployee() 
      println paysheetEmployee.dump()
    }

    def processForIASNetoAndSalaryBruto(row){
      println "IAS_NETO y SA_BRUTO" 
    }

    def processForSalaryBrutoAndIASBruto(row){
      println "SA_BRUTO y IAS_BRUTO" 
    }

    def processForSalaryNetoAndIASBruto(row){
      println "SA_NETO y IAS_BRUTO"
    }

    def processIASNetoAndIASBruto(row){
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

  PaysheetEmployee createPaysheetEmployee(){
     PaysheetEmployee paysheetEmployee = new PaysheetEmployee(
       breakdownPayment: new BreakdownPaymentEmployee(),
       ivaRate:new BigDecimal(grailsApplication.config.iva).setScale(2, RoundingMode.HALF_UP),
       paymentWay: PaymentWay.BANKING
     )
  }

  BreakdownPaymentEmployee breakdownPaymentEmployee(PaysheetEmployee paysheetEmployee){
    
  }


}