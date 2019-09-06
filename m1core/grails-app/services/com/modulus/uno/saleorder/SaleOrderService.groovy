package com.modulus.uno.saleorder

import grails.util.Environment
import grails.transaction.Transactional
import org.springframework.transaction.annotation.Propagation
import java.text.SimpleDateFormat
import groovy.sql.Sql
import java.math.RoundingMode
import grails.util.Environment

import com.modulus.uno.EmailSenderService
import com.modulus.uno.CompanyService
import com.modulus.uno.CommissionTransactionService
import com.modulus.uno.BusinessEntityService
import com.modulus.uno.businessEntity.BusinessEntitiesGroupService

import com.modulus.uno.Company
import com.modulus.uno.BusinessEntity
import com.modulus.uno.Address
import com.modulus.uno.Authorization
import com.modulus.uno.Conciliation
import com.modulus.uno.User
import com.modulus.uno.businessEntity.BusinessEntitiesGroup

import com.modulus.uno.PaymentWay
import com.modulus.uno.Period
import com.modulus.uno.BusinessException
import com.modulus.uno.AddressType
import com.modulus.uno.CommissionType
import com.modulus.uno.status.SaleOrderStatus
import com.modulus.uno.status.ConciliationStatus
import com.modulus.uno.status.CommissionTransactionStatus
import com.modulus.uno.businessEntity.BusinessEntitiesGroupType

import java.util.zip.*

class SaleOrderService {

  EmailSenderService emailSenderService
  InvoiceService invoiceService
  def grailsApplication
  CompanyService companyService
  def springSecurityService
  def dataSource
  CommissionTransactionService commissionTransactionService
  BusinessEntityService businessEntityService
  BusinessEntitiesGroupService businessEntitiesGroupService

  // TODO: Code Review
  @Transactional
  def createSaleOrderWithAddress(def params) {
    Long companyId = params.companyId.toLong()
    Long clientId = params.clientId.toLong()
    Long addressId = params.addressId.toLong()
    def fechaCobro = params.fechaCobro
    String externalId = params.externalId ?: ""
    def note = params.note
    PaymentWay paymentWay = PaymentWay.values().find {
      println it.toString()
      it.toString() == params.paymentWay }


    if(!companyId && !clientId && !addressId){
      throw new BusinessException("No se puede crear la orden de venta...")
    }
    Company company = Company.get(companyId)
    BusinessEntity businessEntity = BusinessEntity.get(clientId)
    SaleOrder saleOrder = createSaleOrder(businessEntity, company, fechaCobro, externalId, note, paymentWay)
    Address address = Address.get(addressId)
    addTheAddressToSaleOrder(saleOrder, address)
    saleOrder
  }

  @Transactional
  def createSaleOrder(BusinessEntity businessEntity, Company company, def fechaCobro, String externalId, String note, PaymentWay paymentWay) {
    def saleOrder = new SaleOrder(rfc:businessEntity.rfc, clientName: businessEntity.toString(), company:company, externalId:externalId, note:note, paymentWay:paymentWay)
    saleOrder.status = SaleOrderStatus.CREADA
    saleOrder.fechaCobro = Date.parse("dd/MM/yyyy", fechaCobro)
    saleOrder.save()
    saleOrder
  }

  @Transactional
  def addItemToSaleOrder(SaleOrder saleOrder, SaleOrderItem... items){
    items.each {
      saleOrder.addToItems(it)
    }
    saleOrder.save()
    saleOrder
  }

  @Transactional
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

  @Transactional
  def addAuthorizationToSaleOrder(SaleOrder saleOrder, User user) {
    Authorization authorization = new Authorization(user:user).save()
    saleOrder.addToAuthorizations(authorization)
    saleOrder.save()
  }

  @Transactional
  def authorizeSaleOrder(SaleOrder saleOrder){
    saleOrder.status = SaleOrderStatus.AUTORIZADA
    saleOrder.save()
    emailSenderService.notifySaleOrderChangeStatus(saleOrder)
    saleOrder
  }

  @Transactional
  SaleOrder executeSaleOrder(SaleOrder saleOrder){
    if (!commissionTransactionService.getCommissionForCompanyByType(saleOrder.company, CommissionType.FACTURA)) {
      throw new BusinessException("La empresa no tiene comisión de facturación registrada")
    }
    Map stampData = invoiceService.generateFactura(saleOrder)
    commissionTransactionService.registerCommissionForSaleOrder(saleOrder)
    stampData.pdfTemplate = saleOrder.pdfTemplate
    log.info "Stamp UUID: ${stampData.stampId}"
    updateSaleOrderFromGeneratedBill(stampData, saleOrder.id)
    saleOrder.refresh()
    log.info "Sale Order updated with stamped uuid: ${saleOrder.id}, ${saleOrder.pdfTemplate}"
    saleOrder
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  SaleOrder updateSaleOrderFromGeneratedBill(Map stampData, Long saleOrderId) {
    SaleOrder saleOrder = SaleOrder.get(saleOrderId)
    saleOrder.folio = stampData.stampId
    saleOrder.invoiceFolio = stampData.folio
    saleOrder.invoiceSerie = stampData.serie
    saleOrder.stampedDate = Date.parse("yyy-MM-dd'T'HH:mm:ss", stampData.stampDate)
    saleOrder.status = SaleOrderStatus.XML_GENERADO
    saleOrder.pdfTemplate = stampData.pdfTemplate
    saleOrder.save()
    saleOrder
  }

  @Transactional
  SaleOrder generatePdfForStampedInvoice(SaleOrder saleOrder) {
    invoiceService.generatePdfForInvoice(saleOrder)
    saleOrder.status = SaleOrderStatus.EJECUTADA
    saleOrder.save()
    saleOrder
  }

  @Transactional
  def executeCancelBill(SaleOrder saleOrder) {
    invoiceService.cancelBill(saleOrder)
    cancelOrRejectSaleOrder(saleOrder, SaleOrderStatus.CANCELACION_EJECUTADA)
  }

  String getFactura(SaleOrder saleOrder, String format){
    "${grailsApplication.config.modulus.facturacionUrl}${grailsApplication.config.modulus.showFactura}/${saleOrder.folio}_${saleOrder.id}/${format}"
  }

  @Transactional
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

  Map findSaleOrdersForCurrentUser(Company company, def statusOrders, def params) {
    Map saleOrders = [:]
    User currentUser = springSecurityService.currentUser
    List<BusinessEntitiesGroup> clientsGroupsForUser = businessEntitiesGroupService.findClientsGroupsForUserInCompany(currentUser, company)
    if (clientsGroupsForUser) {
      List<BusinessEntity> userClients = businessEntitiesGroupService.getAllClientsFromUserGroups(clientsGroupsForUser)
      saleOrders.list = SaleOrder.findAllByCompanyAndStatusInListAndRfcInList(company, statusOrders, userClients.rfc, params)
      saleOrders.items = SaleOrder.countByCompanyAndStatusInListAndRfcInList(company, statusOrders, userClients.rfc)
    } else {
      saleOrders.list = SaleOrder.findAllByCompanyAndStatusInList(company, statusOrders, params)
      saleOrders.items = SaleOrder.countByCompanyAndStatusInList(company, statusOrders)
    }
    saleOrders
  }

  def getSaleOrdersToList(Long company, params){
    def statusOrders = getSaleOrderStatus(params.status)
    def saleOrders = [:]
    if(company){
      saleOrders = findSaleOrdersForCurrentUser(Company.get(company), statusOrders, params)
    } else{
      saleOrders.list = SaleOrder.findAllByStatusInList(statusOrders, params)
      saleOrders.items = SaleOrder.countByStatusInList(statusOrders)
    }
    saleOrders
  }

  @Transactional
  def deleteItemFromSaleOrder(SaleOrder saleOrder, SaleOrderItem item) {
    SaleOrderItem.executeUpdate("delete SaleOrderItem item where item.id = :id", [id: item.id])
  }

  @Transactional
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

  @Transactional
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

  @Transactional
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

  @Transactional
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

  @Transactional
  SaleOrder createCommissionsInvoiceForCompanyAndPeriod(Company company, Period period) {
    SaleOrder saleOrder = createCommissionsSaleOrder(company, period)
    List balances = commissionTransactionService.getCommissionsBalanceInPeriodForCompanyAndStatus(company, CommissionTransactionStatus.PENDING, period)
    saleOrder = createItemsForCommissionsSaleOrder(saleOrder, balances)
    commissionTransactionService.linkCommissionTransactionsForCompanyInPeriodWithSaleOrder(company, period, saleOrder)
    //TODO: send mails to authorizers for emitter company
    saleOrder
  }

  @Transactional
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

  @Transactional
  SaleOrder createItemsForCommissionsSaleOrder(saleOrder, balances) {
    balances.each { balance ->
      if (balance.balance) {
      SaleOrderItem item = new SaleOrderItem(
        sku:"COMISION-${balance.typeCommission}",
        name:"Servicio financiero de alquiler de operaciones (" + balance.typeCommission == CommissionType.FIJA ? "Comisión Fija" : "Comisiones de ${balance.typeCommission}" + ")",
        quantity:balance.quantity,
        price:balance.balance/balance.quantity,
        iva:new BigDecimal(grailsApplication.config.iva),
        unitType:"UNIDAD DE SERVICIO",
        satKey:"84121607",
        saleOrder:saleOrder
      ).save()
      saleOrder.addToItems(item)
      }
    }
    saleOrder.save()
    saleOrder
  }

  @Transactional
  SaleOrder cancelOrRejectSaleOrder(SaleOrder saleOrder, SaleOrderStatus status) {
    saleOrder.status = status
    saleOrder.save()
    if (commissionTransactionService.saleOrderIsCommissionsInvoice(saleOrder)) {
      commissionTransactionService.unlinkTransactionsForSaleOrder(saleOrder)
    }
    emailSenderService.notifySaleOrderChangeStatus(saleOrder)
    saleOrder
  }

  List<SaleOrder> searchSaleOrders(Long idCompany, Map params) {
    Company company = Company.get(idCompany)
    List<SaleOrder> results = getFilterOrdersWithParams(company, params)
    results
  }

  List<SaleOrder> getFilterOrdersWithParams(Company company, Map params) {
    User currentUser = springSecurityService.currentUser
    List<BusinessEntitiesGroup> clientsGroupsForUser = businessEntitiesGroupService.findClientsGroupsForUserInCompany(currentUser, company)
    def filterCriteria = SaleOrder.createCriteria()
    def results = filterCriteria.list () {
      eq('company', company)
        ilike('rfc', "${params.rfc}%")
        ilike('clientName', "%${params.clientName}%")
        if (clientsGroupsForUser) {
          List<BusinessEntity> userClients = businessEntitiesGroupService.getAllClientsFromUserGroups(clientsGroupsForUser)
            'in'('rfc', userClients.rfc)
        }
      if (params.stampedDateInit || params.stampedDateEnd) {
        if (params.stampedDateInit && !params.stampedDateEnd) {
          params.stampedDateEnd = params.stampedDateInit
        }
        if (params.stampedDateEnd && !params.stampedDateInit) {
          params.stampedDateInit = params.stampedDateEnd
        }
        Date dStampedInit = Date.parse("dd/MM/yyyy hh:mm:ss", params.stampedDateInit.concat(" 00:00:00"))
          Date dStampedEnd = Date.parse("dd/MM/yyyy hh:mm:ss", params.stampedDateEnd.concat(" 23:59:59"))
          between('stampedDate', dStampedInit, dStampedEnd)
      }
      if (params.status) {
        eq('status', SaleOrderStatus."${params.status}")
      }
      if (params.currency) {
        eq('currency', params.currency)
      }
    }
    results
  }

  List<SaleOrder> getAllSaleOrdersWithAmountToPayForRfc(Company company, String rfc) {
    def allExecutedForClient = SaleOrder.findAllByCompanyAndRfcAndStatus(company, rfc, SaleOrderStatus.EJECUTADA)
    allExecutedForClient.findAll { saleOrder -> saleOrder.amountToPay.setScale(2, RoundingMode.HALF_UP) > 0 }
  }

  List<SaleOrder> getAllSaleOrdersExecutedAndAuthorizedForRfc(Company company, String rfc) {
    SaleOrder.createCriteria().list{
      eq("rfc", rfc)
      eq("company", company)
      'in'("status", [SaleOrderStatus.EJECUTADA, SaleOrderStatus.AUTORIZADA])
    }
  }

  List<SaleOrder> getAllSaleOrdersAlreadyConciliate(Company company, String rfc) {
    def allExecutedForClient = SaleOrder.findAllByCompanyAndRfcAndStatus(company, rfc, SaleOrderStatus.EJECUTADA)
    allExecutedForClient.findAll { saleOrder -> saleOrder.payments }
  }

  @Transactional
  SaleOrder getSerieForSaleOrderFromInvoice(SaleOrder saleOrder) {
    String emitter = "AAA010101AAA/${saleOrder.company.id}"
    if (Environment.current == Environment.PRODUCTION) {
      emitter = "${saleOrder.company.rfc}/${saleOrder.company.id}"
    }
    String serie = invoiceService.getSerieFromInvoice(emitter, saleOrder.folio)
    saleOrder.invoiceSerie = serie
    saleOrder.save()
    saleOrder
  }

  @Transactional
  SaleOrder getFolioForSaleOrderFromInvoice(SaleOrder saleOrder) {
    String emitter = "AAA010101AAA/${saleOrder.company.id}"
    if (Environment.current == Environment.PRODUCTION) {
      emitter = "${saleOrder.company.rfc}/${saleOrder.company.id}"
    }
    String folio = invoiceService.getFolioFromInvoice(emitter, saleOrder.folio)
    saleOrder.invoiceFolio = folio
    saleOrder.save()
    saleOrder
  }

  @Transactional
  SaleOrder updateStampDateAlreadyUpdate(SaleOrder saleOrder) {
    String emitter = "AAA010101AAA/${saleOrder.company.id}"
    if (Environment.current == Environment.PRODUCTION) {
      emitter = "${saleOrder.company.rfc}/${saleOrder.company.id}"
    }
    Date stampDate = invoiceService.getStampedDate(emitter, saleOrder.folio)
    log.info "Stamp date got: ${stampDate}"
    saleOrder.stampedDate = stampDate
    saleOrder.save()
    saleOrder
  }

  List<BusinessEntity> searchClientsForCompany(Company company, String dataQuery) {
    User currentUser = springSecurityService.currentUser
    List<BusinessEntity> clients = []
    if (currentUser.businessEntitiesGroups.findAll { group -> group.company == company && group.type == BusinessEntitiesGroupType.CLIENTS }) {
      clients = businessEntitiesGroupService.findBusinessEntitiesByKeyword(currentUser, company, dataQuery)
    } else {
      clients = businessEntityService.findBusinessEntityByKeyword(dataQuery, "CLIENT", company)
    }
    clients
  }

  List<SaleOrder> getCanceledSaleOrders(String rfc) {
   SaleOrder.findAllByRfcAndStatus(rfc, SaleOrderStatus.CANCELACION_EJECUTADA)
  }

  @Transactional
  SaleOrder updateReplacementInvoiceUUID(SaleOrder saleOrder, String uuid){
    saleOrder.uuidReplacement = uuid
    saleOrder.save()
    saleOrder
  }

  def generateZipFileFor(SaleOrder saleOrder) {
    def invoices = downloadFiles(saleOrder)
    String nameFile = generateName(saleOrder.id, saleOrder.folio)

    File tmpZipFile = File.createTempFile(nameFile + "_", ".zip")
    ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(tmpZipFile))

    invoices.each { invoice ->
      zipFile.putNextEntry(new ZipEntry(invoice.name))
      def buffer = new byte[invoice.size()]
      invoice.withInputStream {
        zipFile.write(buffer, 0, it.read(buffer))
      }
      zipFile.closeEntry()
    }
    zipFile.close()

    tmpZipFile
  }

  private def downloadFiles(SaleOrder saleOrder) {
    String nameFile = generateName(saleOrder.id, saleOrder.folio)

    ['.xml', '.pdf'].collect { format ->
      def filename = "${nameFile}${format}"
      String url = generateUrl(filename, saleOrder)
      downloadFile(nameFile, format, url)
    }
  }

  private String generateName(Long id, String folio) {
    folio.length() > 36 ? "${folio}" : "${folio}_${id}"
  }

  def String generateUrl(String filename, SaleOrder saleOrder) {
    def parentDir = (Environment.current == Environment.PRODUCTION) ? saleOrder.company.rfc : "AAA010101AAA"
    def rfc = "${parentDir}/${saleOrder.company.id}"
    def urlPath = grailsApplication.config.modulus.showFactura.replace('#rfc', rfc).replace('#file', filename)

    "${grailsApplication.config.modulus.facturacionUrl}${urlPath}".toString()
  }

  private File downloadFile(String nameFile, String format, String url) {
    File tmpFile = File.createTempFile(nameFile, format)
    new URL(url).openConnection().with { conn ->
      conn.inputStream.with { inp ->
        tmpFile.withOutputStream { out ->
          out << inp
          inp.close()
        }
      }
    }
    tmpFile
  }
}
