package com.modulus.uno.credit

import com.modulus.uno.Company

class Credit {
  String name

  ProductType productType
  PortfolioManagementType portfolioManagementType
  FrequencyType frequencyType
  CreditLineType creditLineType
  DispersionType dispersionType
  Boolean enabled

  static belongsTo = [company: Company]

  static constraints = {
    name blank:false, nullable:false
  }

  // static marshaller = {
  //   JSON.registerObjectMarshaller(Address, 1) { m ->
  //     return [
  //     id: m.id,
  //     street: m.street,
  //     streetNumber: m.streetNumber,
  //     suite: m.suite,
  //     zipCode: m.zipCode,
  //     colony: m.colony,
  //     neighboorhood: m.neighboorhood,
  //     country: m.country,
  //     city: m.city,
  //     town: m.town,
  //     federalEntity: m.federalEntity,
  //     addressType: m.addressType
  //     ]
  //   }
  // }
}
