package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

import com.modulus.uno.BusinessEntity
import com.modulus.uno.Company
import com.modulus.uno.EmployeeLink
import com.modulus.uno.DataImssEmployee
import com.modulus.uno.BankAccount
import com.modulus.uno.Bank
import com.modulus.uno.BusinessEntityService
import com.modulus.uno.EmployeeService
import com.modulus.uno.PaymentPeriod

@TestFor(PrePaysheetService)
@Mock([PaysheetContract, PrePaysheet, PrePaysheetEmployee, BusinessEntity, Company, EmployeeLink, DataImssEmployee, BankAccount, Bank, PaysheetProject])
class PrePaysheetServiceSpec extends Specification {

  BusinessEntityService businessEntityService = Mock(BusinessEntityService)
  EmployeeService employeeService = Mock(EmployeeService)
  PaysheetProjectService paysheetProjectService = Mock(PaysheetProjectService)
  SimulatorPaysheetService simulatorPaysheetService = Mock(SimulatorPaysheetService)

  def setup() {
    service.businessEntityService = businessEntityService
    service.employeeService = employeeService
    service.paysheetProjectService = paysheetProjectService
    service.simulatorPaysheetService = simulatorPaysheetService
  }

  void "Should get employees available to add a prepaysheet when paysheet project has employees"() {
    given: "The Paysheet Contract"
      List allEmployees = [new BusinessEntity(rfc:"A").save(validate:false), new BusinessEntity(rfc:"B").save(validate:false), new BusinessEntity(rfc:"C").save(validate:false), new BusinessEntity(rfc:"D").save(validate:false), new BusinessEntity(rfc:"E").save(validate:false)]
      Company company = new Company(businessEntities:allEmployees).save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract(employees:[], company:company).save(validate:false)
      paysheetContract.employees = allEmployees
      paysheetContract.save(validate:false)
    and:"The Paysheet Project"
      PaysheetProject paysheetProject = new PaysheetProject(paysheetContract:paysheetContract, name:"theProject", employees:[]).save(validate:false)
      paysheetProject.employees = allEmployees.subList(0,3)
      paysheetProject.save(validate:false)
      paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(_, _) >> paysheetProject
    and:"The PrePaysheet"
      PrePaysheet prePaysheet = new PrePaysheet(paysheetContract:paysheetContract, paysheetProject:"theProject").save(validate:false)
      PrePaysheetEmployee employeePrePaysheet = new PrePaysheetEmployee(rfc:"B").save(validate:false)
      prePaysheet.addToEmployees(employeePrePaysheet)
      prePaysheet.save(validate:false)
    when:
      def result = service.getEmployeesAvailableToAdd(prePaysheet)
    then:
      result.size() == 2
      result.rfc == ["A", "C"]
  }

  void "Should create and save a employee to prePaysheet without bank account"() {
    given:"A prePaysheet"
      Company company = new Company().save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(company:company).save(validate:false)
    and:"A employee"
      BusinessEntity employee = new BusinessEntity(rfc:"RFC").save(validate:false)
      company.addToBusinessEntities(employee)
      company.save(validate:false)
      EmployeeLink empLink = new EmployeeLink(curp:"CURP", number:"NOEMP", employeeRef:"RFC", company:company).save(validate:false)
    and:"The params"
      Map params = [bankAccount1:null, netPayment1:"5000", note1:"TRAMITAR CUENTA"]
    when:
      service.createAndSavePrePaysheetEmployee(employee, prePaysheet, params)
    then:
      prePaysheet.employees.size() == 1
      prePaysheet.employees.first().rfc == "RFC"
      prePaysheet.employees.first().netPayment == 5000
  }

  void "Should create and save a employee to prePaysheet with bank account"() {
    given:"A prePaysheet"
      Company company = new Company().save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(company:company).save(validate:false)
    and:"A employee"
      BusinessEntity employee = new BusinessEntity(rfc:"RFC").save(validate:false)
      company.addToBusinessEntities(employee)
      company.save(validate:false)
      EmployeeLink empLink = new EmployeeLink(curp:"CURP", number:"NOEMP", employeeRef:"RFC", company:company).save(validate:false)
    and:"The params"
      Map params = [bankAccount1:"1", netPayment1:"5000", note1:"TRAMITAR CUENTA"]
    and:"The bank account"
      BankAccount bankAccount = new BankAccount(accountNumber:"cuenta", clabe:"clabe", cardNumber:"tarjeta", banco:new Bank().save(validate:false)).save(validate:false)
    when:
      service.createAndSavePrePaysheetEmployee(employee, prePaysheet, params)
    then:
      prePaysheet.employees.size() == 1
      prePaysheet.employees.first().rfc == "RFC"
      prePaysheet.employees.first().netPayment == 5000
      prePaysheet.employees.first().bank.id == 1
      prePaysheet.employees.first().clabe == "clabe"
  }

  void "Should get net payments for employees"() {
    given:"The employees"
      Company company = new Company().save(validate:false)
      List<BusinessEntity> beEmployees = [
        new BusinessEntity(rfc:"A").save(validate:false),
        new BusinessEntity(rfc:"B").save(validate:false),
        new BusinessEntity(rfc:"C").save(validate:false),
        new BusinessEntity(rfc:"D").save(validate:false)
      ]
      EmployeeLink elA = new EmployeeLink(employeeRef:"A", company:company).save(validate:false)
      EmployeeLink elB = new EmployeeLink(employeeRef:"B", company:company).save(validate:false)
      EmployeeLink elC = new EmployeeLink(employeeRef:"C", company:company).save(validate:false)
      EmployeeLink elD = new EmployeeLink(employeeRef:"D", company:company).save(validate:false)
    and:"The data imss"
      DataImssEmployee dieA = new DataImssEmployee(employee:elA, baseImssMonthlySalary:new BigDecimal(5000), totalMonthlySalary:new BigDecimal(10000)).save(validate:false)
      DataImssEmployee dieB = new DataImssEmployee(employee:elB, baseImssMonthlySalary:new BigDecimal(5000), totalMonthlySalary:new BigDecimal(5000)).save(validate:false)
      DataImssEmployee dieC = new DataImssEmployee(employee:elC, baseImssMonthlySalary:new BigDecimal(0), totalMonthlySalary:new BigDecimal(20000)).save(validate:false)
    and:"The paysheet project"
      PaysheetProject paysheetProject = new PaysheetProject(integrationFactor:1.0501, occupationalRiskRate:0.879).save(validate:false)
      paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(_, _) >> paysheetProject
    and:"The pre-paysheet"
      PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(paymentPeriod:PaymentPeriod.BIWEEKLY, paysheetContract:paysheetContract).save(validate:false)
    and:
      simulatorPaysheetService.createPaysheetEmployee(_) >> new PaysheetEmployee(salaryImss:2500, socialQuota:63.18, subsidySalary:162.44, incomeTax:166.57, prePaysheetEmployee:new PrePaysheetEmployee(incidences:[]))
    when:
      def result = service.getNetPaymentForEmployees(beEmployees, prePaysheet)
    then:
      result.size() == 4
      result[0] == 5000
      result[1] == 2432.69
      result[2] == 10000
      result[3] == 0
  }

  void "Should add a employee to prePaysheet from xls file to import"() {
    given:"A prePaysheet"
      Company company = new Company().save(validate:false)
      BusinessEntity beEmployee = new BusinessEntity(rfc:"RFC").save(validate:false)
      BankAccount bankAccount = new BankAccount(accountNumber:"cuenta", clabe:"CLABE", cardNumber:"tarjeta", banco:new Bank().save(validate:false)).save(validate:false)
			beEmployee.addToBanksAccounts(bankAccount)
			beEmployee.save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
      paysheetContract.addToEmployees(beEmployee)
      paysheetContract.save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(paysheetContract:paysheetContract).save(validate:false)
    and:"A data map employee to import"
			Map dataEmployee = [RFC:"RFC", CLABE:"CLABE", NETO_A_PAGAR:1000.0]
	  and:
      EmployeeLink empLink = new EmployeeLink(curp:"CURP", number:"NOEMP", employeeRef:"RFC", company:company).save(validate:false)
		and:
			employeeService.employeeAlreadyExistsInCompany(_, _) >> empLink
    when:
      String result = service.createPrePaysheetEmployeeFromData(dataEmployee, prePaysheet)
    then:
			result == "Agregado"
  }

  void "Should return employee don't exists error when add a employee to prePaysheet from xls file to import"() {
    given:"A prePaysheet"
      Company company = new Company().save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(paysheetContract:paysheetContract).save(validate:false)
    and:"A data map employee to import"
			Map dataEmployee = [RFC:"RFC", CLABE:"CLABE", NETO:1000.0]
		and:
			employeeService.employeeAlreadyExistsInCompany(_, _) >> null
    when:
      String result = service.createPrePaysheetEmployeeFromData(dataEmployee, prePaysheet)
    then:
			result == "Error: el empleado no está registrado en la empresa"
  }

  void "Should return clabe account error when add a employee to prePaysheet from xls file to import"() {
    given:"A prePaysheet"
      Company company = new Company().save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(paysheetContract:paysheetContract).save(validate:false)
    and:"A data map employee to import"
			Map dataEmployee = [RFC:"RFC", CLABE:"CLABE", NETO:1000.0]
	  and:
      BusinessEntity beEmployee = new BusinessEntity(rfc:"RFC").save(validate:false)
			company.addToBusinessEntities(beEmployee)
			company.save(validate:false)
      EmployeeLink empLink = new EmployeeLink(curp:"CURP", number:"NOEMP", employeeRef:"RFC", company:company).save(validate:false)
      paysheetContract.addToEmployees(beEmployee)
      paysheetContract.save(validate:false)
    and:"The bank account"
      BankAccount bankAccount = new BankAccount(accountNumber:"cuenta", clabe:"ANOTHER_CLABE", cardNumber:"tarjeta", banco:new Bank().save(validate:false)).save(validate:false)
			beEmployee.addToBanksAccounts(bankAccount)
			beEmployee.save(validate:false)
		and:
			employeeService.employeeAlreadyExistsInCompany(_, _) >> empLink
    when:
      String result = service.createPrePaysheetEmployeeFromData(dataEmployee, prePaysheet)
    then:
			result == "Error: la cuenta CLABE no pertenece al empleado"
  }

  void "Should return payment amount error when add a employee to prePaysheet from xls file to import"() {
    given:"A prePaysheet"
      Company company = new Company().save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(paysheetContract:paysheetContract).save(validate:false)
    and:"A data map employee to import"
			Map dataEmployee = [RFC:"RFC", CLABE:"CLABE", NETO_A_PAGAR:"Mil"]
	  and:
      BusinessEntity beEmployee = new BusinessEntity(rfc:"RFC").save(validate:false)
			company.addToBusinessEntities(beEmployee)
			company.save(validate:false)
      EmployeeLink empLink = new EmployeeLink(curp:"CURP", number:"NOEMP", employeeRef:"RFC", company:company).save(validate:false)
      paysheetContract.addToEmployees(beEmployee)
      paysheetContract.save(validate:false)
    and:"The bank account"
      BankAccount bankAccount = new BankAccount(accountNumber:"cuenta", clabe:"CLABE", cardNumber:"tarjeta", banco:new Bank().save(validate:false)).save(validate:false)
			beEmployee.addToBanksAccounts(bankAccount)
			beEmployee.save(validate:false)
		and:
			employeeService.employeeAlreadyExistsInCompany(_, _) >> empLink
    when:
      String result = service.createPrePaysheetEmployeeFromData(dataEmployee, prePaysheet)
    then:
			result == "Error: el bruto a pagar no es válido"
  }

  void "Should return already exists employee in prepaysheet error when add a employee to prePaysheet from xls file to import"() {
    given:"A prePaysheet"
      Company company = new Company().save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(paysheetContract:paysheetContract).save(validate:false)
			PrePaysheetEmployee prePaysheetEmployee = new PrePaysheetEmployee(rfc:"RFC").save(validate:false)
			prePaysheet.addToEmployees(prePaysheetEmployee)
			prePaysheet.save(validate:false)
    and:"A data map employee to import"
			Map dataEmployee = [RFC:"RFC", CLABE:"CLABE", NETO:"Mil"]
	  and:
      BusinessEntity beEmployee = new BusinessEntity(rfc:"RFC").save(validate:false)
			company.addToBusinessEntities(beEmployee)
			company.save(validate:false)
      EmployeeLink empLink = new EmployeeLink(curp:"CURP", number:"NOEMP", employeeRef:"RFC", company:company).save(validate:false)
    and:"The bank account"
      BankAccount bankAccount = new BankAccount(accountNumber:"cuenta", clabe:"CLABE", cardNumber:"tarjeta", banco:new Bank().save(validate:false)).save(validate:false)
			beEmployee.addToBanksAccounts(bankAccount)
			beEmployee.save(validate:false)
		and:
			employeeService.employeeAlreadyExistsInCompany(_, _) >> empLink
    when:
      String result = service.createPrePaysheetEmployeeFromData(dataEmployee, prePaysheet)
    then:
			result == "Error: el empleado ya está agregado a la prenómina"
  }

  void "Should return employee isn't in the paysheet contract error when add a employee to prePaysheet from xls file to import"() {
    given:"A prePaysheet"
      Company company = new Company().save(validate:false)
      PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(paysheetContract:paysheetContract).save(validate:false)
			prePaysheet.save(validate:false)
    and:"A data map employee to import"
			Map dataEmployee = [RFC:"RFC", CLABE:"CLABE", NETO:"Mil"]
	  and:
      BusinessEntity beEmployee = new BusinessEntity(rfc:"RFC").save(validate:false)
			company.addToBusinessEntities(beEmployee)
			company.save(validate:false)
      EmployeeLink empLink = new EmployeeLink(curp:"CURP", number:"NOEMP", employeeRef:"RFC", company:company).save(validate:false)
    and:"The bank account"
      BankAccount bankAccount = new BankAccount(accountNumber:"cuenta", clabe:"CLABE", cardNumber:"tarjeta", banco:new Bank().save(validate:false)).save(validate:false)
			beEmployee.addToBanksAccounts(bankAccount)
			beEmployee.save(validate:false)
		and:
			employeeService.employeeAlreadyExistsInCompany(_, _) >> empLink
    when:
      String result = service.createPrePaysheetEmployeeFromData(dataEmployee, prePaysheet)
    then:
			result == "Error: el empleado no está asociado al contrato de nómina"
  }

}
