package com.modulus.uno

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

    log.info "Paysheet project saved: ${command.dump()}"

    redirect controller:"company", action:"show", id:company.id
  }

}
