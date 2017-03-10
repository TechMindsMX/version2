package com.modulus.uno

class GenerateXMLService {

  def xmlOrderSaleRequest(Map data){
    def engine = new groovy.text.GStringTemplateEngine()
    def tenplate = new File(getClass().getClassLoader().getResource("templateSaleOrder.xml").file).text
    engine.createTemplate(tenplate).make(data).toString()
  }

}
