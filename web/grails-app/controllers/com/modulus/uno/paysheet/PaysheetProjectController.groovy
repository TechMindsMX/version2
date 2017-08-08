package com.modulus.uno.paysheet

import com.modulus.uno.Company

class PaysheetProjectController {

  PaysheetProjectService paysheetProjectService

  def create() {
    Company company = Company.get(session.company)
    respond new PaysheetProject(), model:[company:company]
  }

  def save(PaysheetProjectCommand command) {
    log.info "Saving paysheet project command: ${command.dump()}"
    Company company = Company.get(session.company)
    if (command.hasErrors()) {
      render view:"create", model:[paysheetProject:command, company:company]
      return
    }

    PaysheetProject paysheetProject = command.createPaysheetProject()
    paysheetProjectService.savePaysheetProject(paysheetProject)

    if (paysheetProject.hasErrors()) {
      render view:"create", model:[paysheetProject:paysheetProject, company:company]
      return
    }

    log.info "Paysheet project saved: ${paysheetProject.dump()}"

    redirect controller:"company", action:"show", id:company.id
  }

  def edit(PaysheetProject paysheetProject) {
    Company company = Company.get(session.company)
    respond paysheetProject, model:[company:company]
  }

  def update(PaysheetProjectCommand command) {
    log.info "Paysheet Project to update: ${command.dump()}"
    Company company = Company.get(session.company)
    if (command.hasErrors()) {
      render view:"edit", model:[paysheetProject:command, company:company]
      return
    }

    PaysheetProject paysheetProject = PaysheetProject.get(params.id)
    paysheetProject.properties = command.createPaysheetProject().properties

    paysheetProjectService.savePaysheetProject(paysheetProject)

    if (paysheetProject.hasErrors()) {
      render view:"edit", model:[paysheetProject:paysheetProject, company:company]
      return
    }

    log.info "Paysheet Project Instance updated: ${paysheetProject.dump()}"

    redirect controller:"company", action:"show", id:company.id
  }

  def delete(PaysheetProject paysheetProject) {
    log.info "Deleting paysheet project ${paysheetProject.dump()}"
    Company company = Company.get(session.company)
    paysheetProjectService.deletePaysheetProject(paysheetProject)
    redirect controller:"company", action:"show", id:company.id
  }

}
