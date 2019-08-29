package com.modulus.uno

class FormatsTagLib {


  static namespace = "modulusuno"
  static defaultEncodeAs = [taglib:'html']

  def formatPrice = { attrs, body ->
    out << g.formatNumber(number:attrs.number, type:"currency", maxFractionDigits:attrs.decimals?:"2", locale:"es_MX")
  }

  def formatQuantity = { attrs, body ->
    out << g.formatNumber(number:attrs.number, format:"#,##0.00", maxFractionDigits:attrs.decimals?:"4", locale:"es_MX")
  }

  def dateFormat = { attrs, body ->
    out << g.formatDate(date:attrs.date, format:"dd/MM/yyyy", locale:"es_MX")
  }

  def quantityWithoutComma = { attrs, body ->
    out << g.formatNumber(number:attrs.number, format:"#0.00", maxFractionDigits:attrs.decimals?:"4", locale:"es_MX")
  }

}
