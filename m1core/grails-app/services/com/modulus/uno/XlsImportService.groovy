package com.modulus.uno

import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.grails.plugins.excelimport.*

class XlsImportService {

  Map COLUMN_MAP_EMPLOYEE = [
    startRow: 1,
    columnMap:  ['A':'RFC', 'B':'CURP', 'C':'PATERNO', 'D':'MATERNO', 'E':'NOMBRE', 'F':'NO_EMPL','G':'BANCO', 'H':'CLABE','I':'CUENTA','J':'SUCURSAL', 'K':'NUMTARJETA', 'L':'IMSS', 'M':'NSS', 'N':'FECHA_ALTA', 'O':'BASE_COTIZA', 'P':'NETO', 'Q':'PRIMA_VAC', 'R':'DIAS_AGUINALDO', 'S':'PERIODO_PAGO']
  ]
		

	Map COLUMN_MAP_PREPAYSHEET = [
		startRow:1,
		columnMap: ['A':'RFC', 'B':'CURP','C':'NO_EMPL','D':'NOMBRE','E':'CLABE','F':'TARJETA','G':'NETO','H':'OBSERVACIONES']
	]

  File getFileToProcess(def file) {
    File xlsFile = File.createTempFile("tmpXlsImport${new Date().getTime()}",".xlsx")
    FileOutputStream fos = new FileOutputStream(xlsFile)
    fos.write(file.getBytes())
    fos.close()
    xlsFile
  }

  def parseXlsMassiveEmployee(def file) {
		File xlsFile = getFileToProcess(file)
    Workbook workbook = getWorkbookFromXlsFile(xlsFile)
    COLUMN_MAP_EMPLOYEE.sheet = workbook.getSheetName(0)
    log.info "Column Map: ${COLUMN_MAP_EMPLOYEE}"
    ExcelImportService excelImportService = new ExcelImportService()
    List data = excelImportService.convertColumnMapConfigManyRows(workbook, COLUMN_MAP_EMPLOYEE)
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

  def parseXlsPrePaysheet(def file) {
		File xlsFile = getFileToProcess(file)
    Workbook workbook = getWorkbookFromXlsFile(xlsFile)
    COLUMN_MAP_PREPAYSHEET.sheet = workbook.getSheetName(0)
    log.info "Column Map: ${COLUMN_MAP_PREPAYSHEET}"
    ExcelImportService excelImportService = new ExcelImportService()
    List data = excelImportService.convertColumnMapConfigManyRows(workbook, COLUMN_MAP_PREPAYSHEET)
    log.info "Data: ${data}"
    validateNotEmptyData(data)
    data
  }

}
