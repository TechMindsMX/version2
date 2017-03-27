package com.modulus.uno

class GenerateXMLService {

  def xmlPayOrderRequest(Map data){
    def payOrder = new PayOrderSTP(data)
    if (!payOrder.validate()){
      log.error "Datos Incorrectos para el XML"
      log.error payOrder.errors.toString()
      throw new XMLException("Datos erroneos para la generacion del xml")
    }
    def engine = new groovy.text.GStringTemplateEngine()
    def tenplate = new File(getClass().getClassLoader().getResource("templatePayOrder.xml").file).text
    engine.createTemplate(tenplate).make(payOrder.asMap()).toString()
  }

}
