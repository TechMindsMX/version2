package com.modulus.uno
import java.text.*
import grails.validation.Validateable

class MovimientosBancariosCommand implements Validateable {

  String concept
  String reference
  String amount
  String dateEvent
  String type
  String debito
  String credito

  MovimientosBancarios createObject() {
    new MovimientosBancarios(
      concept:this.concept,
      reference:this.reference ?: "",
      amount: getValueInBigDecimal(this.amount),
      type:MovimientoBancarioType."${this.type}",
      dateEvent: Date.parse('dd/MM/yyyy',this.dateEvent)
    )
  }

  MovimientosBancarios createObjectByRow() {
    new MovimientosBancarios(
      concept:this.concept,
      reference:this.reference ?: "",
      amount: getValueInBigDecimal(this.debito != "0" ? this.debito: this.credito),
      type:obtainTypeMovimiento(),
      dateEvent: Date.parse('dd/MM/yyyy',this.dateEvent)
    )
  }

  private def obtainTypeMovimiento() {
    if (this.debito != "0" )
      return MovimientoBancarioType.DEBITO
    else
      return MovimientoBancarioType.CREDITO
  }

  private def getValueInBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"));
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    df.setParseBigDecimal(true);
    BigDecimal bd = (BigDecimal) df.parse(value);
    bd
  }

  static constraints = {
    concept blank:false, nullable:false
    reference blank:true, nullable:true
    dateEvent blank:false, nullable:false
    debito blank:true, nullable:true, validator: { val, obj ->
      if ( (val == "0" && obj.credito != "0") || (val != "0" && obj.credito == "0") )
        true
    }
    credito blank:true, nullable: true, validator: { val, obj ->
      if ( (val == "0" && obj.debito != "0") || (val != "0" && obj.debito == "0") )
        true
    }
    type blank:true,nullable: true
  }

}
