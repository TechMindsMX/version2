package com.modulus.uno.quotation

import grails.transaction.Transactional
import com.modulus.uno.SaleOrderService
import com.modulus.uno.Product
import com.modulus.uno.SaleOrder
import com.modulus.uno.SaleOrderItem
import com.modulus.uno.SaleOrderItemCommand
import com.modulus.uno.SaleOrderCommand
import com.modulus.uno.Corporate
import com.modulus.uno.CorporateService
import com.modulus.uno.CompanyService
import com.modulus.uno.CompanyStatus
import com.modulus.uno.SaleOrderStatus
import com.modulus.uno.User
import com.modulus.uno.AddressType
import com.modulus.uno.Company
import com.modulus.uno.PaymentWay
import com.modulus.uno.PaymentMethod
import com.modulus.uno.InvoicePurpose
import java.math.RoundingMode
import org.springframework.transaction.annotation.Propagation
import java.text.*

class QuotationRequestService {

    def grailsApplication
    SaleOrderService saleOrderService
    CorporateService corporateService
    CompanyService companyService
    QuotationCommissionService quotationCommissionService

    @Transactional
    QuotationRequest create(QuotationRequest quotationRequest){
      quotationRequest.save()
    }

    @Transactional
    QuotationRequest update(QuotationRequest quotationRequest){
      quotationRequest.save()
    }

    @Transactional
    def delete(QuotationRequest quotationRequest){
      quotationRequest.delete()
    }

    @Transactional
    QuotationRequest processRequest(def params){
      QuotationRequest quotationRequest = updateQuotationRequest(params)
      SaleOrder saleOrder = createSaleOrderFromQuotationRequest(quotationRequest)
      quotationRequest.saleOrder = saleOrder
      quotationCommissionService.create(quotationRequest, quotationRequest.commission)
      quotationRequest.status = QuotationRequestStatus.PROCESSED
      if (!quotationRequest.save()) {
        transactionStatus.setRollbackOnly()
        throw new QuotationException("No se pudo actualizar la solicitud de cotización")
      }

      log.info "Quotation Request was processed: ${quotationRequest.dump()}"
      quotationRequest
    }

    QuotationRequest updateQuotationRequest(def params) {
      Company biller = Company.get(params.biller) 
      Product product = Product.get(params.product)
      BigDecimal commission = getValueInBigDecimal(params.commission)
      PaymentWay paymentWay = PaymentWay.values().find { it.toString() == params.paymentWay }
      PaymentMethod paymentMethod = PaymentMethod.values().find { it.toString() == params.paymentMethod }
      InvoicePurpose invoicePurpose = InvoicePurpose.values().find { it.toString() == params.invoicePurpose }
      QuotationRequest quotationRequest = QuotationRequest.get(params.id)
      quotationRequest.biller = biller
      quotationRequest.product = product
      quotationRequest.commission = commission
      quotationRequest.paymentWay = paymentWay
      quotationRequest.paymentMethod = paymentMethod
      quotationRequest.invoicePurpose = invoicePurpose
      quotationRequest
    }

  private def getValueInBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"));
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    df.setParseBigDecimal(true);
    BigDecimal bd = (BigDecimal) df.parse(value);
    bd
  }

    SaleOrder createSaleOrderFromQuotationRequest(QuotationRequest quotationRequest) {
      if(!quotationRequest.quotationContract.client.addresses.find { it.addressType == AddressType.FISCAL }){
        transactionStatus.setRollbackOnly()
        throw new QuotationException("El cliente no tiene dirección fiscal")
      }

      SaleOrderCommand saleOrderCommand = createSaleOrderCommandFromQuotationRequest(quotationRequest)
      def saleOrder = saleOrderCommand.createOrUpdateSaleOrder()
      saleOrder.status = SaleOrderStatus.AUTORIZADA

      if(!saleOrder.save()){
        transactionStatus.setRollbackOnly()
        throw new QuotationException("No se pudo crear la orden de venta")
      }
      log.info "The sale order was created: ${saleOrder.dump()}"

      SaleOrderItemCommand saleOrderItemCommand = createSaleOrderItemCommandFromQuotationRequest(quotationRequest)
      def saleOrderItem  = saleOrderItemCommand.createSaleOrderItem()
      saleOrderItem.saleOrder = saleOrder

      if (!saleOrderItem.save()) {
        transactionStatus.setRollbackOnly()
        throw new QuotationException("No se pudo crear el detalle de la orden de venta")
      }

      log.info "The sale order item was created: ${saleOrderItem.dump()}"

      saleOrder
    }

    SaleOrderCommand createSaleOrderCommandFromQuotationRequest(QuotationRequest quotationRequest) {
      new SaleOrderCommand(
        addressId:(quotationRequest.quotationContract.client.addresses.find { it.addressType == AddressType.FISCAL }).id,
        companyId:quotationRequest.biller.id,
        clientId:quotationRequest.quotationContract.client.id,
        note:"",
        fechaCobro:new Date().format('dd/MM/yyyy'),
        paymentWay: quotationRequest.paymentWay.toString(),
        paymentMethod: quotationRequest.paymentMethod.toString(),
        invoicePurpose: quotationRequest.invoicePurpose.toString()
      )
    }

    SaleOrderItemCommand createSaleOrderItemCommandFromQuotationRequest(QuotationRequest quotationRequest) {
      new SaleOrderItemCommand(
        sku:quotationRequest.product.sku,
        name:quotationRequest.product.name,
        quantity:"1",
        price:quotationRequest.subtotal.toString(),
        discount:"0",
        ivaRetention:"0",
        iva:new BigDecimal(grailsApplication.config.iva).setScale(2, RoundingMode.HALF_UP),
        unitType:"UNIDAD"
      )
    }

    @Transactional
    QuotationRequest sendQuotation(QuotationRequest quotationRequest){
      quotationRequest.status = QuotationRequestStatus.SEND
      quotationRequest.save()
    }

    def getBillerCompanies(Long company){
      def corporate = corporateService.getCorporateFromCompany(company)
      List<Corporate> companies = companyService.findCompaniesByCorporateAndStatus(CompanyStatus.ACCEPTED, corporate.id)
      companies
    }
    
    BigDecimal getIvaCurrent(){
      new BigDecimal(grailsApplication.config.iva)
    }

}
