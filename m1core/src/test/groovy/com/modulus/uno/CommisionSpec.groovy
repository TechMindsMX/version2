package com.modulus.uno

import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(Commission)
@Mock([Company])
class CommissionSpec extends Specification {

  @Unroll
  void """When we have a commission with fee: #fee, percentage: #percentage, type:#type when we validate we expect result is : #result"""(){
    given:"A commission"
      def commission = new Commission()
    and:"A Company"
      def company = new Company(rfc:'XXX010101XXX',employeeNumbers:5,grossAnnualBilling:1000000,numberOfAuthorizations:1).save(validate:false)
    when:"It's data"
      commission.fee = fee
      commission.percentage = percentage
      commission.type = type
      commission.company = company
    then:"We validate"
      result == commission.validate()
    where:"We have the following values"
    fee   | percentage | type                    || result
    100   | 0          | CommissionType.VENTA    || true
    100   | null       | CommissionType.VENTA    || true
    null  | 10         | CommissionType.VENTA    || true
    0     | 10         | CommissionType.VENTA    || true
    0     | 0          | CommissionType.VENTA    || true
    null  | null       | CommissionType.VENTA    || true
    100   | 0          | CommissionType.DEPOSITO || true
    100   | 0          | CommissionType.PRESTAMO || true
    100   | 0          | null                    || false
    100   | -1         | CommissionType.VENTA    || false
    100   | 101        | CommissionType.VENTA    || false
    -1    | 0          | CommissionType.VENTA    || false
    11    | 12         | CommissionType.VENTA    || false
  }

}
