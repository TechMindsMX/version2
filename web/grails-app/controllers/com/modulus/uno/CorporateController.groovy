package com.modulus.uno

import grails.transaction.Transactional
import grails.converters.JSON
import com.modulus.uno.businessEntity.BusinessEntitiesGroup
import com.modulus.uno.businessEntity.BusinessEntitiesGroupService
import com.modulus.uno.menu.MenuOperationsService
import com.modulus.uno.menu.Menu

class CorporateController {

  CorporateService corporateService
  CompanyService companyService
  UserService userService
  OrganizationService organizationService
  def springSecurityService
  def managerApplicationService
  def recoveryService
  CommissionTransactionService commissionTransactionService
  CollaboratorService collaboratorService
  BusinessEntitiesGroupService businessEntitiesGroupService
  MenuOperationsService menuOperationsService

  def create(){
    respond new Corporate()
  }

  def save(CorporateCommand corporateCommand){
    if(!corporateCommand){
      transactionStatus.setRollbackOnly()
      respond corporate.errors, view:'create'
      return
    }
    Corporate corporate = corporateCommand.getCorporate()
    if (corporateService.createRoute53(corporate)) {
      flash.error = "La ruta que se desea dar de alta ya existe"
      respond corporate.errors, view:'create'
      return
    }
    corporateService.saveNewCorporate(corporate)
    corporateService.createAVirtualHostNginx(corporate)
    log.info "sudo service nginx reload".execute().text
    request.withFormat {
      form multipartForm {
        flash.message = message(code:'default.created.message', args:[message(code: 'corporate.label',
                                                                              default: 'Corporate'),corporate.id])
        redirect corporate
      }
      '*'{ respond corporate, [status:CREATED] }
    }
  }

  def show(Corporate corporate){
    if(!corporate)
      return response.sendError(404)

    ArrayList<User> users = corporateService.findCorporateUsers(corporate.id)

    respond corporate,model:[users:users]
  }

  def assignRolesInCompaniesForUser(User user){
    Corporate corporate = corporateService.findCorporateOfUser(user)
    def roles = corporateService.getRolesForCorporate(corporate)
    List<UserRoleCompany> rolesOfUser = organizationService.findRolesForUserInCompanies(user.username,corporate)
    List companiesGroups = businessEntitiesGroupService.checkGroupsForCompanies(corporate.companies.toList())
    [companies:companiesGroups, roles:roles, user:user, rolesOfUser:rolesOfUser]
  }

  def saveRolesForUser(RolesCompanyCommand command){
    User user = organizationService.updateRolesForUserInCompanies(command.username,command.rolesByCompany())
    Corporate corporate = session.corporate
    flash.message = message(code:'users.roles.updated',default: 'Usuario actualizado')
    redirect(action:"assignRolesInCompaniesForUser",id:user.id)
  }

  def addCompany(Corporate corporate){
    if(!corporate)
      return response.sendError(404)

    render view:"newCompany",model:[company:new Company()]
  }

  def addUser(Corporate corporate){
    if(!corporate)
      return response.sendError(404)
    String conditionsAndTerms = managerApplicationService.getConditionsAndTerms()
    render view:"newUser",model:[user:new UserCommand(), corporateId:corporate.id, conditionsAndTerms:conditionsAndTerms]
  }

  def saveUser(UserCommand userCommand){
    Long corporateId = params.long("corporate")
    if(userCommand.hasErrors()){
      render(view:"/corporate/newUser",model:[user:userCommand,
                                              corporateId:corporateId])
      return
    }

    Profile profile = userCommand.getProfile()
    Telephone telephone = userCommand.getTelephone()

    if(telephone)
      profile.addToTelephones(telephone)

    User user =  userService.createUserWithoutRole(new User(username:userCommand.username,
                                                            password:userCommand.password),profile)

    ArrayList<Role> roles = springSecurityService.getPrincipal().getAuthorities()
    corporateService.addUserToCorporate(corporateId,user)
    recoveryService.sendConfirmationAccountToken(user)

    if(roles[0].authority == "ROLE_M1"){
      userService.setAuthorityToUser(user,'ROLE_CORPORATIVE')
      redirect(action:"show",id:corporateId)
      return
    }
    else{
      redirect(action:"users",id:corporateId)
      return
    }
  }

  def saveCompany(Company company){
    if(!company)
      return response.sendError(404)

    if(company.hasErrors()){
      respond company.errors, view:"/corporate/newCompany"
      return
    }

    company.status = CompanyStatus.ACCEPTED
    Corporate corporate = session.corporate
    User user = springSecurityService.currentUser

    companyService.saveInsideAndAssingCorporate(company,corporate.id)
    managerApplicationService.acceptingCompanyToIntegrate(company.id, user.profile.email)
    //createCommissionsToCompany(company)
    redirect(controller:"dashboard", action:"index")
  }

  def users(Corporate corporate){
    if(!corporate)
      return response.sendError(404)

    [users:corporateService.findCorporateUsers(corporate.id)]
  }

  def companies(Corporate corporate){
    def companiesForValidation = companyService.findCompaniesByCorporateAndStatus(CompanyStatus.VALIDATE,corporate.id)
    def companiesRejected = companyService.findCompaniesByCorporateAndStatus(CompanyStatus.REJECTED,corporate.id)
    def companiesAccepted = companyService.findCompaniesByCorporateAndStatus(CompanyStatus.ACCEPTED,corporate.id)

    [companiesForValidation:companiesForValidation,
     companiesRejected:companiesRejected,
     companiesAccepted:companiesAccepted]
  }

  def createCommissionsToCompany(Company company) {
    def commissionDeposito = new Commission(fee:new BigDecimal("0"), percentage: new BigDecimal(0), type: CommissionType.DEPOSITO, company: company).save()
    def commissionFactura = new Commission(fee:new BigDecimal("0"), percentage: new BigDecimal(0), type: CommissionType.FACTURA, company: company).save()
    def commissionPrestamo = new Commission(fee:new BigDecimal("0"), percentage: new BigDecimal(0), type: CommissionType.PRESTAMO, company: company).save()
  }

  def commissions(Corporate corporate) {
    Period period = collaboratorService.getCurrentMonthPeriod()
    List companies = corporate.companies.sort{it.bussinessName}
    List totalPendingCommissions = getTotalPendingCommissionsForCorporate(corporate, period)
    [corporate:corporate, companies:companies, totalPendingCommissions:totalPendingCommissions]
  }

  private List getTotalPendingCommissionsForCorporate(Corporate corporate, Period period) {
    List totalPendingCommissions = []
    corporate.companies.each {
      totalPendingCommissions << [company:it, total:commissionTransactionService.getTotalCommissionsPendingForCompany(it, period) ?: 0]
    }
    totalPendingCommissions
  }

  def defineCostCenters(Corporate corporate) {
    [corporate:corporate, companies:corporate.companies.sort{it.bussinessName}]
  }

  @Transactional
  def saveAliasStp() {
    Corporate corporate = Corporate.get(params.corporateId)
    companyService.assignAliasStpToCompany(Company.get(params.company), params.aliasStp)
    redirect action:'defineCostCenters', id:corporate.id
  }

  def enableOrDisableCorporate(Corporate corporate){
    def corporateToEnableOrDisable = corporateService.getCorporateToEnableOrDisable(corporate)
    redirect(controller:"Dashboard", action:"index")
  }

  def editUser(User user) {
    render view:"editUser", model:[user:user, corporateId:params.corporateId]
  }

  @Transactional
  def updateUser(User user) {
    if (user.hasErrors()) {
      log.error "Error updating user ${user.id}"
      render view:"editUser", model:[user:user, corporateId:params.corporateId]
      return
    }

    user.save()
    log.info "User ${user.username} was updated"

    redirect action:'show', id:params.corporateId.toLong()
  }

  @Transactional
  def changeStatusUser(User user) {
    user.enabled = !user.enabled
    user.save()
    if (session.corporate) {
      redirect action:'users', id:session.corporate.id
      return
    } else {
      redirect action:'show', id:params.corporateId.toLong()
      return
    }
  }

  @Transactional
  def updateFlagQuotation(Corporate corporate) {
    corporate.hasQuotationContract = !corporate.hasQuotationContract
    corporate.save()
    corporateService.unassignRolesForQuotationServiceToUsersInCorporate(corporate)
    redirect action:"show", id:corporate.id
  }

  def assignBusinessEntitiesGroup(User user) {
    Corporate corporate = Corporate.get(session.corporate.id)
    Company company = Company.get(params.companyId)
    List<BusinessEntitiesGroup> companyGroups = businessEntitiesGroupService.getAvailableGroupsForCompanyAndUser(company, user)  
    [companyGroups:companyGroups, user:user, company:company, corporate:corporate]
  }

  @Transactional
  def addBusinessEntitiesGroupToUser(User user) {
    BusinessEntitiesGroup group = BusinessEntitiesGroup.get(params.businessEntitiesGroupId)
    businessEntitiesGroupService.addBusinessEntitiesGroupToUser(user, group)
    redirect action:"assignBusinessEntitiesGroup", id:user.id, params:[companyId:group.company.id]
  }

  @Transactional
  def deleteBusinessEntitiesGroupFromUser(User user) {
    BusinessEntitiesGroup group = BusinessEntitiesGroup.get(params.groupId)
    businessEntitiesGroupService.deleteBusinessEntitiesGroupFromUser(user, group)
    redirect action:"assignBusinessEntitiesGroup", id:user.id, params:[companyId:group.company.id]
  }

  def setUpMenusForUser(User user) {
    Corporate corporate = Corporate.get(session.corporate.id)
    Company company = Company.get(params.companyId)
    List<UserRoleCompany> rolesOfUser = organizationService.findRolesForUserInCompanies(user.username,corporate)
    def companyRolesForUser = rolesOfUser.find { allUserRoles ->
      allUserRoles.company == company
    }?.roles
    [companyRolesForUser:companyRolesForUser, user:user, company:company, corporate:corporate]   
  }

  def getMenusForRole() {
    Role role = Role.get(params.role)
    def listMenusOfRole = menuOperationsService.getMenusForTheseRoles([role]) 
    def listMenus = []
    listMenusOfRole.each { item ->
      Map menu = [:]
      menu.id = item.id
      menu.name = item.name
      menu.menus = []
      item.menus.each { subItem ->
        Map submenu = [:]
        submenu.id = subItem.id
        submenu.name = subItem.name
        menu.menus.add(submenu)
      }
      listMenus.add(menu)
    }
    render listMenus as JSON
  }
  
}

@groovy.transform.TypeChecked
class RolesCompanyCommand {
  String username
  Map<String, Map<String, Boolean>> companies

  Map filterDataInCommand(){
    companies.findAll { k,v -> !k.contains(".") }
  }

  Map rolesByCompany(){
    def rolesByCompany = [:]
    def fullData = filterDataInCommand()
    fullData.each { company, roles ->
      rolesByCompany["$company"] = roles.findAll { String k,v -> !k.startsWith("_") }
    }
    rolesByCompany
  }
}
