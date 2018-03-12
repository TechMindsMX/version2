package com.modulus.uno.paysheet

import grails.transaction.Transactional
import java.math.RoundingMode
import pl.touk.excel.export.WebXlsxExporter
import java.text.SimpleDateFormat
import org.springframework.transaction.annotation.Propagation

import com.modulus.uno.BusinessEntityService
import com.modulus.uno.XlsImportService
import com.modulus.uno.EmployeeService
import com.modulus.uno.Company
import com.modulus.uno.BusinessEntity
import com.modulus.uno.DataImssEmployee
import com.modulus.uno.EmployeeLink
import com.modulus.uno.BankAccount

class PrePaysheetService {

  BusinessEntityService businessEntityService
	XlsImportService xlsImportService
	EmployeeService employeeService
  PaysheetProjectService paysheetProjectService
  SimulatorPaysheetService simulatorPaysheetService

  @Transactional
  PrePaysheet savePrePaysheet(PrePaysheet prePaysheet) {
    prePaysheet.save()
    log.info "Prepaysheet saved: ${prePaysheet.dump()}"
    prePaysheet
  }

  Map getListAndCountPrePaysheetsForPaysheetContract(PaysheetContract paysheetContract, Map params) {
    Map prePaysheets = [:]
    prePaysheets.list = PrePaysheet.findAllByPaysheetContract(paysheetContract, params)
    prePaysheets.total = PrePaysheet.countByPaysheetContract(paysheetContract)
    prePaysheets
  }

  List<BigDecimal> getNetPaymentForEmployees(List<BusinessEntity> beEmployees, PrePaysheet prePaysheet) {
    List<BigDecimal> netPayments = []
    int daysPeriod = prePaysheet.paymentPeriod.getDays()
    List dataImss = getDataImssForEmployees(beEmployees, prePaysheet.paysheetContract.company)
    dataImss.each { di ->
      BigDecimal netPayment = di ? (di.totalMonthlySalary/30*daysPeriod).setScale(2, RoundingMode.HALF_UP) : new BigDecimal(0)
      if (di && di.monthlyAssimilableSalary <= 0) {
        netPayment = getNetPaymentFromNetImssSalary(di.baseImssMonthlySalary, prePaysheet)
      }
      netPayments.add(netPayment)
    }
    netPayments
  }

  List<DataImssEmployee> getDataImssForEmployees(List<BusinessEntity> beEmployees, Company company) {
    List<DataImssEmployee> dataImss = []
    beEmployees.each { be ->
      EmployeeLink employee = EmployeeLink.findByEmployeeRefAndCompany(be.rfc, company)
      DataImssEmployee dataImssEmployee = DataImssEmployee.findByEmployee(employee)
      dataImss.add(dataImssEmployee)
    }
    dataImss
  }

  BigDecimal getNetPaymentFromNetImssSalary(BigDecimal monthlySASalary, PrePaysheet prePaysheet) {
    PaysheetProject paysheetProject = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(prePaysheet.paysheetContract, prePaysheet.paysheetProject)
    Map row = [SA_BRUTO:monthlySASalary, IAS_BRUTO:0, IAS_NETO:0, COMISION:0, PERIODO:prePaysheet.paymentPeriod.toString(), FACT_INTEGRA:paysheetProject.integrationFactor, RIESGO_TRAB:paysheetProject.occupationalRiskRate]
    PaysheetEmployee temporalPaysheetEmployee = simulatorPaysheetService.createPaysheetEmployee(row)
    temporalPaysheetEmployee.imssSalaryNet.setScale(2, RoundingMode.HALF_UP)
  }

  List<BusinessEntity> getEmployeesAvailableToAdd(PrePaysheet prePaysheet) {
    List<BusinessEntity> currentEmployees = obtainBusinessEntitiesFromEmployeesPrePaysheet(prePaysheet)
    PaysheetProject paysheetProject = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(prePaysheet.paysheetContract, prePaysheet.paysheetProject)
    def allEmployeesAvailable = paysheetProject.employees ?: prePaysheet.paysheetContract.employees
    (allEmployeesAvailable.toList() - currentEmployees).sort {it.toString()}
  }

  List<BusinessEntity> obtainBusinessEntitiesFromEmployeesPrePaysheet(prePaysheet) {
    List<BusinessEntity> beInPrePaysheet = []
    prePaysheet.employees.each { emp ->
      beInPrePaysheet.add(prePaysheet.paysheetContract.company.businessEntities.find { be -> be.rfc == emp.rfc })
    }
    beInPrePaysheet.sort{ it.id }
  }

  @Transactional
  def addEmployeesToPrePaysheet(PrePaysheet prePaysheet, Map params) {
    List<BusinessEntity> employees = businessEntityService.getBusinessEntitiesFromIds(params.entities)
    employees.each { employee ->
      createAndSavePrePaysheetEmployee(employee, prePaysheet, params)
    }
    log.info "Employees in prePaysheet: ${prePaysheet.employees}"
  }

  PrePaysheet createAndSavePrePaysheetEmployee(BusinessEntity employee, PrePaysheet prePaysheet, Map params) {
    BankAccount bankAccount = params."bankAccount${employee.id}" ? BankAccount.get(params."bankAccount${employee.id}") : null
    PrePaysheetEmployee prePaysheetEmployee = new PrePaysheetEmployee(
      rfc:employee.rfc,
      curp:employee.curp,
      numberEmployee:employee.number?:"SN",
      nameEmployee:employee.toString(),
      netPayment:new BigDecimal(params."netPayment${employee.id}"),
      prePaysheet:prePaysheet,
      note:params."note${employee.id}",
      bank:bankAccount?.banco,
      clabe:bankAccount?.clabe,
      branch:bankAccount?.branchNumber,
      account:bankAccount?.accountNumber,
      cardNumber:bankAccount?.cardNumber
    )
    prePaysheetEmployee.save()
    prePaysheet.addToEmployees(prePaysheetEmployee)
    prePaysheet.save()
    log.info "PrePaysheetEmployee saved: ${prePaysheetEmployee?.dump()}"
    prePaysheet
  }

  @Transactional
  def sendPrePaysheetToProcess(PrePaysheet prePaysheet) {
    prePaysheet.status = PrePaysheetStatus.IN_PROCESS
    prePaysheet.save()
    //TODO: enviar notificación al ejecutivo de cuenta
  }

  def exportPrePaysheetToXls(PrePaysheet prePaysheet) {
    Map employees = getEmployeesToExport(prePaysheet)
    new WebXlsxExporter().with {
      fillRow(["PROYECTO:", prePaysheet.paysheetProject],0)
      fillRow(["PERIODO DE PAGO:", prePaysheet.paymentPeriod, "DEL:", new SimpleDateFormat("dd-MM-yyyy").format(prePaysheet.initPeriod), "AL:", new SimpleDateFormat("dd-MM-yyyy").format(prePaysheet.endPeriod)],1)
      fillRow(["RESIDENTE:", prePaysheet.accountExecutive],2)
      fillRow(employees.headers, 4)
      add(employees.data, employees.properties, 5)
    }
  }

  Map getEmployeesToExport(PrePaysheet prePaysheet) {
    Map employees = [:]
    employees.headers = ['RFC','CURP','NOMBRE','NO. EMPL.','CÓD. BANCO','BANCO','CLABE', 'CUENTA', 'TARJETA','NETO A PAGAR','OBSERVACIONES']
    employees.properties = ['rfc', 'curp', 'nameEmployee', 'numberEmployee', 'bank.bankingCode', 'bank.name', 'clabe', 'account', 'cardNumber', 'netPayment', 'note']
    employees.data = prePaysheet.employees.sort {it.nameEmployee}
    employees
  }

  @Transactional
  PrePaysheet changeStatusToProcessed(PrePaysheet prePaysheet) {
    prePaysheet.status = PrePaysheetStatus.PROCESSED
    prePaysheet.save()
    prePaysheet
  }

  @Transactional
  def deleteEmployeeFromPrePaysheet(PrePaysheetEmployee prePaysheetEmployee) {
		deleteRelationsForPrePaysheetEmployee(prePaysheetEmployee)
    PrePaysheetEmployee.executeUpdate("delete PrePaysheetEmployee employee where employee.id = :id", [id: prePaysheetEmployee.id])
  }

	def deleteRelationsForPrePaysheetEmployee(PrePaysheetEmployee prePaysheetEmployee){
		log.info "Deleting incidences"
		prePaysheetEmployee.incidences.each { incidence ->
			prePaysheetEmployee.removeFromIncidences(incidence)
    	PrePaysheetEmployeeIncidence.executeUpdate("delete PrePaysheetEmployeeIncidence incidence where incidence.id = :id", [id: incidence.id])
		}
		prePaysheetEmployee.save()
		log.info "PrePaysheet employee incidences: ${prePaysheetEmployee.incidences}"

		log.info "Deleting paysheet employee from prepaysheet employee"
		List<PaysheetEmployee> paysheetEmployees = PaysheetEmployee.findAllByPrePaysheetEmployee(prePaysheetEmployee)
		paysheetEmployees.each { paysheetEmployee ->
    	PaysheetEmployee.executeUpdate("delete PaysheetEmployee employee where employee.id = :id", [id: paysheetEmployee.id])
		}
	}

  @Transactional
  PrePaysheetEmployeeIncidence saveIncidence(PrePaysheetEmployeeIncidence incidence) {
    log.info "Saving incidence: ${incidence.dump()}"
    incidence.save()
    incidence
  }

  @Transactional
  def deleteIncidenceFromPrePaysheetEmployee(PrePaysheetEmployeeIncidence incidence) {
    PrePaysheetEmployee.executeUpdate("delete PrePaysheetEmployeeIncidence incidence where incidence.id = :id", [id: incidence.id])
  }

	def createLayoutForPrePaysheet() {
		def headersEmployees = ['RFC', 'CURP', 'NO_EMPL', 'NOMBRE', 'CLABE', 'TARJETA', 'NETO_A_PAGAR', 'OBSERVACIONES']
    new WebXlsxExporter().with {
      fillRow(headersEmployees, 0)
    }
	}

	def processXlsPrePaysheet(def file, PrePaysheet prePaysheet) {
	  log.info "Processing xls for prepaysheet: ${prePaysheet.id}"
    List data = xlsImportService.parseXlsPrePaysheet(file)
    List results = processDataFromXls(data, prePaysheet)
    log.info "Data: ${data}"
    log.info "Results: ${results}"
    [dataEmployees:data, results:results]
	}

  List processDataFromXls(List data, PrePaysheet prePaysheet) {
    List results = []
    data.each { employee ->
      String result = createPrePaysheetEmployeeFromData(employee, prePaysheet)
      results.add(result)
			if (result == "Agregado") {
				addEmployeeToPrePaysheet(employee, prePaysheet)
			}
    }
    results
  }

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	String createPrePaysheetEmployeeFromData(Map dataEmployee, PrePaysheet prePaysheet) {
	  if (!employeeService.employeeAlreadyExistsInCompany(dataEmployee.RFC, prePaysheet.paysheetContract.company)) {
      transactionStatus.setRollbackOnly()
      return "Error: el empleado no está registrado en la empresa"
    }

		if (prePaysheet.employees.find { employee -> employee.rfc == dataEmployee.RFC }) {
			transactionStatus.setRollbackOnly()
			return "Error: el empleado ya está agregado a la prenómina"
		}

    if (!prePaysheet.paysheetContract.employees.find { employee -> employee.rfc==dataEmployee.RFC }) {
			transactionStatus.setRollbackOnly()
			return "Error: el empleado no está asociado al contrato de nómina"
    }

		EmployeeLink employeeLink = EmployeeLink.findByEmployeeRefAndCompany(dataEmployee.RFC, prePaysheet.paysheetContract.company)
		BusinessEntity businessEntity = prePaysheet.paysheetContract.employees.find { be -> be.rfc == dataEmployee.RFC }

		BankAccount bankAccount = businessEntity.banksAccounts.find { ba -> ba.clabe == dataEmployee.CLABE }
		if (!bankAccount) {
			transactionStatus.setRollbackOnly()
			return "Error: la cuenta CLABE no pertenece al empleado"
		}

		if (dataEmployee.NETO_A_PAGAR instanceof String && !dataEmployee.NETO_A_PAGAR.isNumber()) {
			transactionStatus.setRollbackOnly()
			return "Error: el bruto a pagar no es válido"
		}

		//crear el empleado de pre-nómina
		PrePaysheetEmployee prePaysheetEmployee = new PrePaysheetEmployee(
			rfc:businessEntity.rfc,
			curp:employeeLink.curp,
			numberEmployee:employeeLink.number,
			nameEmployee:businessEntity.toString(),
			bank:bankAccount.banco,
			clabe:bankAccount.clabe,
			account:bankAccount.accountNumber,
			cardNumber:bankAccount.cardNumber,
			netPayment:new BigDecimal(dataEmployee.NETO_A_PAGAR),
			note:dataEmployee.OBSERVACIONES,
			prePaysheet:prePaysheet
		)

		prePaysheetEmployee.save()
		log.info "Pre-paysheet employee saved: ${prePaysheetEmployee.dump()}"
		"Agregado"
	}

	@Transactional
	def addEmployeeToPrePaysheet(Map dataEmployee, PrePaysheet prePaysheet) {
		PrePaysheetEmployee prePaysheetEmployee = PrePaysheetEmployee.findByRfcAndPrePaysheet(dataEmployee.RFC, prePaysheet)
		prePaysheet.addToEmployees(prePaysheetEmployee)
		prePaysheet.save()
		log.info "prePaysheet employees: ${prePaysheet.employees}"
	}

  @Transactional
  def deletePrePaysheet(PrePaysheet prePaysheet) {
    prePaysheet.employees.each { employee ->
      employee.incidences.each { incidence ->
        deleteIncidenceFromPrePaysheetEmployee(incidence)
      }
      deleteEmployeeFromPrePaysheet(employee)
    }
    PrePaysheet.executeUpdate("delete PrePaysheet prePaysheet where prePaysheet.id = :id", [id: prePaysheet.id])
  }

  @Transactional
  def reject(PrePaysheet prePaysheet) {
    prePaysheet.status = PrePaysheetStatus.REJECTED
    prePaysheet.save()
  }

}
