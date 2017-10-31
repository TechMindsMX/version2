package com.modulus.uno

import grails.transaction.Transactional
import grails.util.Holders as H

@Transactional
class CorporateService {

  def grailsApplication
  def springSecurityService
  def awsRoute53Service

  private final String VALUE_HOST_IP = H.grailsApplication.config.grails.plugin.awssdk.value.host.ip
  private final String DOMAIN_BASE_URL = H.grailsApplication.config.grails.plugin.awssdk.domain.base.url
  private final String FILES_NGINX = H.grailsApplication.config.grails.plugin.awssdk.files.nginx

  def addCompanyToCorporate(Corporate corporate, Company company) {
    corporate.addToCompanies(company)
    corporate.save()
    corporate
  }

  def saveNewCorporate(Corporate corporate) {
    corporate.save()
  }

  def createRoute53(Corporate corporate) {
    def valueHost = VALUE_HOST_IP
    def baseUrl = DOMAIN_BASE_URL
    def listResultSets = awsRoute53Service.getResourceRecordSet()
    if (!existRecordSetInAWS(listResultSets,corporate.corporateUrl)) {
      awsRoute53Service.createRecordSet(corporate.corporateUrl,"-api${baseUrl}", valueHost)
      awsRoute53Service.createRecordSet(corporate.corporateUrl,baseUrl, valueHost)
      return false
    } else {
      return true
    }
  }

  private existRecordSetInAWS(def listResultSets, def corporateUrl) {
    def result
    result = listResultSets.find{ set -> set.name == "${corporateUrl}${DOMAIN_BASE_URL}."}
    result = listResultSets.find{ set -> set.name == "${corporateUrl}-api${DOMAIN_BASE_URL}."}
    result? true: false
  }

  def createAVirtualHostNginx(Corporate corporate) {
    def baseUrl = DOMAIN_BASE_URL
    def tempDirectory = FILES_NGINX
    createWebAndApiViHost(corporate, baseUrl, tempDirectory)
  }

  Corporate findCorporateOfUser(User user){
    Corporate.createCriteria().get {
      users {
        eq 'username', user.username
      }
    }
  }

  User findCorporateUserOfCompany(Long companyId){
    ArrayList<User> users

    Corporate corporate = Corporate.createCriteria().get{
      companies{
        eq("id",companyId)
      }
    }

    users = corporate.users ?: []
    users = users.findAll{ user -> user.getAuthorities()*.authority.contains('ROLE_CORPORATIVE') }
    users[0]
  }

  Corporate addUserToCorporate(Long corporateId,User user){
    Corporate corporate = Corporate.get(corporateId)
    corporate.addToUsers(user)
    corporate.save()
    corporate
  }

  ArrayList<User> findCorporateUsers(Long corporateId){
    Corporate corporate = Corporate.get(corporateId)
    ArrayList<User> corporateUsers = corporate.users

    User user = springSecurityService.currentUser
    ArrayList<String> currentUserAuthorities = user.getAuthorities()*.authority

    if(currentUserAuthorities.contains("ROLE_M1"))
      corporateUsers = corporateUsers.findAll{ _user -> _user.getAuthorities()*.authority.contains("ROLE_CORPORATIVE") }
    else
      corporateUsers = corporateUsers.findAll{ _user -> ["ROLE_M1","ROLE_CORPORATIVE"].every{ !(it in _user.getAuthorities()*.authority) } }

    corporateUsers
  }

  def addCommissionToCorporate(Corporate corporate, Commission commission) {
    corporate.addToCommissions(commission)
    corporate.save()
    corporate
  }

  String findUrlCorporateOfUser(User user){
    Corporate corporate = Corporate.createCriteria().get {
      users {
        eq 'username', user.username
      }
    }
    corporate ? "${corporate.corporateUrl}${DOMAIN_BASE_URL}" : "web${DOMAIN_BASE_URL}"
  }

  String findCorporateByCompanyId(def companyId) {
    def corporateList = Corporate.withCriteria {
      companies {
        eq("id", companyId)
      }
    }
    "${corporateList.first().corporateUrl}${DOMAIN_BASE_URL}"
  }

  def findLegalRepresentativesOfCompany(Long companyId){
    Corporate corporate = Corporate.createCriteria().get{
      companies{
        eq("id",companyId)
      }
    }

    def users = corporate.users ?: []
    users.findAll{ user -> user.getAuthorities()*.authority.contains('ROLE_LEGAL_REPRESENTATIVE_EJECUTOR') ||
      user.getAuthorities()*.authority.contains('ROLE_LEGAL_REPRESENTATIVE_VISOR')
    }
  }

  private def copyAndReplaceTextInFile(source, dest, Closure replaceText) {
    dest.write(replaceText(source.text))
  }

  private def createWebAndApiViHost(Corporate corporate, String baseUrl, String destFile) {
    def file = corporate.getClass().getClassLoader().getResource("web.txt").file
    def fileDefaultWeb =  new File(corporate.getClass().getClassLoader().getResource("web.txt").file)
    def fileDefaultApi =  new File(corporate.getClass().getClassLoader().getResource("api.txt").file)
    def fileNewWeb = new File("${destFile}${corporate.corporateUrl}.conf")
    def fileNewApi = new File("${destFile}api-${corporate.corporateUrl}.conf")
    copyAndReplaceTextInFile(fileDefaultWeb,fileNewWeb) { it.replaceAll('urlCorporate',"${corporate.corporateUrl}${baseUrl}" )}
    copyAndReplaceTextInFile(fileDefaultApi,fileNewApi) { it.replaceAll('urlCorporate',"${corporate.corporateUrl}-api${baseUrl}" )}
  }

  Corporate getCorporateFromCompany(Long idCompany) {
    def c = Corporate.createCriteria()
    def corporate = c.get {
      companies {
        eq("id", idCompany)
      }
    }
    corporate
  }

}
