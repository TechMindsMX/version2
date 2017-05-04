package com.modulus.uno

class CommissionsInvoiceService {

  def commissionTransactionService
  def invoiceService

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

  Map getCommissionsInvoiceForCompany(Company company, Map params) {
    Map invoices = [:]
    List<CommissionsInvoice> listInvoices = CommissionsInvoice.findAllByReceiver(company, params)
    Integer countInvoices = CommissionsInvoice.countByReceiver(company)
    invoices.listInvoices = listInvoices
    invoices.countInvoices = countInvoices
    invoices
  }

  CommissionsInvoice stampInvoice(CommissionsInvoice invoice) {
    String folioSat = invoiceService.stampCommissionsInvoice(invoice)
    invoice.status = CommissionsInvoiceStatus.STAMPED
    invoice.folioSat = folioSat
    invoice.save()
    invoice
  }

  List getCommissionsSummaryFromInvoice(CommissionsInvoice invoice) {
    def commissionTypes = invoice.commissions.collect { it.type }.unique()

    def summary = []
    commissionTypes.each { type ->
      def totalType = [:]
      totalType.type = type
      def commissionsOfType = invoice.commissions.collect { commission ->
        if (commission.type == type) { return commission }
      } - null
      totalType.total = commissionsOfType*.amount.sum()
      summary << totalType
    }
    summary
  }
}
