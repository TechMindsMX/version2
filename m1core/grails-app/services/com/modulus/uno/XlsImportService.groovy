package com.modulus.uno

import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.grails.plugins.excelimport.*

class XlsImportService {

  Map COLUMN_MAP_EMPLOYEE = [
    startRow: 1,
    columnMap:  ['A':'RFC', 'B':'CURP', 'C':'PATERNO', 'D':'MATERNO', 'E':'NOMBRE', 'F':'NO_EMPL','G':'BANCO', 'H':'CLABE','I':'CUENTA','J':'SUCURSAL', 'K':'NUMTARJETA', 'L':'IMSS', 'M':'NSS', 'N':'FECHA_ALTA', 'O':'SA_BRUTO', 'P':'IAS_BRUTO', 'Q':'PRIMA_VAC', 'R':'DIAS_AGUINALDO', 'S':'PERIODO_PAGO']
  ]
		
	Map COLUMN_MAP_PREPAYSHEET = [
		startRow:1,
		columnMap: ['A':'RFC', 'B':'CURP','C':'NO_EMPL','D':'NOMBRE','E':'CLABE','F':'TARJETA','G':'BRUTO_A_PAGAR','H':'OBSERVACIONES']
	]

  Map COLUMN_MAP_SIMULATOR = [
    startRow:3,
    columnMap:['A':'CONSECUTIVO','B':'SA_BRUTO','C':'IAS_BRUTO','D':'IAS_NETO','E':'PERIODO','F':'RIESGO_TRAB','G':'FACT_INTEGRA','H':'COMISION']
  ]

  Map CELL_MAP_SIMULATOR = [
    cellMap:['B1':'PERIODO']
  ]

  Map COLUMN_MAP_CLIENT = [
    startRow: 1,
    columnMap:  ['A':'PERSONA', 'B':'RFC', 'C':'SITIO_WEB', 'D':'RAZON_SOCIAL', 'E':'PATERNO', 'F':'MATERNO', 'G':'NOMBRE', 'H':'CLAVE_BANCO', 'I':'ULTIMOS_4_DIGITOS_TARJETA', 'J':'CALLE', 'K':'NUMEXTERIOR', 'L':'NUMINTERIOR', 'M':'CODIGO_POSTAL', 'N':'COLONIA', 'O':'DELEGACION/MUNICIPIO', 'P':'PAIS', 'Q':'CIUDAD', 'R':'ENTIDAD_FEDERATIVA', 'S':'TIPO_DE_DIRECCION']
  ]

  Map COLUMN_MAP_PROVIDER = [
    startRow: 1,
    columnMap:  ['A':'PERSONA', 'B':'RFC', 'C':'SITIO_WEB', 'D':'RAZON_SOCIAL', 'E':'PATERNO', 'F':'MATERNO', 'G':'NOMBRE', 'H':'CLABE', 'I':'CALLE', 'J':'NUMEXTERIOR', 'K':'NUMINTERIOR', 'L':'CODIGO_POSTAL', 'M':'COLONIA', 'N':'DELEGACION/MUNICIPIO', 'O':'PAIS', 'P':'CIUDAD', 'Q':'ENTIDAD_FEDERATIVA', 'R':'TIPO_DE_DIRECCION']
  ]

  Map COLUMN_MAP_CLIENT_PROVIDER = [
    startRow: 1,
    columnMap:  ['A':'PERSONA', 'B':'RFC', 'C':'SITIO_WEB', 'D':'RAZON_SOCIAL', 'E':'PATERNO', 'F':'MATERNO', 'G':'NOMBRE', 'H':'CLABE', 'I':'CALLE', 'J':'NUMEXTERIOR', 'K':'NUMINTERIOR', 'L':'CODIGO_POSTAL', 'M':'COLONIA', 'N':'DELEGACION/MUNICIPIO', 'O':'PAIS', 'P':'CIUDAD', 'Q':'ENTIDAD_FEDERATIVA', 'R':'TIPO_DE_DIRECCION']
  ]

  File getFileToProcess(def file) {
    File xlsFile = File.createTempFile("tmpXlsImport${new Date().getTime()}",".xlsx")
    FileOutputStream fos = new FileOutputStream(xlsFile)
    fos.write(file.getBytes())
    fos.close()
    xlsFile
  }

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

  def parseXlsMassiveClient_Provider(File xlsFile) {
    Workbook workbook = getWorkbookFromXlsFile(xlsFile)
    COLUMN_MAP_CLIENT_PROVIDER.sheet = workbook.getSheetName(0)
    log.info "Column Map: ${COLUMN_MAP_CLIENT_PROVIDER}"
    ExcelImportService excelImportService = new ExcelImportService()
    List data = excelImportService.convertColumnMapConfigManyRows(workbook, COLUMN_MAP_CLIENT_PROVIDER)
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
    log.info "Column Map: ${COLUMN_MAP_SIMULATOR}"
    ExcelImportService excelImportService = new ExcelImportService()
    List data = excelImportService.convertColumnMapConfigManyRows(workbook, COLUMN_MAP_PREPAYSHEET)
    log.info "Data: ${data}"
    validateNotEmptyData(data)
    data
  }

  def parseXlsPaysheetSimulator(def file) {
		File xlsFile = getFileToProcess(file)
    Workbook workbook = getWorkbookFromXlsFile(xlsFile)
    COLUMN_MAP_SIMULATOR.sheet = workbook.getSheetName(0)
    log.info "Column Map: ${COLUMN_MAP_SIMULATOR}"
    ExcelImportService excelImportService = new ExcelImportService()
    List data = excelImportService.convertColumnMapConfigManyRows(workbook, COLUMN_MAP_SIMULATOR)
    log.info "Data: ${data}"
    validateNotEmptyData(data)
    data
  }

  def parseXlsPaysheetSimulatorHeaders(def file) {
		File xlsFile = getFileToProcess(file)
    Workbook workbook = getWorkbookFromXlsFile(xlsFile)
    CELL_MAP_SIMULATOR.sheet = workbook.getSheetName(0)
    log.info "Column Map: ${CELL_MAP_SIMULATOR}"
    ExcelImportService excelImportService = new ExcelImportService()
    Map data = excelImportService.convertFromCellMapToMapWithValues(workbook, CELL_MAP_SIMULATOR)
    log.info "Data: ${data}"
    data
  }

}
