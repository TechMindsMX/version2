package com.modulus.uno

import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.Mock

@Mock([Address,Company])
class CompanySpec extends Specification {

  @Unroll
  void """create a company command and validate variables #bussinessName #rfc #webSite #employeeNumbers #grossAnnualBilling"""() {
    given:
      Company company = new Company()
    when:
      company.rfc = rfc
      company.bussinessName = bussinessName
      company.webSite = webSite
      company.employeeNumbers = employeeNumbers
      company.grossAnnualBilling = grossAnnualBilling
    then:
      result == company.validate()
    where:
      rfc             | bussinessName   |  webSite        | employeeNumbers | grossAnnualBilling || result
      "ROS861224NHA" | "outsourcing" | "www.yahoo.com"  | 48              | 212.23                || true
      "rODS861224NHA" | "prueba1"     | "www.h2.com"     | 30              | 1                     || false
      "RODS861224Nha" | "prueba2"     | "www.h2.com"     | 30              | 1                     || false
      "RODS12121NHA"  | "prueba3"     | "www.h2.com"     | 30              | 1                     || false
      "RODS861224NHA" | ""            | "www.h2.com"     | 30              | 1                     || false
      "RODS861224NHA" | null          | "www.h2.com"     | 30              | 1                     || false
      "RODS861224NHA" | "prueba4"     | ""               | 30              | 1                     || true
      "RODS861224NHA" | "prueba5"     | null             | 30              | 1                     || true
      "RODS121212NHA" | "prueba6"     | "www.h2.com"     | 51              | 1                     || true
      "ROS121212NHA" | "prueba7"     | "www.h2.com"     | 50              | 1                     || true
      "ROS121212NHA" | "prueba8"     | "www.h2.com"     | 50              | 250000000             || true
      "RODS121212NHA" | "prueba9"     | "www.h2.com"     | 50              | 250000000.01          || false
      "GAL121030686" | "prueba11"    | "www.h2.com"     | 50              | null                  || false
  }

}
