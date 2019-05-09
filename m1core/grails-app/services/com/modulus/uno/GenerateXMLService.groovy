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

  def xmlSignedConciliationRequest(Map data){
    def engine = new groovy.text.GStringTemplateEngine()
    def xmlSignedConciliationTemplate = new File(getClass().getClassLoader().getResource("templateSignedConciliation.xml").file).text
    engine.createTemplate(xmlSignedConciliationTemplate).make(data).toString()
  }

  def xmlPayOrderReplacementInvoice(Map data){
    def payOrder = new PayOrderSTP(data)
    if (!payOrder.validate() || !payOrder.uuidReplacement){
      log.error "Datos Incorrectos para el XML"
      log.error payOrder.errors.toString()
      throw new XMLException("Datos erroneos para la generacion del xml")
    }
    def engine = new groovy.text.GStringTemplateEngine()
    def template = new File(getClass().getClassLoader().getResource("templatePayOrderReplacement.xml").file).text
    engine.createTemplate(template).make(payOrder.asMap()).toString()
  }

}
