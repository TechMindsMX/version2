package com.modulus.uno

import com.modulus.uno.saleorder.SaleOrder

class CashFlow {
  BigDecimal totalCharges
  BigDecimal totalPayments
  List<SaleOrder> listCharges
  List<PurchaseOrder> listPayments
  Date startDate
  Date endDate
}
