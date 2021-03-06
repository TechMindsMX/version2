package com.modulus.uno.paysheet

import com.modulus.uno.DataImssEmployeeService
import com.modulus.uno.DataImssEmployee
import com.modulus.uno.EmployeeLink
import com.modulus.uno.PaymentPeriod
import com.modulus.uno.BusinessEntity
import com.modulus.uno.Company
import java.math.RoundingMode
import grails.transaction.Transactional
import org.springframework.transaction.annotation.Propagation

class PaysheetEmployeeService {

  BreakdownPaymentEmployeeService breakdownPaymentEmployeeService
  PaysheetProjectService paysheetProjectService
  DataImssEmployeeService dataImssEmployeeService
  PaysheetReceiptService paysheetReceiptService
  def grailsApplication

  @Transactional
  PaysheetEmployee createPaysheetEmployeeFromPrePaysheetEmployee(Paysheet paysheet, PrePaysheetEmployee prePaysheetEmployee) {
    def banksLayout = grailsApplication.config.paysheet.banks.split(",")
    PaysheetEmployee paysheetEmployee = new PaysheetEmployee(
      prePaysheetEmployee:prePaysheetEmployee,
      paysheet:paysheet,
      breakdownPayment: new BreakdownPaymentEmployee(),
      ivaRate: new BigDecimal(grailsApplication.config.iva).setScale(2, RoundingMode.HALF_UP),
			paymentWay: (prePaysheetEmployee.bank && prePaysheetEmployee.account && banksLayout.contains(prePaysheetEmployee.bank?.bankingCode)) || (!banksLayout.contains(prePaysheetEmployee.bank?.bankingCode) && prePaysheetEmployee.clabe) ? PaymentWay.BANKING : PaymentWay.ONLY_CASH
    )

    if (prePaysheetEmployee.netPayment > 0) {
      BigDecimal baseImssMonthlySalary = getBaseMonthlyImssSalary(paysheetEmployee)
      paysheetEmployee.breakdownPayment = breakdownPaymentEmployeeService.generateBreakdownPaymentEmployee(paysheetEmployee)
      paysheetEmployee.salaryImss = calculateImssSalary(paysheetEmployee)
      paysheetEmployee.socialQuota = calculateSocialQuota(paysheetEmployee)
      paysheetEmployee.subsidySalary = calculateSubsidySalary(paysheetEmployee)
      paysheetEmployee.incomeTax = calculateIncomeTax(baseImssMonthlySalary, paysheetEmployee.prePaysheetEmployee.prePaysheet.paymentPeriod)
      paysheetEmployee.netAssimilable = calculateNetAssimilableSalary(paysheetEmployee)
      paysheetEmployee.crudeAssimilable = calculateCrudeAssimilableSalary(paysheetEmployee)
      paysheetEmployee.incomeTaxAssimilable = paysheetEmployee.crudeAssimilable - paysheetEmployee.netAssimilable
      paysheetEmployee.socialQuotaEmployer = calculateSocialQuotaEmployer(paysheetEmployee)
      paysheetEmployee.paysheetTax = calculatePaysheetTax(paysheetEmployee)
      paysheetEmployee.commission = calculateCommission(paysheetEmployee)
    }
    paysheetEmployee.save()
    paysheetEmployee
  }

  BigDecimal calculateImssSalary(PaysheetEmployee paysheetEmployee) {
    calculateProportionalAmountFromPaymentPeriod(getBaseMonthlyImssSalary(paysheetEmployee), paysheetEmployee.paysheet.prePaysheet.paymentPeriod)
  }

  BigDecimal getBaseMonthlyImssSalary(PaysheetEmployee paysheetEmployee) {
    EmployeeLink employeeLink = EmployeeLink.findByEmployeeRefAndCompany(paysheetEmployee.prePaysheetEmployee.rfc, paysheetEmployee.paysheet.paysheetContract.company)
    DataImssEmployee dataImssEmployee = dataImssEmployeeService.getDataImssForEmployee(employeeLink)
    dataImssEmployee.baseImssMonthlySalary
  }

  BigDecimal calculateSocialQuota(PaysheetEmployee paysheetEmployee) {
    calculateProportionalAmountFromPaymentPeriod(paysheetEmployee.breakdownPayment.socialQuotaEmployeeTotal, paysheetEmployee.paysheet.prePaysheet.paymentPeriod)
  }

  BigDecimal calculateSubsidySalary(PaysheetEmployee paysheetEmployee) {
    BigDecimal baseImssMonthlySalary = getBaseMonthlyImssSalary(paysheetEmployee)
    EmploymentSubsidy employmentSubsidy = EmploymentSubsidy.values().find { sb ->
      baseImssMonthlySalary >= sb.lowerLimit && baseImssMonthlySalary <= sb.upperLimit
    }
    employmentSubsidy ? calculateProportionalAmountFromPaymentPeriod(employmentSubsidy.getSubsidy(), paysheetEmployee.paysheet.prePaysheet.paymentPeriod) : new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateIncomeTax(BigDecimal monthlySalary, PaymentPeriod paymentPeriod) {
    RateTax rateTax = getRateTaxForMonthlySalary(monthlySalary)
    if (!rateTax) {
      return new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)
    }

    BigDecimal excess = monthlySalary - rateTax.lowerLimit
    BigDecimal marginalTax = excess * (rateTax.rate/100)
    calculateProportionalAmountFromPaymentPeriod(marginalTax + rateTax.fixedQuota, paymentPeriod)
  }

  BigDecimal calculateNetAssimilableSalary(PaysheetEmployee paysheetEmployee) {
    if (getMonthlyAssimilableForEmployee(paysheetEmployee) > 0) {
    BigDecimal netAssimilable = (paysheetEmployee.prePaysheetEmployee.netPayment - paysheetEmployee.imssSalaryNet).setScale(2, RoundingMode.HALF_UP)
    netAssimilable > 0 ? netAssimilable : new BigDecimal(0)
    } else {
      new BigDecimal(0)
    }
  }

  BigDecimal getMonthlyAssimilableForEmployee(PaysheetEmployee paysheetEmployee) {
    EmployeeLink employeeLink = EmployeeLink.findByEmployeeRefAndCompany(paysheetEmployee.prePaysheetEmployee.rfc, paysheetEmployee.paysheet.paysheetContract.company)
    DataImssEmployee dataImssEmployee = dataImssEmployeeService.getDataImssForEmployee(employeeLink)
    dataImssEmployee.monthlyAssimilableSalary
  }

  BigDecimal calculateSocialQuotaEmployer(PaysheetEmployee paysheetEmployee) {
    (paysheetEmployee.breakdownPayment.socialQuotaEmployer / 30 * paysheetEmployee.paysheet.prePaysheet.paymentPeriod.getDays()).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculatePaysheetTax(PaysheetEmployee paysheetEmployee) {
    BigDecimal baseImssMonthlySalary = getBaseMonthlyImssSalary(paysheetEmployee)
    calculateProportionalAmountFromPaymentPeriod(baseImssMonthlySalary * (new BigDecimal(grailsApplication.config.paysheet.paysheetTax)/100), paysheetEmployee.paysheet.prePaysheet.paymentPeriod)    
  }

  BigDecimal calculateCommission(PaysheetEmployee paysheetEmployee) {
    PaysheetProject project = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(paysheetEmployee.paysheet.paysheetContract, paysheetEmployee.paysheet.prePaysheet.paysheetProject)
    (paysheetEmployee.paysheetCost * (project.commission/100)).setScale(2, RoundingMode.HALF_UP)
  }

  BigDecimal calculateProportionalAmountFromPaymentPeriod(BigDecimal amountMonthly, PaymentPeriod paymentPeriod) {
    (amountMonthly / 30 * paymentPeriod.getDays()).setScale(2, RoundingMode.HALF_UP)
  }

	@Transactional
	void changePaymentWayFromEmployee(PaysheetEmployee employee){
		employee.paymentWay = employee.paymentWay == PaymentWay.BANKING ? PaymentWay.CASH : PaymentWay.BANKING
		employee.save()
	}

  BigDecimal calculateCrudeAssimilableSalary(PaysheetEmployee paysheetEmployee) {
    if (paysheetEmployee.netAssimilable > 0) {
      BigDecimal monthlyNetAssimilable = ((paysheetEmployee.netAssimilable / paysheetEmployee.paysheet.prePaysheet.paymentPeriod.days) * 30).setScale(2, RoundingMode.HALF_UP)
      BigDecimal monthlyCrudeAssimilable = calculateCrudeIASFromNetIAS(monthlyNetAssimilable)
      calculateProportionalAmountFromPaymentPeriod(monthlyCrudeAssimilable, paysheetEmployee.paysheet.prePaysheet.paymentPeriod)
    } else {
      new BigDecimal(0)
    }
  }

  BigDecimal calculateCrudeIASFromNetIAS(BigDecimal netIAS) {
    RateTax temporalRateTax = getRateTaxForMonthlySalary(netIAS)
    BigDecimal approximateSalary = (netIAS * (1 + (temporalRateTax.rate / 100))).setScale(2, RoundingMode.HALF_UP)
    RateTax realRateTax = getRateTaxForMonthlySalary(approximateSalary)
    BigDecimal crudeIAS = (netIAS + realRateTax.fixedQuota -(realRateTax.lowerLimit * (realRateTax.rate/100))) / (1 - (realRateTax.rate/100))
    crudeIAS.setScale(2, RoundingMode.HALF_UP)
  }

  RateTax getRateTaxForMonthlySalary(BigDecimal monthlySalary) {
     RateTax.values().find { rt ->
      monthlySalary >= rt.lowerLimit && monthlySalary <= rt.upperLimit
    }
  } 

  PaysheetEmployee setIMSSPayedStatusToEmployee(PaysheetEmployee paysheetEmployee) {
    paysheetEmployee.status = PaysheetEmployeeStatus.IMSS_PAYED
    paysheetEmployee.save()
    paysheetEmployee
  }

  PaysheetEmployee setASSIMILABLEPayedStatusToEmployee(PaysheetEmployee paysheetEmployee) {
    paysheetEmployee.status = PaysheetEmployeeStatus.ASSIMILABLE_PAYED
    paysheetEmployee.save()
    paysheetEmployee
  }

  PaysheetEmployee setPayedStatusToEmployee(PaysheetEmployee paysheetEmployee) {
    paysheetEmployee.status = PaysheetEmployeeStatus.PAYED
    paysheetEmployee.save()
    paysheetEmployee
  }

  PaysheetEmployee setRejectedStatusToEmployee(PaysheetEmployee paysheetEmployee) {
    paysheetEmployee.status = PaysheetEmployeeStatus.REJECTED
    paysheetEmployee.save()
    paysheetEmployee
  }

  @Transactional
  PaysheetEmployee savePaysheetReceiptUuidIMSS(PaysheetEmployee paysheetEmployee, String paysheetReceiptUuid) {
    paysheetEmployee.paysheetReceiptUuidSA = paysheetReceiptUuid
    paysheetEmployee.save()
    paysheetEmployee
  }

  @Transactional
  PaysheetEmployee savePaysheetReceiptUuidAsimilable(PaysheetEmployee paysheetEmployee, String paysheetReceiptUuid) {
    paysheetEmployee.paysheetReceiptUuidIAS = paysheetReceiptUuid
    paysheetEmployee.save()
    paysheetEmployee
  }

  PaysheetEmployee setIMSSXmlStampedStatusToEmployee(PaysheetEmployee paysheetEmployee) {
    paysheetEmployee.status = PaysheetEmployeeStatus.IMSS_STAMPED_XML
    paysheetEmployee.save()
    paysheetEmployee
  }

  PaysheetEmployee setASSIMILABLEXmlStampedStatusToEmployee(PaysheetEmployee paysheetEmployee) {
    paysheetEmployee.status = PaysheetEmployeeStatus.ASSIMILABLE_STAMPED_XML
    paysheetEmployee.save()
    paysheetEmployee
  }

  PaysheetEmployee setFullXmlStampedStatusToEmployee(PaysheetEmployee paysheetEmployee) {
    paysheetEmployee.status = PaysheetEmployeeStatus.FULL_STAMPED_XML
    paysheetEmployee.save()
    paysheetEmployee
  }

  PaysheetEmployee setFullXmlStampedWithPdfStatusForSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    paysheetEmployee.status = schema == PaymentSchema.ASSIMILABLE ? PaysheetEmployeeStatus."FULL_STAMPED_XML_IMSS_PDF" : PaysheetEmployeeStatus."FULL_STAMPED_XML_ASSIMILABLE_PDF"
    paysheetEmployee.save()
    paysheetEmployee
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  PaysheetEmployee setStampedStatusToEmployee(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    employeeIsPayed(paysheetEmployee) && employeeHasSAAndIASPayment(paysheetEmployee) ? "set${schema.name()}XmlStampedStatusToEmployee"(paysheetEmployee) : employeeIsOnlySchemaStamped(paysheetEmployee) || (employeeIsPayed(paysheetEmployee) && employeeHasOnlySchemaPayment(paysheetEmployee)) ? setFullXmlStampedStatusToEmployee(paysheetEmployee) : employeeHasXmlAndPdfForSchemaOnly(paysheetEmployee) ? setFullXmlStampedWithPdfStatusForSchema(paysheetEmployee, schema) : paysheetEmployee
  }

  Boolean employeeIsPayed(PaysheetEmployee paysheetEmployee) {
    [PaysheetEmployeeStatus.PAYED, PaysheetEmployeeStatus.IMSS_PAYED, PaysheetEmployeeStatus.ASSIMILABLE_PAYED].contains(paysheetEmployee.status)
  }

  Boolean employeeHasSAAndIASPayment(PaysheetEmployee paysheetEmployee) {
    paysheetEmployee.imssSalaryNet && paysheetEmployee.netAssimilable
  }

  Boolean employeeIsOnlySchemaStamped(PaysheetEmployee paysheetEmployee) {
    [PaysheetEmployeeStatus.IMSS_STAMPED_XML, PaysheetEmployeeStatus.ASSIMILABLE_STAMPED_XML].contains(paysheetEmployee.status)
  }

  Boolean employeeHasXmlAndPdfForSchemaOnly(PaysheetEmployee paysheetEmployee) {
    [PaysheetEmployeeStatus.IMSS_STAMPED, PaysheetEmployeeStatus.ASSIMILABLE_STAMPED].contains(paysheetEmployee.status)
  }

  Boolean employeeHasOnlySchemaPayment(PaysheetEmployee paysheetEmployee) {
    !paysheetEmployee.imssSalaryNet || !paysheetEmployee.netAssimilable
  }

  @Transactional
  PaysheetEmployee reloadDataEmployee(PaysheetEmployee paysheetEmployee) {
    Company company = paysheetEmployee.paysheet.paysheetContract.company
    BusinessEntity businessEntity = company.businessEntities.find { be -> be.rfc == paysheetEmployee.prePaysheetEmployee.rfc }
    paysheetEmployee.prePaysheetEmployee.curp = businessEntity.curp
    paysheetEmployee.prePaysheetEmployee.nameEmployee = businessEntity.toString()
    paysheetEmployee.prePaysheetEmployee.numberEmployee = businessEntity.number
    paysheetEmployee.save()
    paysheetEmployee
  }

  PaysheetEmployee findEmployeeForRfcAndPaysheet(String rfc, Paysheet paysheet) {
    paysheet.employees.find { employee -> employee.prePaysheetEmployee.rfc == rfc }
  }

  @Transactional
  PaysheetEmployee generatePaysheetReceiptPdfIMSS(PaysheetEmployee employee) {
    paysheetReceiptService.generatePdfFromPaysheetReceiptForEmployeeAndSchema(employee, PaymentSchema.IMSS)
    setStatusForPdfGeneratedToEmployee(employee, PaymentSchema.IMSS)
  }

  @Transactional
  PaysheetEmployee generatePaysheetReceiptPdfASSIMILABLE(PaysheetEmployee employee) {
    paysheetReceiptService.generatePdfFromPaysheetReceiptForEmployeeAndSchema(employee, PaymentSchema.ASSIMILABLE)
    setStatusForPdfGeneratedToEmployee(employee, PaymentSchema.ASSIMILABLE)
  }

  @Transactional
  PaysheetEmployee setStatusForPdfGeneratedToEmployee(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    paysheetEmployee.status = paysheetEmployee.status == PaysheetEmployeeStatus."${schema.name()}_STAMPED_XML" ? PaysheetEmployeeStatus."${schema.name()}_STAMPED" : defineFullStampedStatusForEmployee(paysheetEmployee, schema)
    paysheetEmployee.save()
    paysheetEmployee
  }

  PaysheetEmployeeStatus defineFullStampedStatusForEmployee(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    paysheetEmployee.status == PaysheetEmployeeStatus."FULL_STAMPED_XML" ? PaysheetEmployeeStatus."FULL_STAMPED_XML_${schema.name()}_PDF" : PaysheetEmployeeStatus.FULL_STAMPED  
  }

}
