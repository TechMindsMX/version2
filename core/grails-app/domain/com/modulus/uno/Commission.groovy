package com.modulus.uno

class Commission {

  BigDecimal fee = 0.00
  BigDecimal percentage = 0.00
  CommissionType type = CommissionType.FACTURA

  static belongsTo = [company:Company]

  static constraints = {
    fee nullable:true,min:0.00,max:250000000.00
    percentage nullable:true,min:0.00,max:100.00,validator: { val, obj ->
      if(obj.fee > 0 && obj.percentage > 0){
        return false
      }
    }
    type unique:"company"
  }

}

