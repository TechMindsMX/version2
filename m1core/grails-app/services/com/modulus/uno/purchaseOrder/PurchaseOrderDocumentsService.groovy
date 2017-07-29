package com.modulus.uno.purchaseOrder

import com.modulus.uno.PurchaseOrder
import com.modulus.uno.BusinessException
import com.modulus.uno.utils.TempFileService

class PurchaseOrderDocumentsService {

  TempFileService tempFileService

  def validateDocumentXmlForOrder(PurchaseOrder order, def document) {
    if (isXmlDocument(document)) {
      validateRfc(document, order.company.rfc)
    }
  }

  boolean isXmlDocument(def document) {
    document.filename.tokenize('.').last().toLowerCase() == "xml"
  }

  def parseXmlFileToProcess(def document, def cfdiNs) {
    File xmlFile = getXmlFileToProcess(document)
    log.info "Text xmlFile: ${xmlFile.text}"
    new XmlParser().parse(xmlFile)
  }

  File getXmlFileToProcess(def document) {
    File xmlFile = tempFileService.getTempFileFromMultipart(document, "xml")
  }

  def validateRfc(def document, String rfcCompany) {
    def cfdiNs = new groovy.xml.Namespace("http://www.sat.gob.mx/cfd/3", "cfdi")
    def xmlParsed = parseXmlFileToProcess(document, cfdiNs)
    if (xmlParsed[cfdiNs.Receptor].'@rfc'==[] || xmlParsed[cfdiNs.Receptor].'@rfc'?.first() != rfcCompany) {
      throw new BusinessException("El XML no corresponde al RFC de la empresa")
    }
  }

  def loadItemsToOrderFromDocumentXml(PurchaseOrder order, def document) {
    if (isXmlDocument(document) && !order.isAnticipated && !order.items) {
      log.debug "Load items"
      def cfdiNs = new groovy.xml.Namespace("http://www.sat.gob.mx/cfd/3", "cfdi")
      def xmlParsed = parseXmlFileToProcess(document, cfdiNs)
      log.debug "Concepts from xml: ${xmlParsed[cfdiNs.Conceptos][cfdiNs.Concepto].size()}"
      //parsear el xml
      //validar el rfc del emisor y receptor
      //generar los items
    }
  }


}
