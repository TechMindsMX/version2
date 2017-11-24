package com.modulus.uno.quotation

import grails.transaction.Transactional
import com.modulus.uno.SaleOrderService
import com.modulus.uno.PaymentMethod
import com.modulus.uno.SaleOrder
import com.modulus.uno.SaleOrderItem
import com.modulus.uno.SaleOrderItemCommand
import com.modulus.uno.SaleOrderCommand
import com.modulus.uno.Corporate
import com.modulus.uno.CorporateService
import com.modulus.uno.CompanyService
import com.modulus.uno.CompanyStatus
import com.modulus.uno.SaleOrderStatus
import java.math.RoundingMode

@Transactional
class QuotationRequestService {

    def grailsApplication
    SaleOrderService saleOrderService
    CorporateService corporateService
    CompanyService companyService
    QuotationCommissionService quotationCommissionService

    def serviceMethod() {

    }

    QuotationRequest create(QuotationRequest quotationRequest){
      quotationRequest.save()

    }

    QuotationRequest update(QuotationRequest quotationRequest){
      quotationRequest.save()

    }

    def delete(QuotationRequest quotationRequest){
      quotationRequest.delete()
    }

    QuotationRequest requestProcessed(QuotationRequest quotationRequest){
      Map params = getParams(quotationRequest)
      SaleOrderCommand saleOrderCommand = new SaleOrderCommand(
                                                              addressId:params.addressId,
                                                              companyId:params.companyId,
                                                              clientId:params.clientId,
                                                              note:params.note,
                                                              fechaCobro:params.fechaCobro,
                                                              paymentMethod: params.paymentMethod
                                                              )
      def saleOrder = saleOrderCommand.createOrUpdateSaleOrder()
      saleOrder.status = SaleOrderStatus.AUTORIZADA
      if(saleOrder.save()){
        SaleOrderItemCommand saleOrderItemCommand = new SaleOrderItemCommand(
                                                                            sku:"FACTURA-10",
                                                                            name:quotationRequest.satConcept.getConcept(),
                                                                            quantity:"1",
                                                                            price:quotationRequest.total.toString(),
                                                                            discount:"0",
                                                                            ivaRetention:"0",
                                                                            iva:new BigDecimal(grailsApplication.config.iva).setScale(2, RoundingMode.HALF_UP),
                                                                            unitType:"UNIDAD"
                                                                            )
        def saleOrderItem  = saleOrderItemCommand.createSaleOrderItem()
        saleOrderItem.saleOrder = saleOrder
        saleOrderItem.save()
        if(saleOrderItem){
         quotationRequest.saleOrder = saleOrder
         quotationCommissionService.create(quotationRequest, quotationRequest.quotationContract.commission)
         quotationRequest.status = QuotationRequestStatus.PROCESSED
        }
      }
      else{
        log.error "Ocurrio Un error al generar la Sale Order"
      }
      quotationRequest.save()
    }

    QuotationRequest sendQuotation(QuotationRequest quotationRequest){
      quotationRequest.status = QuotationRequestStatus.SEND
      quotationRequest.save()
    }

    Map getParams(QuotationRequest quotationRequest){
      if(!quotationRequest.quotationContract.client.addresses){
        throw new QuotationException("Este cliente no tiene dirección Fiscal")
      }
      Map params= [
                  companyId:quotationRequest.biller.id,
                  clientId:quotationRequest.quotationContract.client.id,
                  addressId:quotationRequest.quotationContract.client.addresses?.first()?.id ?: 0,
                  fechaCobro: new Date().format( 'dd/MM/yyyy' ),
                  externalId:"",
                  note:"",
                  paymentMethod:"03 - TRANSFERENCIA ELECTRONICA"
                  ]
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
