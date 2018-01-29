package com.modulus.uno.paysheet

import pl.touk.excel.export.WebXlsxExporter
import com.modulus.uno.XlsImportService
import java.math.RoundingMode
import com.modulus.uno.PaymentPeriod

class SimulatorPaysheetService {

  XlsImportService xlsImportService
  def grailsApplication
  BreakdownPaymentEmployeeService breakdownPaymentEmployeeService
  PaysheetEmployeeService paysheetEmployeeService

  def generateLayoutForSimulator() {
    def message = ["Todos las cantidades deben ser mensuales"]
    def headers = ['CONSECUTIVO','SA_BRUTO','IAS_BRUTO','IAS_NETO','PERIODO','RIESGO_TRAB',"FACT_INTEGRA","COMISION"]
    def descriptions = ['NUMERO','','','',"Semanal, Catorcenal, Quincenal, Mensual"]
    new WebXlsxExporter().with {
      fillRow(message,1)
      fillRow(headers, 2)
      fillRow(descriptions, 3)
    }
  }

  def generateXLSForSimulator(List importResultList){
    def data = importResultList
    def properties = ['consecutive', 'result', 'row.PERIODO','simulatedPaysheetEmployee.salaryImss','simulatedPaysheetEmployee.socialQuota','simulatedPaysheetEmployee.subsidySalary','simulatedPaysheetEmployee.incomeTax','simulatedPaysheetEmployee.imssSalaryNet','simulatedPaysheetEmployee.crudeAssimilable','simulatedPaysheetEmployee.incomeTaxAssimilable','simulatedPaysheetEmployee.netAssimilable','simulatedPaysheetEmployee.totalSalaryEmployee','simulatedPaysheetEmployee.socialQuotaEmployer','simulatedPaysheetEmployee.paysheetTax','simulatedPaysheetEmployee.paysheetCost','simulatedPaysheetEmployee.commission','simulatedPaysheetEmployee.paysheetTotal','simulatedPaysheetEmployee.paysheetIva','simulatedPaysheetEmployee.totalToInvoice' ]
    def headers = ['CONSECUTIVO','RESULTADO', 'PERIODO','SALARIO IMSS BRUTO','CARGA SOCIAL TRABAJADOR','SUBSIDIO','ISR IMSS','SALARIO NETO','ASIMILABLE BRUTO','ISR ASIMILABLE','ASIMILABLE NETO','SUBTOTAL',"CARGA SOCIAL EMPRESA","ISN","COSTO NOMINAL","COMISION","TOTAL NÓMINA","IVA", "TOTAL A FACTURAR"]
    new WebXlsxExporter().with {
      fillRow(headers, 0)
      add(data,properties,1)
    }
  }

  def processXlsSimulator(file) {
    log.info "Processing simulator file xls"
    List data = xlsImportService.parseXlsPaysheetSimulator(file)
    List results = []
    data.eachWithIndex { row, index ->
      log.info "Importing data: ${row}"
      Map resultImportRow = [:]
      resultImportRow.consecutive = index+1
      resultImportRow.row = row
      resultImportRow.result = validateRowToImport(row)
      resultImportRow.simulatedPaysheetEmployee = resultImportRow.result == "OK" ? createPaysheetEmployee(row) : new PaysheetEmployee(prePaysheetEmployee:new PrePaysheetEmployee())
      results.add(resultImportRow)
    }

    results
  }

  String validateRowToImport(def row) {
    if (row.CONSECUTIVO==null || row.SA_BRUTO==null || row.IAS_BRUTO==null || row.IAS_NETO==null || row.PERIODO==null || row.RIESGO_TRAB==null || row.FACT_INTEGRA==null || row.COMISION==null) {
      return "TODAS LAS COLUMNAS SON REQUERIDAS, FALTAN DATOS"
    }

    if (!row.CONSECUTIVO.toString().isNumber() || !row.SA_BRUTO.toString().isNumber() || !row.IAS_BRUTO.toString().isNumber() || !row.IAS_NETO.toString().isNumber() || !row.RIESGO_TRAB.toString().isNumber() || !row.FACT_INTEGRA.toString().isNumber() || !row.COMISION.toString().isNumber()) {
      return "AL MENOS UNA DE LAS COLUMNAS NO TIENE UN VALOR VÁLIDO"
    }

    if (row.IAS_BRUTO.toString().isNumber() && row.IAS_NETO.toString().isNumber()
      && new BigDecimal(row.IAS_BRUTO.toString())>0 && new BigDecimal(row.IAS_NETO.toString())>0) {
      return "LOS DOS CAMPOS DE IAS TIENEN UN NÚMERO MAYOR A CERO, SÓLO UNO DE ELLOS DEBE TENERLO"
    }
    
    if (row.SA_BRUTO.toString().isNumber() && row.IAS_BRUTO.toString().isNumber() && row.IAS_NETO.toString().isNumber()
      && new BigDecimal(row.SA_BRUTO.toString())<=0 && new BigDecimal(row.IAS_BRUTO.toString())<=0 && new BigDecimal(row.IAS_NETO.toString())<=0) {
      return "AL MENOS UNO DE LOS SALARIOS NO DEBE SER CERO"
    }

    PaymentPeriod paymentPeriod = PaymentPeriod.values().find(){it.toString() == row.PERIODO.toUpperCase()}
    if (!paymentPeriod) {
      return "EL PERIODO INDICADO NO EXISTE EN EL CATÁLOGO"
    }

    "OK"
  }

  PaysheetEmployee createPaysheetEmployee(def row){
    BigDecimal monthlyCrudeSA = new BigDecimal(row.SA_BRUTO.toString())
    BigDecimal monthlyCrudeIAS = new BigDecimal(row.IAS_BRUTO.toString())
    BigDecimal monthlyNetIAS = new BigDecimal(row.IAS_NETO.toString())
    BigDecimal commission = new BigDecimal(row.COMISION.toString())
    PaymentPeriod paymentPeriod = PaymentPeriod.values().find(){it.toString() == row.PERIODO.toUpperCase()}
    PaysheetEmployee paysheetEmployee = new PaysheetEmployee(
      prePaysheetEmployee:new PrePaysheetEmployee(),
      ivaRate:new BigDecimal(grailsApplication.config.iva).setScale(2, RoundingMode.HALF_UP),
      paymentWay: PaymentWay.BANKING,
      breakdownPayment: calculateBreakdownPaymentEmployee(row),
      salaryImss: paysheetEmployeeService.calculateProportionalAmountFromPaymentPeriod(monthlyCrudeSA, paymentPeriod),
      subsidySalary: calculateSubsidySalary(monthlyCrudeSA, paymentPeriod),
      incomeTax: paysheetEmployeeService.calculateIncomeTax(monthlyCrudeSA, paymentPeriod),
      paysheetTax: calculatePaysheetTax(monthlyCrudeSA, paymentPeriod)
    )

    if (monthlyCrudeIAS > 0) {
      paysheetEmployee.crudeAssimilable = paysheetEmployeeService.calculateProportionalAmountFromPaymentPeriod(monthlyCrudeIAS, paymentPeriod)
      paysheetEmployee.incomeTaxAssimilable = paysheetEmployeeService.calculateIncomeTax(monthlyCrudeIAS, paymentPeriod)
      paysheetEmployee.netAssimilable = (paysheetEmployee.crudeAssimilable - paysheetEmployee.incomeTaxAssimilable).setScale(2, RoundingMode.HALF_UP)
    } else if (monthlyNetIAS > 0) {
      paysheetEmployee.netAssimilable = paysheetEmployeeService.calculateProportionalAmountFromPaymentPeriod(monthlyNetIAS, paymentPeriod)
      BigDecimal monthlyCrudeAssimilable = paysheetEmployeeService.calculateCrudeIASFromNetIAS(monthlyNetIAS)
      paysheetEmployee.crudeAssimilable = paysheetEmployeeService.calculateProportionalAmountFromPaymentPeriod(monthlyCrudeAssimilable, paymentPeriod)
      paysheetEmployee.incomeTaxAssimilable = (paysheetEmployee.crudeAssimilable - paysheetEmployee.netAssimilable).setScale(2, RoundingMode.HALF_UP)
    }

    paysheetEmployee.socialQuota = paysheetEmployeeService.calculateProportionalAmountFromPaymentPeriod(paysheetEmployee.breakdownPayment.socialQuotaEmployeeTotal, paymentPeriod)
    paysheetEmployee.socialQuotaEmployer = (paysheetEmployee.breakdownPayment.socialQuotaEmployer / 30 * paymentPeriod.getDays()).setScale(2, RoundingMode.HALF_UP)
    
    paysheetEmployee.commission = (paysheetEmployee.paysheetCost * (commission/100)).setScale(2, RoundingMode.HALF_UP)

    paysheetEmployee
  }

  BigDecimal calculateSubsidySalary(BigDecimal baseImssMonthlySalary, PaymentPeriod paymentPeriod){
    EmploymentSubsidy employmentSubsidy = EmploymentSubsidy.values().find { sb ->
      baseImssMonthlySalary >= sb.lowerLimit && baseImssMonthlySalary <= sb.upperLimit
    }
    employmentSubsidy ? paysheetEmployeeService.calculateProportionalAmountFromPaymentPeriod(employmentSubsidy.getSubsidy(), paymentPeriod) : new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculatePaysheetTax(BigDecimal baseImssMonthlySalary, PaymentPeriod paymentPeriod) {
    paysheetEmployeeService.calculateProportionalAmountFromPaymentPeriod(baseImssMonthlySalary * (new BigDecimal(grailsApplication.config.paysheet.paysheetTax)/100), paymentPeriod)
  }

  BreakdownPaymentEmployee calculateBreakdownPaymentEmployee(def row) {
    BigDecimal crudeSA = new BigDecimal(row.SA_BRUTO.toString())
    if (crudeSA <= 0) {
      return new BreakdownPaymentEmployee()
    }

    BigDecimal integratedDailySalary = getIntegratedDailySalary(row.SA_BRUTO, row.FACT_INTEGRA)
    BigDecimal baseQuotation = breakdownPaymentEmployeeService.getBaseQuotation(integratedDailySalary) ?: 0
    BigDecimal diseaseAndMaternityBase = breakdownPaymentEmployeeService.getDiseaseAndMaternityBase(integratedDailySalary) ?:0
    BreakdownPaymentEmployee breakdownPaymentEmployee = new BreakdownPaymentEmployee(
      integratedDailySalary: integratedDailySalary,
      baseQuotation: baseQuotation,
      fixedFee: breakdownPaymentEmployeeService.getFixedFee() ?: 0,
      diseaseAndMaternityBase: diseaseAndMaternityBase,
      diseaseAndMaternityEmployer: breakdownPaymentEmployeeService.getDiseaseAndMaternityEmployer(diseaseAndMaternityBase) ?:0,
      diseaseAndMaternity: breakdownPaymentEmployeeService.getDiseaseAndMaternityEmployee(diseaseAndMaternityBase) ?:0,
      pension: breakdownPaymentEmployeeService.getPensionEmployee(baseQuotation) ?:0,
      pensionEmployer: breakdownPaymentEmployeeService.getPensionEmployer(baseQuotation) ?: 0,
      loan: breakdownPaymentEmployeeService.getLoanEmployee(baseQuotation) ?:0,
      loanEmployer: breakdownPaymentEmployeeService.getLoanEmployer(baseQuotation) ?:0,
      disabilityAndLife: breakdownPaymentEmployeeService.getDisabilityAndLifeEmployee(integratedDailySalary) ?:0,
      disabilityAndLifeEmployer: breakdownPaymentEmployeeService.getDisabilityAndLifeEmployer(integratedDailySalary) ?:0,
      kindergarten: breakdownPaymentEmployeeService.getKindergarten(baseQuotation) ?:0,
      occupationalRisk: getOccupationalRisk(baseQuotation, row.RIESGO_TRAB),
      retirementSaving: breakdownPaymentEmployeeService.getRetirementSaving(baseQuotation) ?:0,
      unemploymentAndEld: breakdownPaymentEmployeeService.getUnemploymentAndEldEmployee(baseQuotation) ?:0,
      unemploymentAndEldEmployer: breakdownPaymentEmployeeService.getUnemploymentAndEldEmployer(baseQuotation) ?:0,
      infonavit: breakdownPaymentEmployeeService.getInfonavit(baseQuotation) ?:0
    )
  }

  BigDecimal getIntegratedDailySalary(BigDecimal SA_BRUTO, BigDecimal FACT_INTEGRA){
    ((new BigDecimal(SA_BRUTO)) / 30 * (new BigDecimal(FACT_INTEGRA))).setScale(2, RoundingMode.HALF_UP)
  }


  BigDecimal getOccupationalRisk(BigDecimal baseQuotation, BigDecimal riskJob) {
    (baseQuotation * (riskJob/100)).setScale(2, RoundingMode.HALF_UP)
  }

  List<Map> employeeToExport(List<PaysheetEmployee> paysheetEmployeeList){
    List<Map> employeeToExportLit = []
    paysheetEmployeeList.eachWithIndex{ employee, index ->
      Map employeeToExport = [
        consecutivo: index + 1,
        period: employee.period,
        salaryImss: employee.salaryImss,
        socialQuota: employee.socialQuota,
        subsidySalary: employee.subsidySalary,
        incomeTax: employee.incomeTax,
        incomeTaxIAS: employee.incomeTaxIAS,
        totalImss:employee.imssSalaryNet,
        salaryBruto: employee.salaryBruto,
        salaryAssimilable: employee.netAssimilable,
        salaryAssimilableBruto: employee.iasBruto,
        subtotal:employee.totalSalaryEmployee,
        socialQuotaEmployeeTotal: employee  .socialQuotaEmployer,
        isn:employee.paysheetTax,
        nominalCost:employee.paysheetCost,
        commission: employee.commission,
        totalNominal:employee.paysheetTotal,
        iva:employee.paysheetIva,
        totalBill:employee.totalToInvoice
      ]
      employeeToExportLit << employeeToExport
    }
    employeeToExportLit
  }

}
