package com.modulus.uno
import java.text.*
import grails.validation.Validateable

class PurchaseOrderItemCommand implements Validateable{

    String name
    String quantity
    String price
    String ieps
    String iva
    String unitType

    PurchaseOrderItem createPurchaseOrderItem() {
      new PurchaseOrderItem(
          name:this.name,
          quantity:getValueInBigDecimal(this.quantity),
          price:getValueInBigDecimal(this.price),
          ieps:getValueInBigDecimal(this.ieps?:"16"),
          iva:getValueInBigDecimal(this.iva?:"16"),
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
