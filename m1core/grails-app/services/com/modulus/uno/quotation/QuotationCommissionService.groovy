package com.modulus.uno.quotation

import grails.transaction.Transactional
import com.modulus.uno.PaymentMethod
import com.modulus.uno.saleorder.SaleOrderService
import com.modulus.uno.saleorder.SaleOrder
import com.modulus.uno.saleorder.SaleOrderItem
import com.modulus.uno.saleorder.SaleOrderItemCommand
import com.modulus.uno.saleorder.SaleOrderCommand
import com.modulus.uno.Corporate
import com.modulus.uno.CorporateService
import com.modulus.uno.CompanyService
import com.modulus.uno.CompanyStatus
import com.modulus.uno.status.SaleOrderStatus
import java.math.RoundingMode

@Transactional
class QuotationCommissionService {

 
    QuotationCommission create(QuotationRequest quotationRequest, def commission){
        new QuotationCommission(
            quotationRequest: quotationRequest,
            amount: quotationRequest.subtotal,
            commissionApply: commission
        ).save()
    }

}
