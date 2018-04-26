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
  String paymentWay
  String externalId
  String paymentMethod
  String invoicePurpose

  static constraints = {
    currencyUsd blank:false, size:3..3, validator: { val -> val in ["MXN","USD"] }
    changeType nullable:true
    externalId nullable:true
    note nullable:true
  }

  SaleOrder createOrUpdateSaleOrder() {
    Company company = Company.get(this.companyId)
    BusinessEntity businessEntity = BusinessEntity.get(this.clientId)
    Address address = Address.get(this.addressId)

    SaleOrder saleOrder
    if (this.externalId) {
      saleOrder = SaleOrder.findByCompanyAndExternalIdAndStatusInList(company, this.externalId, [SaleOrderStatus.CREADA, SaleOrderStatus.POR_AUTORIZAR])
    }

    PaymentWay paymentWay
    if (this.paymentWay.isNumber()) {
      paymentWay = PaymentWay.find { it.ordinal().toString() == this.paymentWay }
    } else {
      paymentWay = PaymentWay.find { it.toString() == this.paymentWay }
    }

    PaymentMethod paymentMethod
    if (this.paymentMethod.isNumber()) {
      paymentMethod = PaymentMethod.find { it.ordinal().toString() == this.paymentMethod }
    } else {
      paymentMethod = PaymentMethod.find { it.toString() == this.paymentMethod }
    }

    InvoicePurpose invoicePurpose
    if (this.invoicePurpose.isNumber()) {
      invoicePurpose = InvoicePurpose.find { it.ordinal().toString() == this.invoicePurpose }
    } else {
      invoicePurpose = InvoicePurpose.find { it.toString() == this.invoicePurpose }
    }

    if (saleOrder) {
       saleOrder.rfc = businessEntity.rfc
       saleOrder.clientName =  businessEntity.toString()
       saleOrder.note = this.note
       saleOrder.paymentWay = paymentWay
       saleOrder.fechaCobro = Date.parse("dd/MM/yyyy", this.fechaCobro)
       saleOrder.currency = this.currencyUsd
       saleOrder.changeType = getValueInBigDecimal(this.changeType ?: "1")
       saleOrder.paymentMethod = paymentMethod
       saleOrder.invoicePurpose = invoicePurpose
    } else {
      saleOrder = new SaleOrder(
        rfc:businessEntity.rfc,
        clientName: businessEntity.toString(),
        company:company,
        externalId:this.externalId,
        note:this.note,
        paymentWay:paymentWay,
        status:SaleOrderStatus.CREADA,
        fechaCobro:Date.parse("dd/MM/yyyy", this.fechaCobro),
        currency:this.currencyUsd,
        changeType:getValueInBigDecimal(this.changeType ?: "1"),
        paymentMethod:paymentMethod,
        invoicePurpose:invoicePurpose
      )
      saleOrder.addToAddresses(address)
    }
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
