package com.modulus.uno.quotatio

import com.modulus.uno.BusinessEntity

class QuatationContractCommand {

  BusinessEntity client
  BigDecimal commision

  BusinessEntity gatBusinessEntity(){
    new BusinessEntity(
                      client:client,
                      commision: commision,
                      initDate: new Date()
    )
  }
}
