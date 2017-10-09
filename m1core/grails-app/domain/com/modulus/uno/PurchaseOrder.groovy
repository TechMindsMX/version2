package com.modulus.uno

import grails.converters.JSON
import com.modulus.uno.machine.Machinery
import java.math.RoundingMode

class PurchaseOrder implements Machinery {

  String uuid = UUID.randomUUID().toString().replace('-','')[0..15]
  String providerName
  PurchaseOrderStatus status = PurchaseOrderStatus.CREADA

  Date dateCreated
  Date lastUpdated
  Boolean isAnticipated = false
  // TODO: No sé si esto sea la solución o tenemos que crear otra clase de dominio
  Boolean isMoneyBackOrder = false
  BankAccount bankAccount
  RejectReason rejectReason
  String rejectComment

  static belongsTo = [company:Company]
  static hasMany = [documents:S3Asset, items:PurchaseOrderItem, authorizations:Authorization, payments:PaymentToPurchase]

  Date fechaPago
  String externalId
  Date originalDate

  String note

  static constraints = {
    providerName blank:false, size:1..300
    bankAccount nullable:true
    rejectReason nullable:true
    rejectComment nullable:true, blank:true
    fechaPago nullable:false
    externalId nullable:true
    originalDate nullable:true
    note nullable:true, size:1..300
  }

  def getSubtotal(){
    items*.amountWithoutTaxes.sum() ?: 0
  }

  def getTaxes(){
    getSubtotal() * 0.16
  }

  BigDecimal getTotalIVA(){
    items*.appliedIVA.sum() ?: 0
  }

  BigDecimal getTotalIEPS() {
    items*.appliedIEPS.sum() ?: 0
  }

  BigDecimal getTotal(){
    getSubtotal() + getTotalIVA() + getTotalIEPS()
  }

  BigDecimal getTotalPayments() {
    payments.findAll{ it.status != PaymentToPurchaseStatus.REFUND }*.amount?.sum() ?: 0
  }

  BigDecimal getAmountToPay() {
    getTotal() - getTotalPayments()
  }

  String toString(){
    "${id} / ${providerName} / Total:\$ ${total.setScale(2, RoundingMode.HALF_UP)} / Por pagar: \$ ${amountToPay.setScale(2, RoundingMode.HALF_UP)}"
  }

  static marshaller = {
    JSON.registerObjectMarshaller(PurchaseOrder, 1) { m ->
      return [
      id: m.id,
      providerName: m.providerName,
      isAnticipated: m.isAnticipated,
      bankAccount: m.bankAccount,
      fechaPago: m.fechaPago,
      externalId: m.externalId,
      note:m.note,
      items:m.items,
      documents:m.documents,
      company: m.company
      ]
    }
  }
}
