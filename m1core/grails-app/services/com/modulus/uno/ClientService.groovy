package com.modulus.uno

import org.springframework.context.i18n.LocaleContextHolder as LCH

import grails.transaction.Transactional

@Transactional
class ClientService {

  def messageSource
  def emailSenderService
  def modulusUnoService
  def springSecurityService

  def addClientToCompany(ClientBusinessEntity client, Company company){
    if(isClientOfThisCompany(client, company))throw new BusinessException(messageSource.getMessage('exception.client.already.exist', null, LCH.getLocale()))
    def clientLink = new ClientLink(type:client.class.simpleName, clientRef: client.rfc, company: company).save()
    company.addToBusinessEntities(client)
    emailSenderService.sendEmailForNewClient(company, client)
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

  ClientLink generateSubAccountStp(ClientLink client, BusinessEntity businessEntity) {
    User user = springSecurityService.currentUser
    CreateAccountCommand command = new CreateAccountCommand(payerAccount:client.company.accounts.first().stpClabe, uuid:businessEntity.uuid, name:businessEntity.toString(), email:user.profile.email)
    client.stpClabe = modulusUnoService.generateSubAccountStpForClient(command)
    client.save()
    client
  }
}
