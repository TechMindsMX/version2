package com.modulus.uno.businessEntity

import grails.validation.Validateable
import com.modulus.uno.Company

class BusinessEntitiesGroupCommand implements Validateable {

  String description
  String type
  String companyId

  BusinessEntitiesGroup createBusinessEntitiesGroup() {
    new BusinessEntitiesGroup(
      description: this.description,
      type: BusinessEntitiesGroupType.values().find { it.toString() == this.type },
      company: Company.get(this.companyId)
    )
  }
}
