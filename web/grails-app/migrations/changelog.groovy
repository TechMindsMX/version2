databaseChangeLog = {
  include file: 'initial-structure.groovy'
  include file: 'create-m1-roles.groovy'
  include file: 'adding-user-admin-production.groovy'
  include file: 'adding-list-banks.groovy'
  include file: 'adding-colunm-reference-to-payment.groovy'
  include file: 'adding-uuid-to-purchase-order.groovy'
  include file: 'moving-data-from-uuid-to-folio-in-sale-order.groovy'
  include file: 'adding-uuid-to-cashout-order.groovy'
  include file: 'adding-uuid-to-fees-receipt.groovy'
  include file: 'adding-unique-to-variable-of-corporate.groovy'
  include file: 'adding-columns-dates-and-status-to-stp-deposit.groovy'
  include file: 'adding-column-payment-method-to-sale-order.groovy'
  include file: 'updating-type-venta-to-factura-in-commission.groovy'
  include file: 'adding-column-alias-stp-to-modulus-uno-account.groovy'
  include file: 'adding-group-notification.groovy'
  include file: 'adding-column-discount-to-sale-order-item.groovy'
  include file: 'add-machine-state-domain-classes.groovy'
  include file: 'updating-machinery-link-class.groovy'
  include file: 'adding-actions-to-company.groovy'
  include file: 'updating-machine-structure.groovy'
  include file: 'updating-transition-actions-relation.groovy'
  include file: 'adding-columns-currency-and-changetype-to-saleorder.groovy'
  include file: 'removing-state-relation-with-log.groovy'
  include file: 'adding-uuid-to-machine.groovy'
  include file: 'drop-not-null-constraint-to-change-type-from-sale-order.groovy'
  include file: 'create-domain-sale-order-payment.groovy'
  include file: 'create-domain-conciliation.groovy'
  include file: 'create-transactions.groovy'
  include file: 'adding-relation-transaction.groovy'
  include file: 'droping-columns-timone-from-cashout-order.groovy'
  include file: 'initializeUuidToPurchaseOrder.groovy'
  include file: 'initialize-uuid-from-cashout-order-existing.groovy'
  include file: 'adding-notification-for-state.groovy'
  include file: 'deleting-deposit-order.groovy'
  include file: 'adding-neighboorhood-to-address.groovy'
  include file: 'adding-dates-created-and-updated-to-fees-receipt.groovy'
  include file: 'initialize-uuid-for-existing-fees-receipt.groovy'
  include file: 'adding-column-reconcilable-to-transactions-banks.groovy'
  include file: 'adding-conciliation-status-to-bankings-transactions.groovy'
  include file: 'adding-column-banking-transaction-to-conciliation.groovy'
  include file: 'adding-mock-machine-class.groovy'
  include file: 'adding-the-linker-structure.groovy'
  include file: 'creating-commission-transaction-domain.groovy'
  include file: 'updating-commission-prestamo-to-commission-fija-for-existing-commissions.groovy'
  include file: 'create-domain-commissions-invoice.groovy'
  include file: 'adding-column-commissions-invoice-to-commission-transaction.groovy'
  include file: 'create-domain-payment-m1-emitter.groovy'
  include file: 'create-domain-conciliation-commissions-invoice.groovy'
  include file: 'create-domain-commissions-invoice-payment.groovy'
  include file: 'drop-column-invoice-from-commission-transaction.groovy'
  include file: 'adding-sale-order-to-commission-transaction.groovy'
  include file: 'drop-commissions-invoice-domains.groovy'
  include file: 'create-domain-account-statement-for-bank-account.groovy'
  include file: 'create-domain-satus-order-stp.groovy'
  include file: 'adding-status-to-payment-to-purchase.groovy'
  include file: 'rename-column-ieps-to-iva-retention-in-sale-order-item.groovy'
  include file: 'drop-domain-payment-m1-emitter.groovy'
  include file: 'adding-column-status-to-business-entity.groovy'
  include file: 'adding-column-number-to-employee-link.groovy'
  include file: 'create-domain-data-imss-employee.groovy'
  include file: 'create-domain-paysheet-project.groovy'
  include file: 'adding-column-card-number-to-bank-account.groovy'
  include file: 'create-domains-prepaysheet.groovy'
  include file: 'create-menu-domains.groovy'
  include file: 'create-menu-link-domain.groovy'
  include file: 'adding-attribute-to-menus.groovy'
  include file: 'adding-default-menus-for-user-m1.groovy'
  include file: 'create-domains-for-paysheet.groovy'
  include file: 'adding-column-commission-to-paysheet-project.groovy'
  include file: 'create-roles-for-paysheets.groovy'
  include file: 'adding-column-reject-reason-to-paysheet.groovy'
  include file: 'create-domain-prepaysheet-employee-incidence.groovy'
	include file: 'adding-dispersion-files-s3asset-to-paysheet.groovy'
	include file: 'adding-column-source-to-payment-to-purchase.groovy'
  include file: 'modify-column-commission-to-paysheet-project.groovy'
  include file: 'adding-column-client-number-to-bankaccount.groovy'
  include file: 'adding-column-payment-to-purchase-to-conciliation.groovy'
	include file: 'create-roles-for-queries.groovy'
	include file: 'adding-column-paymentway-to-paysheet-employee.groovy'
	include file: 'new-menu-relation.groovy'
  include file: 'create-domain-paysheet-contract.groovy'
  include file: 'modify-paysheet-project-into-paysheet-contract.groovy'
  include file: 'modify-prepaysheet-with-paysheet-contract.groovy'
  include file: 'modify-paysheet-with-paysheet-contract.groovy'
  include file: 'create-domain-payer-paysheet-project.groovy'
  include file: 'create-domain-quotation-contract.groovy'
  include file: 'update-column-quotationContract.groovy'
  include file: 'add-domain-quotationRequest.groovy'
  include file: 'create-domain-final-transaction-result.groovy'
  include file: 'adding-column-date-quotation-request.groovy'
  include file: 'create-domain-quotation-payment-request.groovy'
  include file: 'drop-not-null-constraint-to-clabe-and-account-number-in-bankaccount.groovy'
  include file: 'drop-unique-constraint-to-column-email-in-profile.groovy'
  include file: 'adding-column-username-to-registration-code.groovy'
  include file: 'updating-column-paymentMethod-quotationPaymnetRequest.groovy'
  include file: 'adding-employees-to-paysheet-project.groovy'
  include file: 'adding-users-to-paysheet-contract.groovy'
  include file: 'adding-column-satkey-to-product.groovy'
  include file: 'create-domain-unit-type.groovy'
  include file: 'adding-column-satkey-to-sale-order.groovy'
  include file: 'modify-column-unit-type-from-product.groovy'
  include file: 'rename-payment-method-to-payment-way-into-sale-order.groovy'
  include file: 'updating-payment-way-na-to-be-defined-in-sale-order.groovy'
  include file: 'adding-columns-payment-method-and-invoice-purpose-to-sale-order.groovy'
  include file: 'adding-columns-discount-and-ivaretention-to-purchase-order-item.groovy'
  include file: 'create-domain-biller-paysheet-project.groovy'
  include file: 'modify-column-netpayment-to-crudepayment-into-dataimssemployee.groovy'
  include file: 'rename-column-netpayment-to-crudepayment-into-prepaysheetemployee.groovy'
  include file: 'adding-columns-incometaxassimilable-and-crudeassimilable-to-paysheetemployee.groovy'
  include file: 'rename-crude-salary-to-total-salary-into-data-imss-employee.groovy'
  include file: 'rename-column-crude-payment-to-net-payment-into-prepaysheet-employee.groovy'
  include file: 'adding-columns-quotation-request.groovy'
  include file: 'delete-column-quotation-request.groovy'
  include file: 'create-domain-quotation-commission.groovy'
  include file: 'adding-column-users-quotation-contract.groovy'
  include file: 'changed-column-quotation-request.groovy'
  include file: 'changed-column-corporate-has-quotation.groovy'
  include file: 'add-roles-quotation.groovy'
  include file: 'adding-columns-paymentway-paymentmethod-and-invoicepurpose-to-quotation-request.groovy'
  include file: 'adding-column-status-to-corporate.groovy'
  include file: 'drop-unique-constraint-to-nss-from-data-imss-employee.groovy'
  include file: 'adding-column-alias-to-company.groovy'
  include file: 'drop-unique-constraint-to-rfc-in-company.groovy'
  include file: 'create-domain-dispersion-result-file-and-add-relation-to-paysheet.groovy'
  include file: 'adding-column-schema-to-dispersion-result-file.groovy'
  include file: 'create-domain-dispersion-result-file-detail.groovy'
  include file: 'adding-column-employer-registration-to-paysheet-contract.groovy'
  include file: 'adding-columns-for-paysheet-receipt-into-data-imss-employee.groovy'
  include file: 'adding-column-federal-entity-to-paysheet-project.groovy'
    include file: 'adding-column-branch-to-pre-paysheet-employee.groovy'
    include file: 'adding-columns-key-keyType-taxed-amount-to-pre-paysheet-employee-incidence.groovy'
    include file: 'create-domain-extra-hour-incidence-and-add-relation-to-pre-paysheet-employee-incidence.groovy'
    include file: 'adding-column-discharge-date-into-data-imss-employee.groovy'
    include file: 'adding-folio-and-serie-into-paysheet-contract.groovy'
    include file: 'adding-column-job-risk-to-data-imss-employee.groovy'
    include file: 'adding-column-paysheet-receipt-uuid-to-paysheet-employee.groovy'
    include file: 'updating-column-contract-type-indefined-to-undefined-into-data-imss-employee.groovy'
    include file: 'adding-column-paysheet-receipt-uuid-ias-to-paysheet-employee.groovy'
    include file: 'create-domain-user-employee.groovy'
}
