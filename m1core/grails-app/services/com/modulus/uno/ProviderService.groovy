package com.modulus.uno

import org.springframework.context.i18n.LocaleContextHolder as LCH

import grails.transaction.Transactional

@Transactional
class ProviderService {

  def messageSource

  def addProviderToCompany(ProviderBusinessEntity provider, Company company){
    if(isProviderOfThisCompany(provider, company))throw new BusinessException(messageSource.getMessage('exception.provider.already.exist', null, LCH.getLocale()))
    def providerLink = new ProviderLink(type:provider.class.simpleName, providerRef: provider.rfc, company: company)
    providerLink.save()
    if (providerLink.hasErrors()){
      log.error "Error al guardar el provider ${providerLink.dump()}"
      throw new BusinessException("Los datos del cliente son erroneos")
    }
    company.addToBusinessEntities(provider)
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

  def updateProviderToCompany(BusinessEntity businessEntity, String backRfc) {
    ProviderLink providerLink = ProviderLink.findByProviderRef(backRfc)
    providerLink.providerRef = businessEntity.rfc
    providerLink.save()
    providerLink
  }

  ProviderLink providerAlreadyExistsInCompany(String rfc, Company company){
    ProviderLink.findByProviderRefAndCompany(rfc, company)
  }

  ProviderLink createProviderForRowProvider(Map rowProvider, Company company) {
    ProviderLink providerLink = new ProviderLink(
      type:"BusinessEntity",
      providerRef:rowProvider.RFC,
      company:company
    )
    providerLink.save()
    providerLink
    }   
}
