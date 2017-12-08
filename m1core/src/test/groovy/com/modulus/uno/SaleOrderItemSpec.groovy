package com.modulus.uno

import grails.test.mixin.TestFor

import spock.lang.Specification
import spock.lang.Unroll

@TestFor(SaleOrderItem)
class SaleOrderItemSpec extends Specification {

  @Unroll
  void """when we have an sku:#sku, name:#name, price:#price, ieps:#ieps and iva:#iva, quantity:#quantity and want to validate we expect result: #result"""() {
  given:"A item"
    def item = new SaleOrderItem()
  and:"A sale order"
    def saleOrder = new SaleOrder(rfc:'rfc', clientName:'clientName')
  when:"We set its values"
    item.sku = sku
    item.name = name
    item.price = price
    item.ivaRetention = ivaRetention
    item.iva = iva
    item.quantity = quantity
    item.unitType = unitType
    item.currencyType = currencyType
    item.saleOrder = saleOrder
    item.satKey = satKey
  then:"We validate"
    result == item.validate()
  where:"We have following values"
  sku         | name              | quantity | price | ivaRetention  | iva  | unitType          | currencyType     |  satKey || result
  'H129'      | 'Galgo System 71' | 1.0      | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || true
  'H129-1'    | 'Galgo System 72' | 20.12    | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || true
  'h129'      | 'Galgo System 73' | 1.0      | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || true
  '123'       | 'Galgo System 74' | 0        | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  'h12,'      | 'Galgo System 75' | 0        | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  'h1'        | 'Galgo System 76' | 0        | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  'h'         | 'Galgo System 77' | 0        | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  ''          | 'Galgo System 78' | 0        | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  null        | 'Galgo System 79' | 0        | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  'H129'      | 'G'               | 1.20     | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || true
  'H129'      | ''                | 0        | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  'H129'      | null              | 0        | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  'H129'      | 'Galgo System 80' | 0.23     | 0.01  | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || true
  'H129'      | 'Galgo System 81' | 0.2      | 0.00  | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || true
  'H129'      | 'Galgo System 82' | 0        | -0.01 | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  'H129'      | 'Galgo System 83' | 0        | -1    | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  'H129'      | 'Galgo System 84' | 0        | 500   | -0.01 | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  'H129'      | 'Galgo System 85' | 0        | 500   | null  | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  'H129'      | 'Galgo System 86' | 1.0      | 500   | 0     | 0.01 | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || true
  'H129'      | 'Galgo System 87' | 0.1      | 500   | 0     | 0.00 | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || true
  'H129'      | 'Galgo System 88' | 0        | 500   | 0     | -0.01| UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  'H129'      | 'Galgo System 89' | 0        | 500   | 0     | null | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  'H129'      | 'Galgo System 90' | -1       | 500   | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  | "01010101" || false
  }

  @Unroll
  void """When item has (quantity=#quantity, price=#price, discount=#discount) we expect (amountDiscount=#amountDiscount, priceWithDiscount=#priceWithDiscount, netPrice=#netPrice, netAmount=#netAmount)"""() {
  given:"A item"
    def item = new SaleOrderItem()
  and:"A sale order"
    def saleOrder = new SaleOrder(rfc:'rfc', clientName:'clientName')
  when:"We set its values"
    item.sku = sku
    item.name = name
    item.price = price
    item.discount = discount
    item.ivaRetention = ivaRetention
    item.iva = iva
    item.quantity = quantity
    item.unitType = unitType
    item.currencyType = currencyType
    item.saleOrder = saleOrder
    item.satKey = satKey
    def result = item.save()
  then:"We validate"
    result.amountDiscount == amountDiscount
    result.priceWithDiscount == priceWithDiscount
    result.netPrice == netPrice
    result.netAmount == netAmount
  where:"We have following values"
  sku    | satKey | name              | quantity | price | discount | ivaRetention  | iva  | unitType          | currencyType      || amountDiscount | priceWithDiscount | netPrice | netAmount
  'H129' | "01010101" | 'Galgo System 71' | 1.0      | 500   | 0        | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || 0              | 500               | 580      | 580
  'H129' | "01010101" | 'Galgo System 71' | 1.0      | 500   | 10       | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || 50             | 450               | 522      | 522
  'H129' | "01010101" | 'Galgo System 71' | 2.0      | 500   | 10       | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || 50             | 450               | 522      | 1044
  'H129' | "01010101" | 'Galgo System 71' | 0.5      | 500   | 10       | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || 50             | 450               | 522      | 261
  'H129' | "01010101" | 'Galgo System 71' | 1.0      | 500   | 5.5      | 0     | 16   | UnitType.UNIDADES | CurrencyType.USD  || 27.50          | 472.50            | 548.10   | 548.10
  'H129' | "01010101" | 'Galgo System 71' | 1.0      | 500   | 0        | 100   | 16   | UnitType.UNIDADES | CurrencyType.USD  || 0              | 500               | 480      | 480
  }

}
