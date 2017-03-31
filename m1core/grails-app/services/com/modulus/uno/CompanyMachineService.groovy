package com.modulus.uno

import grails.transaction.Transactional
import com.modulus.uno.machine.Machine


@Transactional
class CompanyMachineService {

  CombinationLinkService combinationLinkService

  ArrayList<Machine> getCompanyMachinesForEntity(Long companyId,String entity){
    Company company = Company.get(companyId)
    ArrayList<CombinationLink> combinationLinks = combinationLinkService.findCombinationLinksForInstance(company)
    combinationLinks = combinationLinks.findAll{ combinationLink -> combinationLink.combination.classLeft == entity && combinationLink.combination.classRight == Machine.class.simpleName }
    ArrayList<Long> machineIds = []
    combinationLinks.each{ combinationLink -> 
      machineIds << combinationLink.combinations*.rightInstanceId
    }

    Machine.getAll(machineIds)
  }

}
