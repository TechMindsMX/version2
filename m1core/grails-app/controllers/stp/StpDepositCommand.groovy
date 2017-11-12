package com.modulus.uno.stp

import grails.validation.Validateable
import java.text.*

class StpDepositCommand implements Validateable {
  String clave
  String fechaOperacion
  String institucionOrdenante
  String institucionBeneficiaria
  String claveRastreo
  String monto
  String nombreOrdenante
  String nombreBeneficiario
  String tipoCuentaBeneficiario
  String cuentaBeneficiario
  String rfcCurpBeneficiario
  String conceptoPago
  String referenciaNumerica
  String empresa

  static constraints = {
    clave blank:false
    fechaOperacion blank:false
    institucionOperante blank:false
    institucionBeneficiaria blank:false
    claveRastreo blank:false
    monto blank:false
    nombreOrdenante blank:true, nullable:true
    nombreBeneficiario blank:false
    tipoCuentaBeneficiario blank:false
    cuentaBeneficiario blank:false
    rfcCurpBeneficiario blank:false
    conceptoPago blank:false
    referenciaNumerica blank:false
    empresa blank:false
  }

  StpDeposit createStpDeposit() {
    new StpDeposit(
      operationNumber:this.clave.toLong(),
      operationDate: new SimpleDateFormat("yyyyMMdd").parse(this.fechaOperacion),
      payerKey: this.institucionOrdenante,
      beneficiaryKey: this.institucionBeneficiaria,
      tracingKey: this.claveRastreo,
      amount: getValueInBigDecimal(this.monto),
      payerName: this.nombreOrdenante,
      beneficiaryName: this.nombreBeneficiario,
      typeAccountBeneficiary: this.tipoCuentaBeneficiario.toLong(),
      accountBeneficiary: this.cuentaBeneficiario,
      rfcCurpBeneficiary: this.rfcCurpBeneficiario,
      paymentConcept: this.conceptoPago,
      numericalReference: this.referenciaNumerica.toLong(),
      companyNameStp: this.empresa
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
