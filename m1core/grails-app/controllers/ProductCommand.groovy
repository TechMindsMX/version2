package com.modulus.uno
import java.text.*
import grails.validation.Validateable

class ProductCommand implements Validateable{

  String id
  String satKey
  String sku
  String name
  String price
  String ieps
  String iva
  UnitType unitType
  CurrencyType currencyType


  Product createProduct(){
    new Product(
      satKey:this.satKey,
      sku:this.sku,
      name:this.name,
      price:getValueInBigDecimal(this.price),
      ieps:getValueInBigDecimal(this.ieps),
      iva:getValueInBigDecimal(this.iva),
      unitType:this.unitType,
      currencyType:this.currencyType
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
