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
class QuotationCommissionService {

 
    QuotationCommission create(QuotationRequest quotationRequest, def commission){
        new QuotationCommission(
            quotationRequest: quotationRequest,
            amount: quotationRequest.subtotal,
            commissionApply: commission
        ).save()
    }

}