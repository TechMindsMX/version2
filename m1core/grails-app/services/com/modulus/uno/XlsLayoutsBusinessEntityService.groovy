package com.modulus.uno

import pl.touk.excel.export.WebXlsxExporter
import com.modulus.uno.BusinessEntity

class XlsLayoutsBusinessEntityService {

  def generateLayoutForCLIENTE() {
    new WebXlsxExporter().with {
      fillRow(["Layout para alta masiva de tipo CLIENTE"], 0)
    }
  }

  def generateLayoutForCLIENTE_PROVEEDOR() {
    new WebXlsxExporter().with {
      fillRow(["Layout para alta masiva de tipo CLIENTE_PROVEEDOR"], 0)
    }
  }

  def generateLayoutForPROVEEDOR() {
    new WebXlsxExporter().with {
      fillRow(["Layout para alta masiva de tipo PROVEEDOR"], 0)
    }
  }

  def generateLayoutForEMPLEADO() {
    def headers = ['RFC','CURP','PATERNO','MATERNO','NOMBRE','NUM_EMPL','BANCO','CLABE','CUENTA','SUCURSAL','NUMTARJETA',"IMSS","NSS","FECHA_ALTA", "BASE_COTIZA", "NETO", "PRIMA_VAC", "DIAS_AGUINALDO", "PERIODO_PAGO"]
    def descriptions = ['Reemplazar esta fila', '', '', '', '', 'No. de Empleado', 'Código del Banco', '18 dígitos','11 dígitos máximo','3 dígitos', '16 dígitos', "S ó N","", "dd-MM-yyyy", "Salario Base de Cotización mensual", "Salario Neto mensual", "En porcentaje", "", "Semanal, Catorcenal, Quincenal, Mensual"]
    new WebXlsxExporter().with {
      fillRow(headers, 0)
      fillRow(descriptions, 1)
    }
  }

  def exportListOfBusinessEntities(List<BusinessEntity> businessEntityList){
    def headers = ['RFC', 'NOMBRE/RAZON_SOCIAL', 'SITIO_WEB', 'PERSONA', 'TIPO_DE_RELACIÓN', 'ESTATUS']
    def data = ['${businessEntity.rfc}', 'businessEntity.name']
    new WebXlsxExporter().with{
      fillRow(headers, 0)
      fillRow(data, 1)
    }
  }


}
