package com.modulus.uno
import java.text.*

import grails.validation.Validateable

class CashOutOrderCommand implements Validateable{

  String id
  String amount
  BankAccount account


  CashOutOrder createCashOutOrder(){
    new CashOutOrder(
      amount:getValueInBigDecimal(this.amount),
      account:this.account,
    )
  }

  private def getValueInBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"));
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    df.setParseBigDecimal(true);
    BigDecimal bd = (BigDecimal) df.parse(value);
    bd
  }

  CashOutOrder editCashOutOrder(Long id){
    CashOutOrder cashoutOrder = CashOutOrder.get(id)
    cashoutOrder.amount = getValueInBigDecimal(this.amount)
    cashoutOrder.account = this.account
    cashoutOrder
  }

}
