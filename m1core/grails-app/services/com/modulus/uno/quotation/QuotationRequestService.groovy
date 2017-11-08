package com.modulus.uno.quotation

import grails.transaction.Transactional
import com.modulus.uno.SaleOrderService
import com.modulus.uno.PaymentMethod
import com.modulus.uno.SaleOrder
import com.modulus.uno.SaleOrderItem
import com.modulus.uno.SaleOrderItemCommand
import com.modulus.uno.SaleOrderCommand

@Transactional
class QuotationRequestService {

    SaleOrderService saleOrderService

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
      if(saleOrder.save()){
        SaleOrderItemCommand saleOrderItemCommand = new SaleOrderItemCommand(
                                                                            sku:"FACTURA-10",
                                                                            name:quotationRequest.satConcept.getConcept(),
                                                                            quantity:"1",
                                                                            price:quotationRequest.amount.toString(),
                                                                            discount:"0",
                                                                            ivaRetention:"0",
                                                                            iva:"16",
                                                                            unitType:"UNIDAD"
                                                                            )
        def saleOrderItem  = saleOrderItemCommand.createSaleOrderItem()
        saleOrderItem.saleOrder = saleOrder
        saleOrderItem.save()
        if(saleOrderItem){
         quotationRequest.saleOrder = saleOrder
         quotationRequest.status = QuotationRequestStatus.PROCESSED
        }
      }
      else{
        log.erro "Ocurrio Un error al generar la Sale Order"
      }
      quotationRequest.save()
    }

    QuotationRequest sendQuotation(QuotationRequest quotationRequest){
      quotationRequest.status = QuotationRequestStatus.SEND
      quotationRequest.save()
    }

    Map getParams(QuotationRequest quotationRequest){
      Map params= [
                  companyId:quotationRequest.quotationContract.company.id,
                  clientId:quotationRequest.quotationContract.client.id,
                  addressId:quotationRequest.quotationContract.client.addresses?.first()?.id ?: 0,
                  fechaCobro: new Date().format( 'dd/MM/yyyy' ),
                  externalId:"",
                  note:"",
                  paymentMethod:"03 - TRANSFERENCIA ELECTRONICA"
                  ]
    }
}
