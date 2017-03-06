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

  // TODO: Code Review
  def createSaleOrderWithAddress(def params) {
    Long companyId = params.companyId.toLong()
    Long clientId = params.clientId.toLong()
    Long addressId = params.addressId.toLong()
    def fechaCobro = params.fechaCobro
    String externalId = params.externalId ?: ""
    def note = params.note
    PaymentMethod paymentMethod = PaymentMethod.values().find { it.toString() == params.paymentMethod }

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
    String uuidFolio = invoiceService.generateFactura(saleOrder)
    updateSaleOrderFromGeneratedBill(uuidFolio, saleOrder)
  }

  private updateSaleOrderFromGeneratedBill(String uuidFolio, SaleOrder saleOrder) {
    def tokens = uuidFolio.tokenize("_")
    saleOrder.folio = tokens[0]
    saleOrder.status = SaleOrderStatus.EJECUTADA
    saleOrder.save()
    emailSenderService.notifySaleOrderChangeStatus(saleOrder)
    saleOrder
  }

  def executeCancelBill(SaleOrder saleOrder) {
    invoiceService.cancelBill(saleOrder)
    saleOrder.status = SaleOrderStatus.CANCELACION_EJECUTADA
    saleOrder.save()
    emailSenderService.notifySaleOrderChangeStatus(saleOrder)
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
}
