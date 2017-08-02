package com.modulus.uno.purchaseOrder

import com.modulus.uno.PurchaseOrder
import com.modulus.uno.PurchaseOrderItem
import com.modulus.uno.BusinessException
import com.modulus.uno.utils.TempFileService

class PurchaseOrderDocumentsService {

  TempFileService tempFileService
  PurchaseOrderItemService purchaseOrderItemService

  def validateDocumentXmlForOrder(PurchaseOrder order, def document) {
    if (isXmlDocument(document)) {
      validateRfc(document, order.company.rfc)
    }
  }

  boolean isXmlDocument(def document) {
    document.originalFilename.tokenize('.').last().toLowerCase() == "xml"
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
    true
  }

  def loadItemsToOrderFromDocumentXml(PurchaseOrder order, def document) {
    if (isXmlDocument(document)) {
      purchaseOrderItemService.deleteCurrentItemsFromPurchaseOrder(order)
      def cfdiNs = new groovy.xml.Namespace("http://www.sat.gob.mx/cfd/3", "cfdi")
      def xmlParsed = parseXmlFileToProcess(document, cfdiNs)
      def itemsXml = xmlParsed[cfdiNs.Conceptos][cfdiNs.Concepto]
      def taxes = xmlParsed[cfdiNs.Impuestos][cfdiNs.Traslados][cfdiNs.Traslado]
      def retentions = xmlParsed[cfdiNs.Impuestos][cfdiNs.Retenciones][cfdiNs.Retencion]
      itemsXml.eachWithIndex { xmlItem, index ->
        def taxItem = taxes != [] ? taxes[index] : ""
        createOrderItemFromXmlItem(order, xmlItem, taxItem)
      }
    }
    order
  }

  def createOrderItemFromXmlItem(PurchaseOrder order, def item, def taxItem) {
    PurchaseOrderItem poItem = new PurchaseOrderItem(
      name:item.'@descripcion',
      quantity:new BigDecimal(item.'@cantidad'),
      price:new BigDecimal(item.'@valorUnitario'),
      iva:taxItem ? new BigDecimal(taxItem.'@tasa'): new BigDecimal(0),
      unitType:item.'@unidad',
      purchaseOrder:order
    )
    poItem.save()
  }

}
