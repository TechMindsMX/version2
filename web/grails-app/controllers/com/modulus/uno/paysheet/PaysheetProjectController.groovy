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

    redirect controller:"paysheetContract", action:"show", id:paysheetProject.paysheetContract.id
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
}
