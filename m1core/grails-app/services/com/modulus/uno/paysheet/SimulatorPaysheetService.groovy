
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
       def headers = ['CONSECUTIVO','SA_BRUTO','IAS_BRUTO','IAS_NETO','PERIODO','RIESGO_TRAB',"FACT_INTEGRA","COMISION"]
       def descriptions = ['NUMERO','','','',"Semanal, Catorcenal, Quincenal, Mensual"]
       new WebXlsxExporter().with {
          fillRow(headers, 2)
          fillRow(descriptions, 3)
        }
    }

    def generateXLSForSimulator(List<PaysheetEmployee> paysheetEmployeeList){
       def data = employeeToExport(paysheetEmployeeList) 
       def properties = ['consecutivo','salaryImss','socialQuota','subsidySalary','incomeTax','totalImss','salaryAssimilable','subtotal','socialQuotaEmployeeTotal','isn','nominalCost','commission','totalNominal','iva','totalBill' ]
       def headers = ['CONSECUTIVO','SALARIO NETO','CARGA SOCIAL TRABAJADOR','SUBSIDIO','ISR','TOTAL IMSS','ASIMILABLE','SUBTOTAL',"CARGA SOCIAL EMPRESA","ISN","COSTO NOMINAL","COMISION","TOTAL NÓMINA","IVA", "TOTAL A FACTURAR"]
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
      paysheetEmployee.salaryAssimilable = calculateSalaryAssimilable(row.SA_BRUTO, paysheetEmployee.salaryImss) // IAS NETO
      paysheetEmployee.paysheetTax = calculatePaysheetTax(row.SA_BRUTO, row.PERIODO) // Salario Neto
      paysheetEmployee.metaClass.iasBruto = new BigDecimal(row.SA_BRUTO)
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
        salaryImss: employee.salaryImss,
        socialQuota: employee.socialQuota,
        subsidySalary: employee.subsidySalary,
        incomeTax: employee.incomeTax,
        totalImss:employee.imssSalaryNet,
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


  BigDecimal calculateSalaryNeto(BigDecimal salaryBruto){
    BigDecimal salaryNeto = 0
    BigDecimal isr = 1
    BigDecimal limitInferior = 0
    BigDecimal paymentFija = 0
    if(salaryBruto < 496.07){
      limitInferior = 0.01
      isr = 0.0192
      paymentFija = 0
    }
    else if(496.08 < salaryBruto && salaryBruto < 4210.41 ){
      limitInferior = 496.08
      isr = 0.0640
      paymentFija = 9.52
    } 
    else if(4210.42 < salaryBruto && salaryBruto < 7399.42 ){
      limitInferior = 4210.42
      isr = 0.1088
      paymentFija = 247.24
    }
    else if(7399.43 < salaryBruto && salaryBruto < 8601.50 ){
      limitInferior = 7399.43
      isr = 0.16
      paymentFija = 594.21
    }
    else if(8601.51  < salaryBruto && salaryBruto < 10298.35 ){
      limitInferior =8601.51 
      isr = 0.1792
      paymentFija = 786.54
    }
    else if(10298.36 < salaryBruto && salaryBruto < 20770.29 ){
      limitInferior = 10298.36
      isr = 0.2136
      paymentFija = 1090.51
    }
    else if(20770.30< salaryBruto && salaryBruto < 32736.83 ){
      limitInferior = 20770.30
      isr = 0.2352
      paymentFija = 3327.42
    }
    else if(32736.84 < salaryBruto && salaryBruto < 62500.00 ){
      limitInferior = 32736.84
      isr = 0.30
      paymentFija = 6141.95
    }
    else if(62500.01 < salaryBruto && salaryBruto < 83333.33 ){
      limitInferior = 62500.01
      isr = 0.32
      paymentFija = 15070.90
    }
    else if(83333.34 < salaryBruto && salaryBruto < 250000.00 ){
      limitInferior =83333.34
      isr = 0.34
      paymentFija = 21737.57
    }
    else{
      limitInferior = 250000.01
      isr = 0.35
      paymentFija = 78404.23
    }
    BigDecimal excessive = salaryBruto - limitInferior
    println "Excedido: ${excessive}"
    println "isr: ${isr}"
    println "fija : ${paymentFija}"
    println "limite Inferior : ${limitInferior}"
    salaryNeto = salaryBruto - ((excessive * isr) + paymentFija)
    println "Salario Neto : ${salaryNeto}"
    salaryNeto
  }

  BigDecimal calculateIASNeto(BigDecimal iasBruto){
    iasBruto
  }


  BigDecimal calculateSalaryAssimilable(BigDecimal netPayment, BigDecimal imssSalaryNet) {
    (netPayment - imssSalaryNet).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateIASBruto(BigDecimal iasNeto, BigDecimal SA_BRUTO){
    BigDecimal sANeto = calculateSalaryNeto(SA_BRUTO)
    iasNeto = sANeto -SA_BRUTO
    iasNeto
  } 

}