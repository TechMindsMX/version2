package com.modulus.uno

import grails.test.mixin.TestFor

import spock.lang.Specification
import spock.lang.Unroll

@TestFor(Product)
class ProductSpec extends Specification {

  @Unroll
  void """when we have an sku:#sku, name:#name, price:#price, ieps:#ieps and iva:#iva and want to validate we expect result: #result"""() {
  given:"A product"
    def product = new Product()
  and:"A company"
    def company = new Company()
  when:"We set its values"
    product.sku = sku
    product.name = name
    product.price = price
    product.ieps = ieps
    product.iva = iva
    product.unitType = unitType
    product.currencyType = currencyType
    product.company = company
  then:"We validate"
    result == product.validate()
  where:"We have following values"
  sku         | name              | price | ieps  | iva  | unitType          | currencyType      || result
  'H129'      | 'Galgo System 76' | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || true
  'H129-1'    | 'Galgo System 76' | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || true
  'h129'      | 'Galgo System 76' | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || true
  '123'       | 'Galgo System 76' | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || false
  'h12,'      | 'Galgo System 76' | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || false
  'h1'        | 'Galgo System 76' | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || false
  'h'         | 'Galgo System 76' | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || false
  ''          | 'Galgo System 76' | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || false
  null        | 'Galgo System 76' | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || false
  'H129'      | 'G'               | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || true
  'H129'      | ''                | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || false
  'H129'      | null              | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || false
  'H129'      | 'Galgo System 76' | 0.01  | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || true
  'H129'      | 'Galgo System 76' | 0.00  | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || true
  'H129'      | 'Galgo System 76' | -0.01 | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || false
  'H129'      | 'Galgo System 76' | -1    | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || false
  'H129'      | 'Galgo System 76' | 500   | -0.01 | 16   | UnitType.UNIDADES | CurrencyType.USD  || false
  'H129'      | 'Galgo System 76' | 500   | null  | 16   | UnitType.UNIDADES | CurrencyType.USD  || false
  'H129'      | 'Galgo System 76' | 500   | 0     | 0.01 | UnitType.UNIDADES | CurrencyType.USD  || true
  'H129'      | 'Galgo System 76' | 500   | 0     | 0.00 | UnitType.UNIDADES | CurrencyType.USD  || true
  'H129'      | 'Galgo System 76' | 500   | 0     | -0.01| UnitType.UNIDADES | CurrencyType.USD  || false
  'H129'      | 'Galgo System 76' | 500   | 0     | null | UnitType.UNIDADES | CurrencyType.USD  || false
  }

}
