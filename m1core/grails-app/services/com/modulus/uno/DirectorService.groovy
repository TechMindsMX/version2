package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class DirectorService {

  ArrayList<User> findUsersOfCompany(Long companyId){
    def criteria = UserRoleCompany.createCriteria()
    ArrayList<UserRoleCompany> userRoleCompanies = criteria.list {
      company{
        eq("id",companyId)
      }
    }

    userRoleCompanies*.user
  }

  ArrayList<User> findUsersOfCompanyByRole(Long companyId,ArrayList<String> authorities){
    def criteria = UserRoleCompany.createCriteria()
    ArrayList<UserRoleCompany> userRoleCompanies = criteria.list{
      company{
        eq("id",companyId)
      }

      roles{
        'in'("authority",authorities)
      }
    }

    userRoleCompanies*.user
  }

}
