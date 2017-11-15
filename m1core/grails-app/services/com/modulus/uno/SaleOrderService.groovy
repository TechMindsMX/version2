package com.modulus.uno

import grails.transaction.Transactional
import java.text.SimpleDateFormat
import groovy.sql.Sql

@Transactional
class SaleOrderService {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
  def emailSenderService
  def invoiceService
  def grailsApplication
  def companyService
  def springSecurityService
  def dataSource
  def commissionTransactionService


  // TODO: Code Review
  def createSaleOrderWithAddress(def params) {
    Long companyId = params.companyId.toLong()
    Long clientId = params.clientId.toLong()
    Long addressId = params.addressId.toLong()
    def fechaCobro = params.fechaCobro
    String externalId = params.externalId ?: ""
    def note = params.note
    PaymentMethod paymentMethod = PaymentMethod.values().find {
      println it.toString()
      it.toString() == params.paymentMethod }


    if(!companyId && !clientId && !addressId){
      throw new BusinessException("No se puede crear la orden de venta...")
    }
    Company company = Company.get(companyId)
    BusinessEntity businessEntity = BusinessEntity.get(clientId)
    SaleOrder saleOrder = createSaleOrder(businessEntity, company, fechaCobro, externalId, note, paymentMethod)
    Address address = Address.get(addressId)
    addTheAddressToSaleOrder(saleOrder, address)
    saleOrder
  }

  def createSaleOrder(BusinessEntity businessEntity, Company company, def fechaCobro, String externalId, String note, PaymentMethod paymentMethod) {
    def saleOrder = new SaleOrder(rfc:businessEntity.rfc, clientName: businessEntity.toString(), company:company, externalId:externalId, note:note, paymentMethod:paymentMethod)
    saleOrder.status = SaleOrderStatus.CREADA
    saleOrder.fechaCobro = Date.parse("dd/MM/yyyy", fechaCobro)
    saleOrder.save()
    saleOrder
  }

  def addItemToSaleOrder(SaleOrder saleOrder, SaleOrderItem... items){
    items.each {
      saleOrder.addToItems(it)
    }
    saleOrder.save()
    saleOrder
  }

  def sendOrderToConfirmation(SaleOrder saleOrder){
    saleOrder.status = SaleOrderStatus.POR_AUTORIZAR
    saleOrder.save()
    emailSenderService.notifySaleOrderChangeStatus(saleOrder)
    saleOrder
  }

  Boolean isFullAuthorized (SaleOrder saleOrder) {
    def alreadyAuthorizations = saleOrder.authorizations ? saleOrder.authorizations.size() : 0
    alreadyAuthorizations >= saleOrder.company.numberOfAuthorizations
  }

  def addAuthorizationToSaleOrder(SaleOrder saleOrder, User user) {
    Authorization authorization = new Authorization(user:user).save()
    saleOrder.addToAuthorizations(authorization)
    saleOrder.save()
  }

  def authorizeSaleOrder(SaleOrder saleOrder){
    saleOrder.status = SaleOrderStatus.AUTORIZADA
    saleOrder.save()
    emailSenderService.notifySaleOrderChangeStatus(saleOrder)
    saleOrder
  }

  def executeSaleOrder(SaleOrder saleOrder){
    commissionTransactionService.registerCommissionForSaleOrder(saleOrder)
    String uuidFolio = invoiceService.generateFactura(saleOrder)
    updateSaleOrderFromGeneratedBill(uuidFolio, saleOrder)
  }

  private updateSaleOrderFromGeneratedBill(String uuidFolio, SaleOrder saleOrder) {
    saleOrder.folio = uuidFolio
    saleOrder.status = SaleOrderStatus.EJECUTADA
    saleOrder.save()
    emailSenderService.notifySaleOrderChangeStatus(saleOrder)
    saleOrder
  }

  def executeCancelBill(SaleOrder saleOrder) {
    invoiceService.cancelBill(saleOrder)
    cancelOrRejectSaleOrder(saleOrder, SaleOrderStatus.CANCELACION_EJECUTADA)
  }

  String getFactura(SaleOrder saleOrder, String format){
    "${grailsApplication.config.modulus.facturacionUrl}${grailsApplication.config.modulus.showFactura}/${saleOrder.folio}_${saleOrder.id}/${format}"
  }

  def addTheAddressToSaleOrder(SaleOrder saleOrder, Address address){
    saleOrder.addToAddresses(address)
    saleOrder.save()
    saleOrder
  }

  def generatePreviewInvoiceForSaleOrderWithTemplate(SaleOrder saleOrder) {
    invoiceService.generatePreviewFactura(saleOrder)
  }

  def getTotalSaleOrderAuthorizedOfCompany(Company company){
    SaleOrder.findAllByCompanyAndStatus(company, SaleOrderStatus.AUTORIZADA).total.sum()
  }

  def getSaleOrderStatus(String status){
    def saleOrderStatuses = []
    saleOrderStatuses = Arrays.asList(SaleOrderStatus.values())
    if (status){
      def listStatus = status.tokenize(",")
      saleOrderStatuses = listStatus.collect { it as SaleOrderStatus }
    }

    saleOrderStatuses
  }

  def getSaleOrdersToList(Long company, params){
    def statusOrders = getSaleOrderStatus(params.status)
    def saleOrders = [:]
    if(company){
      saleOrders.list = SaleOrder.findAllByCompanyAndStatusInList(Company.get(company), statusOrders, params)
      saleOrders.items = SaleOrder.countByCompanyAndStatusInList(Company.get(company), statusOrders)
    } else{
      saleOrders.list = SaleOrder.findAllByStatusInList(statusOrders, params)
      saleOrders.items = SaleOrder.countByStatusInList(statusOrders)
    }
    saleOrders
  }

  def deleteItemFromSaleOrder(SaleOrder saleOrder, SaleOrderItem item) {
    SaleOrderItem.executeUpdate("delete SaleOrderItem item where item.id = :id", [id: item.id])
  }

  def createOrUpdateSaleOrder(def params) {
    SaleOrder saleOrder = SaleOrder.findByExternalId(params.externalId)
    if (saleOrder) {
      saleOrder.fechaCobro = Date.parse("dd/MM/yyyy", params.fechaCobro)
      saleOrder.save()
    } else {
      saleOrder = createSaleOrderWithAddress(params)
    }
    saleOrder
  }

  def updateDateChargeForOrder(Long id, Date chargeDate) {
    SaleOrder saleOrder = SaleOrder.get(id)
    if (!saleOrder.originalDate)
      saleOrder.originalDate = saleOrder.fechaCobro
      saleOrder.fechaCobro = chargeDate
      saleOrder.save()
      saleOrder
  }

  List<SaleOrder> obtainListPastDuePortfolio(Long idCompany, Integer days) {
    Company company = Company.get(idCompany)
    def dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    Date end = dateFormat.parse(dateFormat.format(new Date()-days))
    def saleCriteria = SaleOrder.createCriteria()
    def listResult = []

    if (days==120) {
      listResult = saleCriteria.list {
        eq("company", company)
        or {
          and {
            le("fechaCobro", end)
            isNull("originalDate")
          }
          and {
            le("originalDate", end)
          }
        }
        eq("status", SaleOrderStatus.EJECUTADA)
      }
    } else {
      Date begin = dateFormat.parse(dateFormat.format(new Date()-(days+30)))
      listResult = saleCriteria.list {
        eq("company", company)
        or {
          and {
            between("fechaCobro", begin, end)
            isNull("originalDate")
          }
          between("originalDate", begin, end)
        }
        eq("status", SaleOrderStatus.EJECUTADA)
      }
    }
    listResult
  }

  def deleteSaleOrder(SaleOrder saleOrder) {
    Sql sql = new Sql(dataSource)
    sql.execute("delete from sale_order_item where sale_order_id=${saleOrder.id}")
    sql.execute("delete from sale_order_address where sale_order_addresses_id=${saleOrder.id}")
    saleOrder.delete()
  }

  List<SaleOrder> findOrdersToConciliateForCompanyAndClient(Company company, String rfc) {
    SaleOrder.findAllByCompanyAndRfcAndStatus(company, rfc, SaleOrderStatus.EJECUTADA)
  }

  List<SaleOrder> findOrdersToConciliateForCompany(Company company) {
    SaleOrder.findAllByCompanyAndStatus(company, SaleOrderStatus.EJECUTADA)
  }

  SaleOrder addPaymentToSaleOrder(SaleOrder saleOrder, BigDecimal amount, BigDecimal changeType) {
    BigDecimal amountPayment = saleOrder.currency == "MXN" ? amount : amount/changeType
    SaleOrderPayment saleOrderPayment = new SaleOrderPayment(amount:amountPayment)
    saleOrder.addToPayments(saleOrderPayment)
    saleOrder.save()
    if (saleOrder.amountToPay <= 0) {
      saleOrder.status = SaleOrderStatus.PAGADA
      saleOrder.save()
      if (commissionTransactionService.saleOrderIsCommissionsInvoice(saleOrder)) {
        commissionTransactionService.conciliateTransactionsForSaleOrder(saleOrder)
      }
    }
    saleOrder
  }

  List<SaleOrder> getSaleOrdersToConciliateFromCompany(Company company) {
    List<SaleOrder> saleOrders = findOrdersToConciliateForCompany(company)
    List<Conciliation> conciliations = Conciliation.findAllByCompanyAndStatus(company, ConciliationStatus.TO_APPLY)
    List<SaleOrder> saleOrdersFiltered = saleOrders.findAll { saleOrder ->
      if (!conciliations.find { conciliation -> conciliation.saleOrder.id == saleOrder.id }){
        saleOrder
      }
    }
    saleOrdersFiltered
  }

  BigDecimal getTotalSoldForClient(Company company, String rfc/*, Date firstDate, Date lastDate*/) {
    def salesOrderEjecuted = SaleOrder.createCriteria().list{
      eq("rfc", rfc)
      eq("company", company)
      'in'("status", [SaleOrderStatus.EJECUTADA, SaleOrderStatus.PAGADA])
      //   between("dateCreated", firstDate, lastDate)
    }
    salesOrderEjecuted.total.sum()
  }
  BigDecimal getTotalSoldForClientStatusConciliated(Company company, String rfc) {
    List<SaleOrder> salesOrderConciliated = SaleOrder.createCriteria().list{
      eq("rfc", rfc)
      eq("company", company)
    }
    salesOrderConciliated.amountPayed.sum()
  }

  SaleOrder createCommissionsInvoiceForCompanyAndPeriod(Company company, Period period) {
    SaleOrder saleOrder = createCommissionsSaleOrder(company, period)
    List balances = commissionTransactionService.getCommissionsBalanceInPeriodForCompanyAndStatus(company, CommissionTransactionStatus.PENDING, period)
    saleOrder = createItemsForCommissionsSaleOrder(saleOrder, balances)
    commissionTransactionService.linkCommissionTransactionsForCompanyInPeriodWithSaleOrder(company, period, saleOrder)
    //TODO: send mails to authorizers for emitter company
    saleOrder
  }

  SaleOrder createCommissionsSaleOrder(Company company, Period period) {
    Company emitter = Company.findByRfc(grailsApplication.config.m1emitter.rfc)
    Address address = company.addresses.find { addr -> addr.addressType == AddressType.FISCAL }
    SaleOrder saleOrder = new SaleOrder(
      rfc:company.rfc,
      clientName:company.bussinessName,
      fechaCobro:new Date(),
      note:"Comisiones del ${period.init.format('dd-MM-yyyy')} al ${period.end.format('dd-MM-yyyy')}",
      currency:"MXN",
      company:emitter,
      status:SaleOrderStatus.POR_AUTORIZAR
    )
    saleOrder.addToAddresses(address)
    saleOrder.save()
    saleOrder
  }

  SaleOrder createItemsForCommissionsSaleOrder(saleOrder, balances) {
    balances.each { balance ->
      if (balance.balance) {
      SaleOrderItem item = new SaleOrderItem(
        sku:"COMISION-${balance.typeCommission}",
        name:balance.typeCommission == CommissionType.FIJA ? "Comisión Fija" : "Comisiones de ${balance.typeCommission}",
        quantity:balance.quantity,
        price:balance.balance/balance.quantity,
        iva:new BigDecimal(grailsApplication.config.iva),
        unitType:"SERVICIO",
        saleOrder:saleOrder
      ).save()
      saleOrder.addToItems(item)
      }
    }
    saleOrder.save()
    saleOrder
  }

  SaleOrder cancelOrRejectSaleOrder(SaleOrder saleOrder, SaleOrderStatus status) {
    saleOrder.status = status
    saleOrder.save()
    if (commissionTransactionService.saleOrderIsCommissionsInvoice(saleOrder)) {
      commissionTransactionService.unlinkTransactionsForSaleOrder(saleOrder)
    }
    emailSenderService.notifySaleOrderChangeStatus(saleOrder)
    saleOrder
  }

}
