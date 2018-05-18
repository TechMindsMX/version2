package com.modulus.uno.saleorder

import java.text.*
import grails.validation.Validateable

class SaleOrderItemCommand implements Validateable{

  String sku
  String name
  String quantity
  String price
  String discount
  String ivaRetention
  String iva
  String unitType
  String satKey

  SaleOrderItem createSaleOrderItem() {
    new SaleOrderItem(
      sku:this.sku,
      name:this.name,
      quantity:getValueInBigDecimal(this.quantity),
      price:getValueInBigDecimal(this.price),
      discount:getValueInBigDecimal(this.discount),
      ivaRetention:getValueInBigDecimal(this.ivaRetention?:"0"),
      iva:getValueInBigDecimal(this.iva?:"0"),
      satKey:this.satKey ?: "01010101",
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
