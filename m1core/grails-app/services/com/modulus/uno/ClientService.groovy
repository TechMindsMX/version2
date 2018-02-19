package com.modulus.uno

import org.springframework.context.i18n.LocaleContextHolder as LCH

import grails.transaction.Transactional

@Transactional
class ClientService {

  def messageSource
  def stpClabeService
  def springSecurityService

  def addClientToCompany(ClientBusinessEntity client, Company company){
    if(isClientOfThisCompany(client, company))throw new BusinessException(messageSource.getMessage('exception.client.already.exist', null, LCH.getLocale()))
    def clientLink = new ClientLink(type:client.class.simpleName, clientRef: client.rfc, company: company)
    clientLink.save()
    if (clientLink.hasErrors()){
      log.error "Error al guardar el client ${clientLink.dump()}"
      throw new BusinessException("Los datos del cliente son erroneos")
    }

    company.addToBusinessEntities(client)
    clientLink
  }

  def isClientOfThisCompany(ClientBusinessEntity client, Company company){
    ClientLink.countByTypeAndClientRefAndCompany(client?.class?.simpleName,client?.rfc,company)
  }

  def getClientsFromCompany(Company company){
    def clients = []
    def links = ClientLink.findAllByCompany(company)
    links.each{ link ->
      clients.add(BusinessEntity.findByRfc(link.clientRef))
    }
    clients
  }

  def isClient(instance){
    ClientLink.countByTypeAndClientRef(instance.class.simpleName, instance.rfc)
  }

  ClientLink generateSubAccountStp(ClientLink client) {
    client.stpClabe = stpClabeService.generateSTPSubAccount(client.company.accounts.first().stpClabe, 4)
    client.save()
    client
  }

  def updateClientToCompany(BusinessEntity businessEntity, String backRfc) {
    ClientLink clientLink = ClientLink.findByClientRef(backRfc)
    clientLink.clientRef = businessEntity.rfc
    clientLink.save()
    clientLink
  }

  ClientLink clientAlreadyExistsInCompany(String rfc, Company company){
    ClientLink.findByClientRefAndCompany(rfc, company)
  }

  ClientLink createClientForRowClient(Map rowClient, Company company) {
    ClientLink clientLink = new ClientLink(
      type:"BusinessEntity",
      clientRef:rowClient.RFC,
      company:company
    )
    clientLink.save()
    clientLink
  }

  def deleteClientLinkForRfcAndCompany(String rfc, Company company) {
    ClientLink clientLink = ClientLink.findByClientRefAndCompany(rfc, company)
    clientLink?.delete()
  }

}
