package com.modulus.uno

import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.grails.plugins.excelimport.*

class XlsImportService {

  Map COLUMN_MAP_EMPLOYEE = [
    startRow: 1,
    columnMap:  ['A':'RFC', 'B':'CURP', 'C':'PATERNO', 'D':'MATERNO', 'E':'NOMBRE', 'F':'NO_EMPL', 'G':'CLABE', 'H':'NUMTARJETA', 'I':'IMSS', 'J':'NSS', 'K':'FECHA_ALTA', 'L':'BASE_COTIZA', 'M':'NETO', 'N':'PRIMA_VAC', 'O':'DIAS_AGUINALDO', 'P':'PERIODO_PAGO']
  ]

  Map COLUMN_MAP_CLIENT = [
    startRow: 1,
    columnMap:  ['A':'PERSONA', 'B':'RFC', 'C':'SITIO_WEB', 'D':'RAZON_SOCIAL', 'E':'PATERNO', 'F':'MATERNO', 'G':'NOMBRE', 'H':'CLAVE_BANCO', 'I':'ULTIMOS_4_DIGITOS_TARJETA', 'J':'CALLE', 'K':'NUMEXTERIOR', 'L':'NUMINTERIOR', 'M':'CODIGO_POSTAL', 'N':'COLONIA', 'O':'DELEGACION/MUNICIPIO', 'P':'PAIS', 'Q':'CIUDAD', 'R':'ENTIDAD_FEDERATIVA', 'S':'TIPO_DE_DIRECCION']
  ]

  Map COLUMN_MAP_PROVIDER = [
    startRow: 1,
    columnMap:  ['A':'PERSONA', 'B':'RFC', 'C':'SITIO_WEB', 'D':'RAZON_SOCIAL', 'E':'PATERNO', 'F':'MATERNO', 'G':'NOMBRE', 'H':'CLABE', 'I':'CALLE', 'J':'NUMEXTERIOR', 'K':'NUMINTERIOR', 'L':'CODIGO_POSTAL', 'M':'COLONIA', 'N':'DELEGACION/MUNICIPIO', 'O':'PAIS', 'P':'CIUDAD', 'Q':'ENTIDAD_FEDERATIVA', 'R':'TIPO_DE_DIRECCION']
  ]

  def parseXlsMassiveEmployee(File xlsFile) {
    Workbook workbook = getWorkbookFromXlsFile(xlsFile)
    COLUMN_MAP_EMPLOYEE.sheet = workbook.getSheetName(0)
    log.info "Column Map: ${COLUMN_MAP_EMPLOYEE}"
    ExcelImportService excelImportService = new ExcelImportService()
    List data = excelImportService.convertColumnMapConfigManyRows(workbook, COLUMN_MAP_EMPLOYEE)
    log.info "Data: ${data}"
    validateNotEmptyData(data)
    data
  }

  def parseXlsMassiveClient(File xlsFile) {
    Workbook workbook = getWorkbookFromXlsFile(xlsFile)
    COLUMN_MAP_CLIENT.sheet = workbook.getSheetName(0)
    log.info "Column Map: ${COLUMN_MAP_CLIENT}"
    ExcelImportService excelImportService = new ExcelImportService()
    List data = excelImportService.convertColumnMapConfigManyRows(workbook, COLUMN_MAP_CLIENT)
    log.info "Data: ${data}"
    validateNotEmptyData(data)
    data
  }

  def parseXlsMassiveProvider(File xlsFile) {
    Workbook workbook = getWorkbookFromXlsFile(xlsFile)
    COLUMN_MAP_PROVIDER.sheet = workbook.getSheetName(0)
    log.info "Column Map: ${COLUMN_MAP_PROVIDER}"
    ExcelImportService excelImportService = new ExcelImportService()
    List data = excelImportService.convertColumnMapConfigManyRows(workbook, COLUMN_MAP_PROVIDER)
    log.info "Data: ${data}"
    validateNotEmptyData(data)
    data
  }

  Workbook getWorkbookFromXlsFile(File xlsFile) {
    try {
      WorkbookFactory.create(xlsFile)
    } catch (Exception ex) {
      throw new BusinessException("El archivo no es válido")
    }
  }

  void validateNotEmptyData(List data) {
    if (data.empty) {
      throw new BusinessException("El archivo está vacío")
    }
  }

}
