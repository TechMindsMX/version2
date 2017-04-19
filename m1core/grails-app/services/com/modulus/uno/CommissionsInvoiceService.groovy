package com.modulus.uno

class CommissionsInvoiceService {

  def commissionTransactionService

  CommissionsInvoice createCommissionsInvoiceForCompany(Company company) {
    List<CommissionTransaction> pendingCommissions = CommissionTransaction.findAllByCompanyAndStatus(company, CommissionTransactionStatus.PENDING)
    CommissionsInvoice commissionsInvoice = new CommissionsInvoice(
      receiver:company,
      commissions:pendingCommissions
    )
    commissionsInvoice.save()
    updateCommissionTransactionsOfInvoice(commissionsInvoice)
    commissionsInvoice
  }

  private void updateCommissionTransactionsOfInvoice(CommissionsInvoice invoice) {
    invoice.commissions.each { commission ->
      commission.status = CommissionTransactionStatus.INVOICED
      commission.invoice = invoice
      commission.save()
    }
  }

}
