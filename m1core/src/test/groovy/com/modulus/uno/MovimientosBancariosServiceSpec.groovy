package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Ignore
import org.springframework.mock.web.MockHttpServletRequest
import grails.web.servlet.mvc.GrailsParameterMap

@TestFor(MovimientosBancariosService)
@Mock([MovimientosBancarios,BankAccount])
class MovimientosBancariosServiceSpec extends Specification {

    def setup() {
    }

    void "verify if list is valid for process"() {
      given:
        def list = [fecha, concepto, referencia, debito, credito]
      when:
        def result = service.isValidList(list)
      then:
        result == response
       where:
        fecha         | concepto    | referencia | debito  | credito || response
        "12/12/2016"  | "prueba 1"  |  ""        |  ""     | "120"   || true
          ""          |  ""         |  ""        |  ""     | ""      || false


    }

    @Ignore
    void "verify if list contains elements"() {
      given: "create list of elements"
        def list = [fecha, concepto, referencia, debito, credito]
      and:
        BankAccount account = new BankAccount().save(validate:false)
      when:
        def result = service.createMovimeintosBancariosFromList(list, account)
      then:
        result.id == id
        result.type == type
      where:
        fecha         | concepto    | referencia | debito   | credito || id  | type
        "12/12/2016"  | "prueba 1"  |  ""        |  ""      | "120"   || 1   | MovimientoBancarioType.CREDITO
        "12/12/2016"  | "prueba 2"  | "12345"    | "330.50" | ""      || 1   | MovimientoBancarioType.DEBITO
    }

}
