package com.modulus.uno.quotation

import grails.transaction.Transactional
import com.modulus.uno.SaleOrderService
import com.modulus.uno.PaymentMethod
import com.modulus.uno.SaleOrder
import com.modulus.uno.SaleOrderItem

@Transactional
class QuotationRequestService {

    SaleOrderService saleOrderServise

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
      println params.dump()
      def saleOrder = saleOrderServise.createSaleOrderWithAddress(params)
      if(!saleOrder){
        log.error "No se creo la Sale Order"
      }
      else{
        def saleOrderItem = new SaleOrderItem(
                                             sku:"A98GB",
                                             name: quotationRequest.satConcet.getConcetp(),
                                             quantity:1,
                                             price: quotationRequest.amount,
                                             discount:0,
                                             ivaRetention: 0,
                                             iva: 16,
                                             unitType:"UNIDADES",
                                             saleOerder:saleOerder
                                             )
      }
      quotationRequest.status = QuotationRequestStatus.PROCESSED
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
                  addressId:quotationRequest.quotationContract.client.addresses?.id ?: 2,
                  fechaCobro: new Date(),
                  externalId:"",
                  note:"",
                  paymentMethod:PaymentMethod.EFECTIVO
                  ]
    }
}
