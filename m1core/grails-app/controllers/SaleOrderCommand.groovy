package com.modulus.uno

import java.text.*
import grails.validation.Validateable

class SaleOrderCommand implements Validateable {

  String addressId
  String companyId
  String clientId
  String currencyUsd = "MXN"
  String changeType
  String fechaCobro
  String note
  String paymentMethod
  String externalId

  SaleOrder createSaleOrder() {
    Company company = Company.get(this.companyId)
    BusinessEntity businessEntity = BusinessEntity.get(this.clientId)
    Address address = Address.get(this.addressId)
    SaleOrder saleOrder = new SaleOrder(
      rfc:businessEntity.rfc,
      clientName: businessEntity.toString(),
      company:company,
      externalId:this.externalId,
      note:this.note,
      paymentMethod:PaymentMethod.find { it.toString() == this.paymentMethod },
      status:SaleOrderStatus.CREADA,
      fechaCobro:Date.parse("dd/MM/yyyy", this.fechaCobro),
      currency:this.currencyUsd,
      changeType:getValueInBigDecimal(this.changeType),
    )
    saleOrder.addToAddresses(address)
    saleOrder
  }

  private def getValueInBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"));
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    df.setParseBigDecimal(true);
    BigDecimal bd = (BigDecimal) df.parse(value);
    bd
  }

}
