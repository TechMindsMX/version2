package com.modulus.uno
import java.text.*
import grails.validation.Validateable

class PurchaseOrderItemCommand implements Validateable{

    String name
    String quantity
    String price
    String discount
    String iva
    String ivaRetention
    String unitType

    PurchaseOrderItem createPurchaseOrderItem() {
      new PurchaseOrderItem(
          name:this.name,
          quantity:getValueInBigDecimal(this.quantity),
          price:getValueInBigDecimal(this.price),
          discount:getValueInBigDecimal(this.discount?:"0"),
          iva:getValueInBigDecimal(this.iva?:"16"),
          ivaRetention:getValueInBigDecimal(this.ivaRetention?:"0"),
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
