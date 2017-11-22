
package com.modulus.uno.paysheet
import pl.touk.excel.export.WebXlsxExporter
import com.modulus.uno.XlsImportService

class SimulatorPaysheetService {

  XlsImportService xlsImportService

    def generateLayoutForSimulator() {
       def headers = ['RFC','CURP','NOMBRE','NUM_EMPL','CÓD. BANCO','BANCO','CLABE','CUENTA','NUM.TARJETA',"IMSS","NSS","FECHA_ALTA", "BASE_COTIZA", "NETO", "PRIMA_VAC", "DIAS_AGUINALDO", "PERIODO_PAGO", "SALARIO_NETO", "SALARIO_BRUTO"]
       def descriptions = ['Reemplazar esta fila', '', '', '', '', 'No. de Empleado', '18 dígitos', '16 dígitos', "S ó N","", "dd-MM-yyyy", "Salario Base de Cotización mensual", "Salario Neto mensual", "En porcentaje", "", "Semanal, Catorcenal, Quincenal, Mensual"]
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
            println row.SALARY
        }
        Map dataHeaders = xlsImportService.parseXlsPaysheetSimulatorHeaders(file)
        println dataHeaders.dump()
        data
    }

    def salaryProcess(row){

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

}