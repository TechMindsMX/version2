package com.modulus.uno.paysheet

import com.modulus.uno.Company

class PaysheetProjectController {

  PaysheetProjectService paysheetProjectService

  def create(PaysheetContract paysheetContract) {
    respond new PaysheetProject(paysheetContract:paysheetContract)
  }

  def save(PaysheetProjectCommand command) {
    log.info "Saving paysheet project command: ${command.dump()}"
    if (command.hasErrors()) {
      render view:"create", model:[paysheetProject:command, paysheetContract:PaysheetContract.get(command.contractId)]
      return
    }

    PaysheetProject paysheetProject = command.createPaysheetProject()
    paysheetProjectService.savePaysheetProject(paysheetProject)

    if (paysheetProject.hasErrors()) {
      render view:"create", model:[paysheetProject:paysheetProject, paysheetContract:paysheetProject.paysheetContract]
      return
    }

    log.info "Paysheet project saved: ${paysheetProject.dump()}"

    redirect controller:"paysheetContract", action:"show", id:paysheetProject.paysheetContract.id
  }

  def edit(PaysheetProject paysheetProject) {
    respond paysheetProject
  }

  def update(PaysheetProjectCommand command) {
    log.info "Paysheet Project to update: ${command.dump()}"
    if (command.hasErrors()) {
      render view:"edit", model:[paysheetProject:command]
      return
    }

    PaysheetProject paysheetProject = PaysheetProject.get(params.id)
    paysheetProject.properties = command.createPaysheetProject().properties

    paysheetProjectService.savePaysheetProject(paysheetProject)

    if (paysheetProject.hasErrors()) {
      redirect action:"show", id:paysheetProject.id
      return
    }

    log.info "Paysheet Project Instance updated: ${paysheetProject.dump()}"

    redirect controller:"paysheetProject", action:"show", id:paysheetProject.id
  }

  def delete(PaysheetProject paysheetProject) {
    log.info "Deleting paysheet project ${paysheetProject.dump()}"
    PaysheetContract paysheetContract = paysheetProject.paysheetContract
    paysheetProjectService.deletePaysheetProject(paysheetProject)
    redirect controller:"paysheetContract", action:"show", id:paysheetContract.id
  }

  def show(PaysheetProject paysheetProject) {
    respond paysheetProject
  }
  
  def choosePayers(PaysheetProject paysheetProject) {
    List<Company> corporateCompanies = paysheetProjectService.getCompaniesInCorporate(session.company.toLong())
    render view:"show", model:[paysheetProject:paysheetProject, payersList:corporateCompanies]
  }

  def addPayerCompany(PayerPaysheetProjectCommand command) {
    log.info "Payer Paysheet project command: ${command.dump()}"
    PayerPaysheetProject payerPaysheetProject = command.createPayerPaysheetProject()
    log.info "Payer Paysheet project to save: ${payerPaysheetProject.dump()}"
    paysheetProjectService.savePayerPaysheetProject(payerPaysheetProject)
   
    redirect action:"show", id:payerPaysheetProject.paysheetProject.id
  }

  def deletePayer(PayerPaysheetProject payer) {
    log.info "Deleting payer paysheet project: ${payer.dump()}"
    PaysheetProject paysheetProject = payer.paysheetProject
    paysheetProjectService.deletePayer(payer)
    redirect action:"show", id:paysheetProject.id
  }

  def chooseEmployees(PaysheetProject paysheetProject) {
    List employeesList = paysheetProjectService.getAvailableEmployeesToAdd(paysheetProject)
    render view:"show", model:[paysheetProject:paysheetProject, employeesList:employeesList]
  }

  def addEmployees(PaysheetProject paysheetProject) {
    log.info "Add employees: ${params.entities} to paysheet project ${paysheetProject.id}"
    if (!params.entities) {
      flash.message = "No seleccionó empleados"
      redirect action:"chooseEmployees", id:paysheetProject.id
      return
    }

    paysheetProjectService.addEmployeesToPaysheetProject(paysheetProject, params)

    redirect action:"show", id:paysheetProject.id 
  }

  def deleteEmployee(PaysheetProject paysheetProject) {
    log.info "Delete employee ${params.employeeId} from paysheet project ${paysheetProject.id}"
    paysheetProjectService.deleteEmployeeFromPaysheetProject(paysheetProject, params.employeeId.toLong()) 
    redirect action:"show", id:paysheetProject.id 
  }

  def chooseBillers(PaysheetProject paysheetProject) {
    List<Company> corporateCompanies = paysheetProjectService.getCompaniesInCorporate(session.company.toLong())
    render view:"show", model:[paysheetProject:paysheetProject, billersList:corporateCompanies]
  }

  def addBillerCompany(BillerPaysheetProjectCommand command) {
    log.info "Biller Paysheet project command: ${command.dump()}"
    BillerPaysheetProject billerPaysheetProject = command.createBillerPaysheetProject()
    log.info "Biller Paysheet project to save: ${billerPaysheetProject.dump()}"
    paysheetProjectService.saveBillerPaysheetProject(billerPaysheetProject)
   
    redirect action:"show", id:billerPaysheetProject.paysheetProject.id
  }

  def deleteBiller(BillerPaysheetProject biller) {
    log.info "Deleting biller paysheet project: ${biller.dump()}"
    PaysheetProject paysheetProject = biller.paysheetProject
    paysheetProjectService.deleteBiller(biller)
    redirect action:"show", id:paysheetProject.id
  }

}
