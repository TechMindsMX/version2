package com.modulus.uno
import grails.validation.Validateable

class SalesCaptureArticleCommand implements Validateable {
  String name
  String description
  Integer quantity
  BigDecimal price
  Integer tax
  BigDecimal amount
}
