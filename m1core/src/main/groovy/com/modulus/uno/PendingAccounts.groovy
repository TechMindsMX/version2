package com.modulus.uno

import com.modulus.uno.saleorder.SaleOrder

class PendingAccounts {
  BigDecimal totalCharges
  BigDecimal totalPayments
  BigDecimal totalExpiredCharges
  BigDecimal totalExpiredPayments

  List<SaleOrder> listCharges
  List<PurchaseOrder> listPayments
  List<SaleOrder> listExpiredCharges
  List<PurchaseOrder> listExpiredPayments

  Date startDate
  Date endDate
}
