
package com.modulus.uno.paysheet
import pl.touk.excel.export.WebXlsxExporter

class SimulatorPaysheetService {

    void testService(){
        
        log.info "Test..."*100
    }

    def generateLayoutForSimulator() {
       def headers = ['RFC','CURP','PATERNO','MATERNO','NOMBRE','NUM_EMPL','CLABE','NUMTARJETA',"IMSS","NSS","FECHA_ALTA", "BASE_COTIZA", "NETO", "PRIMA_VAC", "DIAS_AGUINALDO", "PERIODO_PAGO"]
       def descriptions = ['Reemplazar esta fila', '', '', '', '', 'No. de Empleado', '18 dígitos', '16 dígitos', "S ó N","", "dd-MM-yyyy", "Salario Base de Cotización mensual", "Salario Neto mensual", "En porcentaje", "", "Semanal, Catorcenal, Quincenal, Mensual"]
       new WebXlsxExporter().with {
          fillRow(headers, 0)
          fillRow(descriptions, 1)
        }
    }

}