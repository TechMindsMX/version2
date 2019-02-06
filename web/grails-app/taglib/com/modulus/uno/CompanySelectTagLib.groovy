package com.modulus.uno

class CompanySelectTagLib {

  def companyService
  def springSecurityService
  def restService
  OrganizationService organizationService

  static namespace = "companyInfo"
  static defaultEncodeAs = "raw"

  def companyInfo = { attrs, body ->
    def company = Company.findById(session.company.toLong())
    out << "${company.toString()}"
  }

  def selectedCompany = { attrs,body ->
    def user = springSecurityService.currentUser
    def companies = organizationService.findAllCompaniesOfUser(user)
    out << g.select(from:companies, id:"companyNavSelect", name:"company",optionKey:"id", value:"${session.company}",required:"required")
  }

  def isAvailableForOperationInThisCompany = { attrs, body ->
    def company = Company.findById(session.company.toLong())
    out << (company.status == CompanyStatus.ACCEPTED)
  }

  def listTemplatesPdfForCompany = {
    def emisor = restService.getAllPdfTemplates()
    if (emisor.templatesPdf?.size()>1) {
      out << """
          <select name="pdfTemplate" class="form-control" required="required">
            <option value=""> Seleccione la plantilla PDF...</option>
        """
        emisor.templatesPdf.each { template ->
          out << "<option value=\"${template}\">${template}</option>"
        }
      out << """
          </select>
      """
    }
  }

}
