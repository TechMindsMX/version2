package com.modulus.uno

class GenerateXMLService {

  def xmlOrderSaleRequest(Map data){
    def saleOrder = new SaleOrderSTP(data)
    if (!saleOrder.validate()){
      log.error "Datos Incorrectos para el XML"
      log.error saleOrder.errors.toString()
      throw new XMLException("Datos erroneos para la generacion del xml")
    }
    def engine = new groovy.text.GStringTemplateEngine()
    def tenplate = new File(getClass().getClassLoader().getResource("templateSaleOrder.xml").file).text
    engine.createTemplate(tenplate).make(saleOrder.asMap()).toString()
  }

}
