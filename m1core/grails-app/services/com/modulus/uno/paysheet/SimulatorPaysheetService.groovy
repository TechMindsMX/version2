
package com.modulus.uno.paysheet
import pl.touk.excel.export.WebXlsxExporter
import com.modulus.uno.XlsImportService
import java.math.RoundingMode
import com.modulus.uno.PaymentPeriod

class SimulatorPaysheetService {

  XlsImportService xlsImportService
  def grailsApplication
  BreakdownPaymentEmployeeService breakdownPaymentEmployeeService

    def generateLayoutForSimulator() {
       def headers = ['CONSECUTIVO','SA_MENSUAL','SA_NETO','IAS_NETO','SA_BRUTO','IAS_BRUTO','PERIODO','RIESGO_TRAB',"FACT_INTEGRA","COMISION"]
       def descriptions = ['NUMERO','','','','','',"Semanal, Catorcenal, Quincenal, Mensual"]
       new WebXlsxExporter().with {
          fillRow(headers, 2)
          fillRow(descriptions, 3)
        }
    }

    def generateXLSForSimulator(List<PaysheetEmployee> paysheetEmployeeList){
       def data = employeeToExport(paysheetEmployeeList) 
       def properties = ['consecutivo','salaryImss','socialQuota','subsidySalary','incomeTax','totalImss','salaryAssimilable','subtotal','socialQuotaEmployerTotal','isn','nominalCost','commission','totalNominal','iva','totalBill' ]
       def headers = ['CONSECUTIVO','SALARIO IMSS','CARGA SOCIAL TRABAJADOR','SUBSIDIO','ISR','TOTAL IMSS','ASIMILABLE','SUBTOTAL',"CARGA SOCIAL EMPRESA","ISN","COSTO NOMINAL","COMISION","TOTAL NÓMINA","IVA", "TOTAL A FACTURAR"]
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
            println row
            if(row.SA_NETO && row.IAS_NETO){PaysheetEmployee paysheetEmployee =  processForSalaryNetoAndIASNeto(row); paysheetEmployeeList << paysheetEmployee}
            if(row.IAS_NETO && row.SA_BRUTO){ processForIASNetoAndSalaryBruto(row) }
            if(row.SA_BRUTO && row.IAS_BRUTO){ processForSalaryBrutoAndIASBruto(row) }
            if(row.SA_NETO && row.IAS_BRUTO){ processForSalaryNetoAndIASBruto(row)}
            if(row.IAS_NETO && row.IAS_BRUTO){processIASNetoAndIASBruto(row) }
        }
        println paysheetEmployeeList.dump()
        paysheetEmployeeList
    }

    def processForSalaryNetoAndIASNeto(def row){
      println "SA y IA Netos"
      PaysheetEmployee paysheetEmployee = createPaysheetEmployee(row) 
    }

    def processForIASNetoAndSalaryBruto(def row){
      println "IAS_NETO y SA_BRUTO" 
    }

    def processForSalaryBrutoAndIASBruto(def row){
      println "SA_BRUTO y IAS_BRUTO" 
    }

    def processForSalaryNetoAndIASBruto(def row){
      println "SA_NETO y IAS_BRUTO"
    }

    def processIASNetoAndIASBruto(def row){
      println "IAS_NETO y IAS_BRUTO"
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
    paysheetEmployee.salaryImss = calculateAmountForPeriod(row.SA_MENSUAL,row.PERIODO)
    paysheetEmployee.socialQuota = calculateAmountForPeriod(paysheetEmployee.breakdownPayment.socialQuotaEmployeeTotal,row.PERIODO) 
    paysheetEmployee.subsidySalary = calculateSubsidySalary(row.SA_NETO, row.PERIODO) //Aquí podría cambiar dependiendo del tipo de salario 
    paysheetEmployee.incomeTax = calculateIncomeTax(row.SA_NETO, row.PERIODO) // Salario neto
    paysheetEmployee.salaryAssimilable = row.IAS_NETO // IAS NETO
    paysheetEmployee.socialQuotaEmployer = calculateSocialQuotaEmployer(paysheetEmployee, row.PERIODO)
    paysheetEmployee.paysheetTax = calculatePaysheetTax(row.SA_NETO, row.PERIODO) // Salario Neto
    paysheetEmployee.commission = calculateCommission(paysheetEmployee, row.COMISION)
    paysheetEmployee
  }

  BigDecimal calculateAmountForPeriod(BigDecimal amount, String period){
    PaymentPeriod paymentPeriod = PaymentPeriod.values().find(){it.toString() == period.toUpperCase()}
    (amount / 30 * paymentPeriod.getDays()).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateSubsidySalary(BigDecimal salaryNeto, String period){
    BigDecimal baseImssMonthlySalary = salaryNeto
    println baseImssMonthlySalary 
    EmploymentSubsidy employmentSubsidy = EmploymentSubsidy.values().find { sb ->
      baseImssMonthlySalary >= sb.lowerLimit && baseImssMonthlySalary <= sb.upperLimit
    }
    employmentSubsidy ? calculateAmountForPeriod(employmentSubsidy.getSubsidy(), period) : new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculatePaysheetTax(BigDecimal salaryNeto, String period) {
    BigDecimal baseImssMonthlySalary = salaryNeto
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
    BigDecimal integratedDailySalary =  getIntegratedDailySalary(row.SA_MENSUAL, row.FACT_INTEGRA)
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

  BigDecimal getIntegratedDailySalary(BigDecimal SA_MENSUAL, BigDecimal FACT_INTEGRA){
    ((new BigDecimal(SA_MENSUAL)) / 30 * (new BigDecimal(FACT_INTEGRA))).setScale(2, RoundingMode.HALF_UP)
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
        salaryImss: employee.salaryImss,
        socialQuota: employee.socialQuota,
        subsidySalary: employee.subsidySalary,
        incomeTax: employee.incomeTax,
        totalImss:0,
        salaryAssimilable: employee.salaryAssimilable,
        subtotal:0,
        socialQuotaEmployeeTotal: employee.breakdownPayment.socialQuotaEmployeeTotal,
        isn:0,
        nominalCost:0,
        commission: employee.commission,
        totalNominal:0,
        iva:employee.ivaRate,
        totalBill:0
      ]
      employeeToExportLit << employeeToExport
    }
    employeeToExportLit
  }


}