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

  def listTemplatesPdfForCompany = { attrs, body ->
    def emisor = restService.existEmisorForGenerateInvoice(attrs.rfc, attrs.id)
    if (emisor.templatesPdf?.size()>1) {
      out << """
        <div class="row">
          <label>Formato PDF:*</label>
          <select name="pdfTemplate" class="form-control" required="required">
            <option value="">- Seleccione un formato...</option>
        """
        emisor.templatesPdf.each { it ->
          out << "<option value=\"${it}\">${it}</option>"
        }
      out << """
          </select>
        </div><br/>
      """
    }
  }

}
