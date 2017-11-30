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
import com.modulus.uno.User
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(QuotationRequestService)
@Mock([Company, SaleOrder, BusinessEntity, QuotationContract, QuotationRequest, Address, User])
class QuotationRequestServiceSpec extends Specification {

    def items = []
    SaleOrderService saleOrderService = Mock(SaleOrderService)

    def setup() {
      service.saleOrderService = saleOrderService
    }

    def cleanup() {
    }

    void "Get list of clients from the current user"(){
      given:"List of quotation contract list"
        def quotationContractList = generateSomeQuotationContractList()

      and:"user"
        User user1 = generateSomeUser()

      when:"Pass the current user from the session to new list"
        List<QuotationContract> listOfCurrentUsers = service.getListOfClientsFromTheCurrentUser(quotationContractList, user1)

      then:
        listOfCurrentUsers.size() == 1
        listOfCurrentUsers*.client.size() == 1
        listOfCurrentUsers[0].comission == 2.00

    }

    private List<QuotationContract> generateSomeQuotationContractList(){
          List<QuotationContract> quotationContractList = []
          QuotationContract quotationContract = new QuotationContract(commission)
          quotationContract.user.add(user1).save(validate:false)
          QuotationContract quotationContract2 = new QuotationContract()
          quotationContract2.user.add(user2).save(validate:false)
        quotationContractList << quotationContract
        quotationContractList << quotationContract2
        quotationContractList
    }

    private User generateSomeUser(){
      User user = new User().save(validate:false)
      user
    }
}









