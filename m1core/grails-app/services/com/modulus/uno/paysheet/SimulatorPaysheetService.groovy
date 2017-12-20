
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

    def generateXLSForSimulator(List<PaysheetEmployee> paysheetEmployeeList){
       def data = employeeToExport(paysheetEmployeeList) 
       def properties = ['consecutivo','period','salaryImss','socialQuota','subsidySalary','incomeTax','incomeTaxIAS','salaryBruto','totalImss','salaryAssimilable','salaryAssimilableBruto','subtotal','socialQuotaEmployeeTotal','isn','nominalCost','commission','totalNominal','iva','totalBill' ]
       def headers = ['CONSECUTIVO','PERIODO','SALARIO IMSS BRUTO','CARGA SOCIAL TRABAJADOR','SUBSIDIO','ISR IMSS','ISR Assimilable','SALARIO BASE IMSS','SALARIO NETO','ASIMILABLE NETO','ASIMILABLE BRUTO','SUBTOTAL',"CARGA SOCIAL EMPRESA","ISN","COSTO NOMINAL","COMISION","TOTAL NÓMINA","IVA", "TOTAL A FACTURAR"]
       new WebXlsxExporter().with {
          fillRow(headers, 2)
          add(data,properties,3)
        }

    }

    def processXlsSimulator(file) {
        log.info "Processing massive registration for Employee"
        List data = xlsImportService.parseXlsPaysheetSimulator(file)
        List<PaysheetEmployee> paysheetEmployeeList = []
        data.each{ row ->
            if(row.SA_BRUTO && row.IAS_BRUTO && !row.IAS_NETO){ paysheetEmployeeList << processForSalaryBrutoAndIASBruto(row) }
            if(row.SA_BRUTO && !row.IAS_BRUTO && row.IAS_NETO){ paysheetEmployeeList << processForIASNetoAndSalaryBruto(row) }
        }
        paysheetEmployeeList
    }

    def processForSalaryNetoAndIASNeto(def row){
      println "SA y IA Netos"
      getPaysheetEmployeeWithCalcules(row)
    }

    def processForIASNetoAndSalaryBruto(def row){
      println "IAS_NETO y SA_BRUTO......" 
      row.IAS_BRUTO = calculateIASBruto(row.IAS_NETO, 0, row.SA_BRUTO)
      getPaysheetEmployeeWithCalcules(row)
    }

    def processForSalaryBrutoAndIASBruto(def row){
      println "SA_BRUTO y IAS_BRUTO" 
      getPaysheetEmployeeWithCalcules(row)
    }

    def processForSalaryNetoAndIASBruto(def row){
      println "SA_NETO y IAS_BRUTO"
      row.IAS_BRUTO = calculateIASBruto(row.IAS_NETO)
      getPaysheetEmployeeWithCalcules(row)
    }

    def processIASNetoAndIASBruto(def row){
      println "IAS_NETO y IAS_BRUTO"
    }

    PaysheetEmployee getPaysheetEmployeeWithCalcules(def row){
      PaysheetEmployee paysheetEmployee = createPaysheetEmployee(row) 
      paysheetEmployee.subsidySalary = calculateSubsidySalary(row.SA_BRUTO, row.PERIODO) //Aquí podría cambiar dependiendo del tipo de salario 
      paysheetEmployee.incomeTax = calculateIncomeTax(row.SA_BRUTO, row.PERIODO) // Salario neto
      paysheetEmployee.metaClass.incomeTaxIAS = calculateIncomeTax(row.IAS_BRUTO, row.PERIODO) 
      paysheetEmployee.metaClass.salaryBruto = calculateAmountForPeriod(row.SA_BRUTO, row.PERIODO)
      paysheetEmployee.metaClass.iasBruto = calculateAmountForPeriod(row.IAS_BRUTO, row.PERIODO)
      paysheetEmployee.metaClass.period = row.PERIODO
      paysheetEmployee.salaryAssimilable = calculateSalaryAssimilable(paysheetEmployee.iasBruto, paysheetEmployee.incomeTaxIAS) // IAS NETO
      paysheetEmployee.paysheetTax = calculatePaysheetTax(row.SA_BRUTO, row.PERIODO) // Salario Neto
      paysheetEmployee
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

  PaysheetEmployee createPaysheetEmployee(def row){
     PaysheetEmployee paysheetEmployee = new PaysheetEmployee(
       prePaysheetEmployee:new PrePaysheetEmployee(),
       breakdownPayment: new BreakdownPaymentEmployee(),
       ivaRate:new BigDecimal(grailsApplication.config.iva).setScale(2, RoundingMode.HALF_UP),
       paymentWay: PaymentWay.BANKING
     )
    paysheetEmployee = setAttributesPaysheetEmployee(paysheetEmployee, row)
  }

  PaysheetEmployee setAttributesPaysheetEmployee(PaysheetEmployee paysheetEmployee, def row){
    paysheetEmployee.breakdownPayment = breakdownPaymentEmployee(row) 
    paysheetEmployee.salaryImss = calculateAmountForPeriod(row.SA_BRUTO,row.PERIODO)
    paysheetEmployee.socialQuota = calculateAmountForPeriod(paysheetEmployee.breakdownPayment.socialQuotaEmployeeTotal,row.PERIODO) 
    paysheetEmployee.socialQuotaEmployer = calculateSocialQuotaEmployer(paysheetEmployee, row.PERIODO)
    paysheetEmployee.commission = calculateCommission(paysheetEmployee, row.COMISION)
    paysheetEmployee
  }

  BigDecimal calculateAmountForPeriod(BigDecimal amount, String period){
    PaymentPeriod paymentPeriod = PaymentPeriod.values().find(){it.toString() == period.toUpperCase()}
    (amount / 30 * paymentPeriod.getDays()).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateSubsidySalary(BigDecimal salaryNeto, String period){
    BigDecimal baseImssMonthlySalary = salaryNeto
    EmploymentSubsidy employmentSubsidy = EmploymentSubsidy.values().find { sb ->
      baseImssMonthlySalary >= sb.lowerLimit && baseImssMonthlySalary <= sb.upperLimit
    }
    employmentSubsidy ? calculateAmountForPeriod(employmentSubsidy.getSubsidy(), period) : new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculatePaysheetTax(BigDecimal salaryNeto, String period) {
    BigDecimal baseImssMonthlySalary = salaryNeto?:0
    calculateAmountForPeriod(baseImssMonthlySalary * (new BigDecimal(grailsApplication.config.paysheet.paysheetTax)/100), period)    
  }

  BigDecimal calculateSocialQuotaEmployer(PaysheetEmployee paysheetEmployee, String period) {
    PaymentPeriod paymentPeriod = PaymentPeriod.values().find(){it.toString() == period.toUpperCase()}
    (paysheetEmployee.breakdownPayment.socialQuotaEmployer / 30 * paymentPeriod.getDays()).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateCommission(PaysheetEmployee paysheetEmployee, BigDecimal commission) {
    (paysheetEmployee.paysheetCost * (commission/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateIncomeTax(BigDecimal salaryNeto, String period){
    BigDecimal baseImssMonthlySalary = salaryNeto
    RateTax rateTax = RateTax.values().find { rt ->
      baseImssMonthlySalary >= rt.lowerLimit && baseImssMonthlySalary <= rt.upperLimit
    }
    if (!rateTax) {
      return new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
    }

    BigDecimal excess = baseImssMonthlySalary - rateTax.lowerLimit
    BigDecimal marginalTax = excess * (rateTax.rate/100)
    calculateAmountForPeriod(marginalTax + rateTax.fixedQuota, period)
  }

  BreakdownPaymentEmployee breakdownPaymentEmployee(def row){
    BigDecimal integratedDailySalary =  getIntegratedDailySalary(row.SA_BRUTO, row.FACT_INTEGRA)
    BigDecimal baseQuotation = getBaseQuotation(integratedDailySalary)
    BigDecimal diseaseAndMaternityBase = getDiseaseAndMaternityBase(integratedDailySalary)
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

  BigDecimal getBaseQuotation(BigDecimal integratedDailySalary){
    integratedDailySalary * new BigDecimal(grailsApplication.config.paysheet.quotationDays)
  } 
  
  BigDecimal getDiseaseAndMaternityBase(BigDecimal integratedDailySalary) {
    BigDecimal limit = 3 * new BigDecimal(grailsApplication.config.paysheet.uma)
    BigDecimal diseaseAndMaternityBase = new BigDecimal(0)
    if (integratedDailySalary > limit) {
      diseaseAndMaternityBase = (integratedDailySalary - limit) * new BigDecimal(grailsApplication.config.paysheet.quotationDays)
    }
    diseaseAndMaternityBase.setScale(2, RoundingMode.HALF_UP)
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
        salaryAssimilable: employee.salaryAssimilable,
        salaryAssimilableBruto: employee.iasBruto,
        subtotal:employee.totalSalaryEmployee,
        socialQuotaEmployeeTotal: employee.breakdownPayment.socialQuotaEmployeeTotal,
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

  BigDecimal calculateIASNeto(BigDecimal iasBruto){
    iasBruto
  }


  BigDecimal calculateSalaryAssimilable(BigDecimal netPayment, BigDecimal imssSalaryNet) {
    println netPayment
    (netPayment - imssSalaryNet).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateIASBruto(BigDecimal iasNeto, BigDecimal SA_BRUTO){
    BigDecimal sANeto = SA_BRUTO
    iasNeto = sANeto -SA_BRUTO
    iasNeto
  } 

}