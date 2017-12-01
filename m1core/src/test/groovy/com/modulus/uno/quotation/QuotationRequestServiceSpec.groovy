package com.modulus.uno.quotation

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock
import org.springframework.mock.web.MockHttpServletRequest
import com.modulus.uno.SaleOrderService
import com.modulus.uno.SaleOrder
import com.modulus.uno.BusinessEntity
import com.modulus.uno.Company
import com.modulus.uno.BusinessEntityType
import com.modulus.uno.Address
import com.modulus.uno.Address
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(QuotationRequestService)
@Mock([Company, SaleOrder, BusinessEntity, QuotationContract, QuotationRequest, Address])
class QuotationRequestServiceSpec extends Specification {

    def items = []
    SaleOrderService saleOrderService = Mock(SaleOrderService)

    def setup() {
      service.saleOrderService = saleOrderService
    }

    def cleanup() {
    }

    

    
}









