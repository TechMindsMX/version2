package com.modulus.uno.saleorder

import com.modulus.uno.CurrencyType

class CreditNoteItem {

  BigDecimal quantity = new BigDecimal(1)
  BigDecimal price = new BigDecimal(0)
  BigDecimal discount = new BigDecimal(0)
  BigDecimal ivaRetention = new BigDecimal(0)
  BigDecimal iva = new BigDecimal(0)

  String sku
  String name
  String unitType
  String satKey = "84111506"

  static belongsTo = [creditNote:CreditNote]

  static constraints = {
    sku blank:false,size:4..50,matches:/^[A-Za-z0-9-]*$/
    name blank:false,size:1..300
    price min:0.0,max:250000000.00
    discount min:0.0,max:100.00
    ivaRetention min:0.0
    iva min:0.0,max:100.00
    quantity min:0.0
  }

  BigDecimal getAmountDiscount() {
    this.price * (this.discount/100)
  }

  BigDecimal getAppliedDiscount() {
    this.quantity * this.getAmountDiscount()
  }

  BigDecimal getPriceWithDiscount() {
    this.price - this.getAmountDiscount()
  }

  BigDecimal getAmountIVA(){
    this.getPriceWithDiscount() * (this.iva/100)
  }

  BigDecimal getAppliedIVA() {
    this.quantity * this.getAmountIVA()
  }

  BigDecimal getAmountWithoutTaxes(){
    this.quantity * this.getPriceWithDiscount()
  }

  BigDecimal getNetPrice(){
    this.getPriceWithDiscount() + this.getAmountIVA() - this.ivaRetention
  }

  BigDecimal getNetAmount(){
    this.quantity * this.getNetPrice()
  }

  BigDecimal getTotalIvaRetention() {
    this.quantity * this.ivaRetention
  }

}
