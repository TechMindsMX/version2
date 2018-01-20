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
    }

    SaleOrder createSaleOrderFromQuotationRequest(QuotationRequest quotationRequest) {
      Map params = getParamsToGenerateSaleOrder(quotationRequest)
      SaleOrderCommand saleOrderCommand = new SaleOrderCommand(
          addressId:params.addressId,
          companyId:params.companyId,
          clientId:params.clientId,
          note:params.note,
          fechaCobro:params.fechaCobro,
          paymentWay: params.paymentWay
      )
      def saleOrder = saleOrderCommand.createOrUpdateSaleOrder()
      saleOrder.status = SaleOrderStatus.AUTORIZADA

      if(!saleOrder.save()){
        throw new QuotationException("No se pudo crear la orden de venta")
      }

      SaleOrderItemCommand saleOrderItemCommand = new SaleOrderItemCommand(
          sku:quotationRequest.product.sku,
          name:quotationRequest.product.name,
          quantity:"1",
          price:quotationRequest.subtotal.toString(),
          discount:"0",
          ivaRetention:"0",
          iva:new BigDecimal(grailsApplication.config.iva).setScale(2, RoundingMode.HALF_UP),
          unitType:"UNIDAD"
      )
      def saleOrderItem  = saleOrderItemCommand.createSaleOrderItem()
      saleOrderItem.saleOrder = saleOrder

      if (!saleOrderItem.save()) {
        throw new QuotationException("No se pudo crear el detalla de la orden de venta")
      }

      saleOrder
    }

    Map getParamsToGenerateSaleOrder(QuotationRequest quotationRequest){
      if(!quotationRequest.quotationContract.client.addresses.find { it.addressType == AddressType.FISCAL }){
        throw new QuotationException("El cliente no tiene dirección fiscal")
      }

      [
        companyId:quotationRequest.biller.id,
        clientId:quotationRequest.quotationContract.client.id,
        addressId:(quotationRequest.quotationContract.client.addresses.find { it.addressType == AddressType.FISCAL }).id,
        fechaCobro: new Date().format( 'dd/MM/yyyy' ),
        externalId:"",
        note:"",
        paymentWay:"03 - TRANSFERENCIA ELECTRONICA"
      ]
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
