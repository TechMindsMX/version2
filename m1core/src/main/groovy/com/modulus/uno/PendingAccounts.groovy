package com.modulus.uno

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
