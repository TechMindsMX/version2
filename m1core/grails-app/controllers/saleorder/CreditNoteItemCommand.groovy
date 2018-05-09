package com.modulus.uno.saleorder

import java.text.*
import grails.validation.Validateable

class CreditNoteItemCommand implements Validateable{

  String quantity
  String price
  String discount
  String ivaRetention
  String iva
  String sku
  String name
  String unitType

  CreditNoteItem createCreditNoteItem() {
    new CreditNoteItem(
      sku:this.sku,
      name:this.name,
      quantity:getValueInBigDecimal(this.quantity),
      price:getValueInBigDecimal(this.price),
      discount:getValueInBigDecimal(this.discount),
      ivaRetention:getValueInBigDecimal(this.ivaRetention?:"0"),
      iva:getValueInBigDecimal(this.iva?:"0"),
      unitType:this.unitType
    )
  }

  private def getValueInBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"));
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    df.setParseBigDecimal(true);
    BigDecimal bd = (BigDecimal) df.parse(value);
    bd
  }

}
