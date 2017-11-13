package com.modulus.uno

import com.modulus.uno.stp.StpDeposit

class StpDepositSwagger{

  String clave
  String fechaOperacion
  String claveInstitucionOrdenante
  String claveInstitucionBeneficiaria
  String claveRastreo
  String monto
  String nombreOrdenante
  String nombreBeneficiario
  String claveTipoCuentaBeneficiario
  String cuentaBeneficiario
  String rfcCurpBeneficiario
  String conceptoPago
  String referenciaNumerica
  String empresa

  StpDeposit createStpDeposit() {
    StpDeposit stpDeposit = new StpDeposit(
      operationNumber:this.clave?.toLong(),
      operationDate: this.fechaOperacion ? new Date().parse("yyyyMMdd", this.fechaOperacion) : null,
      payerKey: this.claveInstitucionOrdenante,
      beneficiaryKey: this.claveInstitucionBeneficiaria,
      tracingKey: this.claveRastreo,
      amount: this.monto ? new BigDecimal(this.monto) : null,
      payerName: this.nombreOrdenante,
      beneficiaryName: this.nombreBeneficiario,
      typeAccountBeneficiary: this.claveTipoCuentaBeneficiario?.toLong(),
      accountBeneficiary: this.cuentaBeneficiario,
      rfcCurpBeneficiary: this.rfcCurpBeneficiario,
      paymentConcept: this.conceptoPago,
      numericalReference: this.referenciaNumerica?.toLong(),
      companyNameStp: this.empresa
    )
    if(!stpDeposit.validate())
      throw new BusinessException("Verifica los datos de envio")
    stpDeposit
  }

}
