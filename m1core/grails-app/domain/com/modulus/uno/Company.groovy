package com.modulus.uno

import grails.converters.JSON
import com.modulus.uno.paysheet.PaysheetProject
import com.modulus.uno.saleorder.SaleOrder
import com.modulus.uno.credit.Credit

class Company implements Linker {

  String rfc
  String bussinessName
  String aliasCompany
  String webSite
  Integer employeeNumbers
  BigDecimal grossAnnualBilling
  CompanyStatus status = CompanyStatus.CREATED
  String uuid = UUID.randomUUID().toString().replace('-','')[0..15]
  Integer numberOfAuthorizations = 1
  CompanyTaxRegime taxRegime = CompanyTaxRegime.MORAL

  String artemisaId
  String pdfTemplate

  static hasMany = [banksAccounts:BankAccount,
                    documents:S3Asset,
                    addresses:Address,
                    businessEntities:BusinessEntity,
                    products:Product,
                    accounts:ModulusUnoAccount,
                    salesOrders:SaleOrder,
                    purchaseOrders:PurchaseOrder,
                    feesReceipts:FeesReceipt,
                    loanOrders: LoanOrder,
                    commissions:Commission,
                    telephones:Telephone,
                    contacts:ContactInformation,
                    credits: Credit
  ]

  static constraints = {
    bussinessName blank:false,size:1..100
    aliasCompany nullable:true, blank:true
    webSite nullable:true,size:5..50
    grossAnnualBilling max:250000000.00
    rfc blank:false,size:10..50,matches:/^[A-Z]{3,4}([0-9]{2})(1[0-2]|0[1-9])([0-3][0-9])([A-Z0-9]{3})$/
    numberOfAuthorizations nullable:false
    artemisaId nullable:true
    pdfTemplate nullable:true, blank:true
  }

  String toString(){
    aliasCompany ? "${bussinessName} / ${aliasCompany}" : bussinessName
  }

  static marshaller = {
    JSON.registerObjectMarshaller(Company, 1) { m ->
      return [
      id: m.id,
      artemisaId: m.artemisaId,
      rfc: m.rfc,
      bussinessName: m.bussinessName,
      webSite: m.webSite,
      employeeNumbers: m.employeeNumbers,
      grossAnnualBilling: m.grossAnnualBilling,
      status: m.status,
      uuid: m.uuid,
      numberOfAuthorizations: m.numberOfAuthorizations,
      taxRegime: m.taxRegime,
      banksAccounts: m.banksAccounts,
      //documents: m.documents,
      addresses: m.addresses,
      businessEntities: m.businessEntities,
      pdfTemplate: m.pdfTemplate,
      //products: m.products,
      //accounts: m.accounts,
      //salesOrders: m.salesOrders,
      //loanOrders: m.loanOrders,
      //commissions: m.commissions,
      //telephones: m.telephones
      ]
    }
  }



}
