package com.modulus.uno

import grails.transaction.Transactional
import grails.util.Holders as H
import com.modulus.uno.menu.Menu
import com.modulus.uno.menu.MenuOperationsService

@Transactional
class CorporateService {

  def grailsApplication
  def springSecurityService
  def awsRoute53Service
  def userRoleService
  MenuOperationsService menuOperationsService

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

  ArrayList getRolesForCorporate(Corporate corporate){
    List<Role> roles = []
    roles = Role.list().findAll{ it.authority.toString() in ["ROLE_LEGAL_REPRESENTATIVE_VISOR",
                        "ROLE_LEGAL_REPRESENTATIVE_EJECUTOR",
                        "ROLE_FICO_VISOR",
                        "ROLE_FICO_EJECUTOR",
                        "ROLE_AUTHORIZER_VISOR",
                        "ROLE_AUTHORIZER_EJECUTOR",
                        "ROLE_OPERATOR_VISOR",
                        "ROLE_OPERATOR_EJECUTOR",
                        "ROLE_AUTHORIZER_PAYSHEET",
                        "ROLE_OPERATOR_PAYSHEET",
                        "ROLE_EMPLOYEE"
                        ]}
    log.info "Has Quotation Contract: ${corporate.hasQuotationContract}"
    if(corporate.hasQuotationContract){
      roles.addAll(Role.list().findAll{ it.authority.toString() in [
                            "ROLE_OPERATOR_QUOTATION",
                            "ROLE_EXECUTOR_QUOTATION"
                            ]})
    }
    roles 
  }

  def unassignRolesForQuotationServiceToUsersInCorporate(Corporate corporate) {
    log.info "HasQuotationContract flag: ${corporate.hasQuotationContract}"
    if (!corporate.hasQuotationContract) {
      corporate.users.each { user ->
        log.info "Delete roles quotation service for user: ${user}"
        userRoleService.deleteRoleForUser(user, Role.findByAuthority("ROLE_OPERATOR_QUOTATION"))      
        userRoleService.deleteRoleForUser(user, Role.findByAuthority("ROLE_EXECUTOR_QUOTATION")) 
      }
    }
    corporate
  }

  def getCorporateToEnableOrDisable(Corporate corporate) {
    if(corporate.status == CorporateStatus.ENABLED){
      corporate.users.each{ status ->
        status.enabled = false }
      return corporate.status = CorporateStatus.DISABLED
    }
    else {
      corporate.users.each{ status ->
        status.enabled = true }
      return corporate.status = CorporateStatus.ENABLED 
    }
  }

  def saveUserMenus(User user, Map params) {
    def menusIdsSelected = []
    params.each { key, value ->
      if (key.startsWith("menuId")) {
        menusIdsSelected.add(value)
      }
    }

    def listMenus = Menu.getAll(menusIdsSelected)
    Company company = Company.get(params.companyId)
    Role role = Role.get(params.roleId)
    UserRoleCompanyMenu userMenus = UserRoleCompanyMenu.findByUserAndRoleAndCompany(user, role, company)
    if (userMenus) {
      userMenus.menus.clear()
      userMenus.save()
      userMenus.menus = listMenus
    } else {
      userMenus = new UserRoleCompanyMenu(
        user: user,
        company: company,
        role: role,
        menus: listMenus
      )
    }
    userMenus.save()
  } 

  def getMenusForRole(Map userRoleCompany) {
    Role role = Role.get(userRoleCompany.role)
    Company company = Company.get(userRoleCompany.company)
    User user = User.get(userRoleCompany.user)
    def listMenusOfRole = menuOperationsService.getMenusForTheseRoles([role]) 
    def listMenus = []
    listMenusOfRole.each { item ->
      Map menu = [:]
      menu.id = item.id
      menu.name = item.name
      menu.menus = []
      menu.checked = menuIsAssignedToUserInCompany([user:user, company:company, role:role, menu:item])
      item.menus.each { subItem ->
        Map submenu = [:]
        submenu.id = subItem.id
        submenu.name = subItem.name
        submenu.checked = menuIsAssignedToUserInCompany([user:user, company:company, role:role, menu:subItem])
        menu.menus.add(submenu)
      }
      listMenus.add(menu)
    }
    listMenus
  }

  List<Menu> getUserRoleCompanyMenus(Map userRoleCompanyData) {
    UserRoleCompanyMenu.findByUserAndRoleAndCompany(userRoleCompanyData.user, userRoleCompanyData.role, userRoleCompanyData.company)?.menus?.toList()
  }

  boolean menuIsAssignedToUserInCompany(Map userRoleCompanyMenu) {
    List<Menu> userMenus = getUserRoleCompanyMenus(userRoleCompanyMenu)
    userMenus?.contains(userRoleCompanyMenu.menu)
  }
}
