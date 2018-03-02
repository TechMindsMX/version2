package com.modulus.uno

class BusinessEntityTagLib {
  static namespace = "my"
  static defaultEncodeAs = [taglib:'html']

  def clientService
  def providerService
  def employeeService

  def whatIsThisBusinessEntity = { attrs ->
    String type = ""
    type = clientService.isClientOfThisCompany(attrs.be, attrs.company) ? "Cliente" : ""
    type += providerService.isProviderOfThisCompany(attrs.be, attrs.company) ? (type ? " - Proveedor" : "Proveedor") : ""
    type += employeeService.isEmployeeOfThisCompany(attrs.be, attrs.company) ? (type ? " - Emp/Colaborador" : "Emp/Colaborador") : ""
    out <<  type
  }

}
