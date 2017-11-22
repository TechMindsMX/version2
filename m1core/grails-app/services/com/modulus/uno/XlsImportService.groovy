package com.modulus.uno

import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.grails.plugins.excelimport.*

class XlsImportService {

  Map COLUMN_MAP_EMPLOYEE = [
    startRow: 1,
    columnMap:  ['A':'RFC', 'B':'CURP', 'C':'PATERNO', 'D':'MATERNO', 'E':'NOMBRE', 'F':'NO_EMPL', 'G':'CLABE', 'H':'NUMTARJETA', 'I':'IMSS', 'J':'NSS', 'K':'FECHA_ALTA', 'L':'BASE_COTIZA', 'M':'NETO', 'N':'PRIMA_VAC', 'O':'DIAS_AGUINALDO', 'P':'PERIODO_PAGO']
  ]
		

	Map COLUMN_MAP_PREPAYSHEET = [
		startRow:1,
		columnMap: ['A':'RFC', 'B':'CURP','C':'NO_EMPL','D':'NOMBRE','E':'CLABE','F':'TARJETA','G':'NETO','H':'OBSERVACIONES']
	]

  Map COLUMN_MAP_SIMULATOR = [
    startRow:1,
    columnMap:['A':'RFC', 'B':'CURP','C':'NO_EMPL','D':'NOMBRE','E':'CLABE','F':'TARJETA','G':'NETO','H':'OBSERVACIONES','N':'SALARY']
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

  def parseXlsPaysheetSimulator(def file){
		File xlsFile = getFileToProcess(file)
    Workbook workbook = getWorkbookFromXlsFile(xlsFile)
    COLUMN_MAP_PREPAYSHEET.sheet = workbook.getSheetName(0)
    log.info "Column Map: ${COLUMN_MAP_SIMULATOR}"
    ExcelImportService excelImportService = new ExcelImportService()
    List data = excelImportService.convertColumnMapConfigManyRows(workbook, COLUMN_MAP_SIMULATOR)
    log.info "Data: ${data}"
    validateNotEmptyData(data)
    data
  }

}
