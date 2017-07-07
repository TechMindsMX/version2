package com.modulus.uno

import pl.touk.excel.export.WebXlsxExporter

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
    def headers = ['RFC','CURP','PATERNO','MATERNO','NOMBRE','NO. EMPL.','CUENTA CLABE','NO. TARJETA',"IMSS (S ó N)","NSS","FECHA ALTA (dd-MM-yyyy)", "SALARIO BASE DE COTIZACION (mensual)", "SALARIO NETO (mensual)", "PRIMA VAC. (%)", "DÍAS AGUINALDO", "PERIODO PAGO"]
    new WebXlsxExporter().with {
      fillRow(headers, 0)
    }
  }

}
