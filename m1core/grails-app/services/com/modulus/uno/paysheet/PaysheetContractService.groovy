package com.modulus.uno.paysheet

class PaysheetContractService {
  
  def savePaysheetContract(PaysheetContract paysheetContract){
    paysheetContract.save()
    paysheetContract
  }
 
}
