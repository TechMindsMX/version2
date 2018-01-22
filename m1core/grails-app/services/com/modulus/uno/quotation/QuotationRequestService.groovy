package com.modulus.uno.quotation

import grails.transaction.Transactional
import com.modulus.uno.SaleOrderService
import com.modulus.uno.PaymentWay
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
import java.math.RoundingMode
import org.springframework.transaction.annotation.Propagation

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
    QuotationRequest processRequest(QuotationRequest quotationRequest){
      SaleOrder saleOrder = createSaleOrderFromQuotationRequest(quotationRequest)
      quotationRequest.saleOrder = saleOrder
      quotationCommissionService.create(quotationRequest, quotationRequest.commission)
      quotationRequest.status = QuotationRequestStatus.PROCESSED
      quotationRequest.save()
      log.info "Quotation Request was processed: ${quotationRequest.dump()}"
      quotationRequest
    }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
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
