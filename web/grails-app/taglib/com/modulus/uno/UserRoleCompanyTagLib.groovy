package com.modulus.uno

class UserRoleCompanyTagLib {
  static defaultEncodeAs = [taglib:'raw']
  static namespace = "modulusuno"
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

  def checkboxForRoleAtCompany = { attrs, body ->
    Company company = attrs.company
    Role currentRole = attrs.role
    def rolesForUserAtThisCompany = attrs.rolesOfUser.find { rolesAtCompany ->
      rolesAtCompany.company == company
    }?.roles
    Boolean checked = rolesForUserAtThisCompany?.contains currentRole ?: false
    out << g.checkBox(
      name:"companies.${company.uuid}.${currentRole.authority}",
      value:"${checked ? 'on' : ''}",
      checked:"${checked ? 'checked' : ''}")
  }
}
