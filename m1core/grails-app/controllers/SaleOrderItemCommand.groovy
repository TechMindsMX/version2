package com.modulus.uno
import java.text.*
import grails.validation.Validateable

class SaleOrderItemCommand implements Validateable{

  String sku
  String name
  String quantity
  String price
  String discount
  String ieps
  String iva
  String unitType

  SaleOrderItem createSaleOrderItem() {
    new SaleOrderItem(
      sku:this.sku,
      name:this.name,
      quantity:getValueInBigDecimal(this.quantity),
      price:getValueInBigDecimal(this.price),
      discount:getValueInBigDecimal(this.discount),
      ieps:getValueInBigDecimal(this.ieps?:"0"),
      iva:getValueInBigDecimal(this.iva?:"0"),
      unitType:UnitType.values().find {  it.toString() == this.unitType } ?: unitType
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
