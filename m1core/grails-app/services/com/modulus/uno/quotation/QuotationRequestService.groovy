package com.modulus.uno.quotation

import grails.transaction.Transactional
import com.modulus.uno.SaleOrderService
import com.modulus.uno.PaymentMethod
import com.modulus.uno.SaleOrder

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
      def sale = saleOrderServise.createSaleOrderWithAddress(params)
      println sale
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
