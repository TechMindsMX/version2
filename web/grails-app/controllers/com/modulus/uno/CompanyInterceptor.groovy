package com.modulus.uno

import grails.gsp.PageRenderer

class CompanyInterceptor {

    int order = LOWEST_PRECEDENCE;

    def springSecurityService
    OrganizationService organizationService
    RoleService roleService
    PageRenderer groovyPageRenderer

    CompanyInterceptor() {
      log.info "************** Contructor Method of interceptor ******************"
      matchAll()
           .excludes(controller:"login")
           .excludes(controller:"logout")
           .excludes(controller:'company')
      match(controller:"company",action:"show")
      match(controller:"company",action:"index")

    }

    boolean before() {
      log.info "Interceptor method for a company Id in session"
      def currentUser = springSecurityService.currentUser
      if (!currentUser)
        return true
      if (session.company)
        return true
      def companies = organizationService.findAllCompaniesOfUser(currentUser)
      switch (companies.size()) {
        case 0:
          return true
          break
        case 1:
          roleService.updateTheUserRolesOfUserAtThisCompany(currentUser,companies.first())
          session["company"] = companies.first().id
          return true
          break
        default:
          render view: '/company/companiesSelect', model: [companies:companies]
          break
      }

      return true
    }

    boolean after() {
      return true
    }

    void afterView() {
        // no-op
    }
}
