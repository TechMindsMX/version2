package com.modulus.uno.invoice

class Concepto {
  BigDecimal cantidad = new BigDecimal(1)
  String descripcion
  String unidad
  BigDecimal valorUnitario
  BigDecimal descuento
  String claveProd
  String claveUnidad
  List<Impuesto> impuestos
  List<Impuesto> retenciones
}
