package com.modulus.uno

import org.springframework.context.i18n.LocaleContextHolder as LCH

import grails.transaction.Transactional

@Transactional
class ProviderService {

  def messageSource
  def emailSenderService

  def addProviderToCompany(ProviderBusinessEntity provider, Company company){
    if(isProviderOfThisCompany(provider, company))throw new BusinessException(messageSource.getMessage('exception.provider.already.exist', null, LCH.getLocale()))
    def providerLink = new ProviderLink(type:provider.class.simpleName, providerRef: provider.rfc, company: company).save()
    company.addToBusinessEntities(provider)
    emailSenderService.sendEmailForNewProvider(company, provider)
    providerLink
  }

  def isProviderOfThisCompany(ProviderBusinessEntity provider, Company company){
    ProviderLink.countByTypeAndProviderRefAndCompany(provider?.class?.simpleName,provider?.rfc,company)
  }

  def getProvidersFromCompany(Company company){
    def providers = []
    def links = ProviderLink.findAllByCompany(company)
    links.each{ link ->
      providers.add(BusinessEntity.findByRfc(link.providerRef))
    }
    providers
  }

  def isProvider(instance){
    ProviderLink.countByTypeAndProviderRef(instance.class.simpleName, instance.rfc)
  }
}
