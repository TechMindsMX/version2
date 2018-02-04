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
		xmlFile.text = xmlFile.text.toLowerCase()
    log.info "Text xmlFile: ${xmlFile.text}"
    new XmlParser().parse(xmlFile)
  }

  File getXmlFileToProcess(def document) {
    File xmlFile = tempFileService.getTempFileFromMultipart(document, "xml")
  }

  def validateRfc(def document, String rfcCompany) {
    def cfdiNs = new groovy.xml.Namespace("http://www.sat.gob.mx/cfd/3", "cfdi")
    def xmlParsed = parseXmlFileToProcess(document, cfdiNs)
    if (xmlParsed[cfdiNs.receptor].'@rfc'==[] || xmlParsed[cfdiNs.receptor].'@rfc'?.first().toUpperCase() != rfcCompany) {
      throw new BusinessException("El RFC del XML (${xmlParsed[cfdiNs.receptor].'@rfc'?.first().toUpperCase()}) no corresponde al RFC de la empresa (${rfcCompany})")
    }
    true
  }

  def loadItemsToOrderFromDocumentXml(PurchaseOrder order, def document) {
    if (isXmlDocument(document)) {
      purchaseOrderItemService.deleteCurrentItemsFromPurchaseOrder(order)
      def cfdiNs = new groovy.xml.Namespace("http://www.sat.gob.mx/cfd/3", "cfdi")
      def xmlParsed = parseXmlFileToProcess(document, cfdiNs)
			log.info "Version Xml: ${xmlParsed.'@version'}"
      def itemsXml = xmlParsed[cfdiNs.conceptos][cfdiNs.concepto]
      def taxes = xmlParsed[cfdiNs.impuestos][cfdiNs.traslados][cfdiNs.traslado]
      def retentions = xmlParsed[cfdiNs.impuestos][cfdiNs.retenciones][cfdiNs.retencion]
      if (xmlParsed.'@version'=="3.3") {
        taxes = xmlParsed[cfdiNs.conceptos][cfdiNs.concepto][cfdiNs.impuestos][cfdiNs.traslados][cfdiNs.traslado]
        retentions = xmlParsed[cfdiNs.conceptos][cfdiNs.concepto][cfdiNs.impuestos][cfdiNs.retenciones][cfdiNs.retencion]
      }
      itemsXml.eachWithIndex { xmlItem, index ->
        def taxItem = taxes != [] ? taxes[index] : ""
        def retentionItem = retentions != [] ? retentions[index] : ""
				Map xmlDataItem = [xmlItem:xmlItem, taxItem:taxItem, retentionItem:retentionItem, version:xmlParsed.'@version']
        createOrderItemFromXmlItem(order, xmlDataItem)
      }
    }
    order
  }

  def createOrderItemFromXmlItem(PurchaseOrder order, Map xmlDataItem) {
    PurchaseOrderItem poItem = new PurchaseOrderItem(
      name:xmlDataItem.xmlItem.'@descripcion'.toUpperCase(),
      quantity:new BigDecimal(xmlDataItem.xmlItem.'@cantidad'),
      price:new BigDecimal(xmlDataItem.xmlItem.'@valorunitario'),
      discount: calculateDiscountForItem(xmlDataItem.xmlItem),
      iva:xmlDataItem.taxItem ? (xmlDataItem.version=="3.3" ? new BigDecimal(xmlDataItem.taxItem.'@tasaocuota')*100 : new BigDecimal(xmlDataItem.taxItem.'@tasa')) : new BigDecimal(0),
      ivaRetention:xmlDataItem.retentionItem ? new BigDecimal(xmlDataItem.retentionItem.'@importe') : new BigDecimal(0),
      unitType:xmlDataItem.xmlItem.'@unidad'.toUpperCase(),
      purchaseOrder:order
    )
    log.info "Item to save: ${poItem.dump()}"
    poItem.save()
  }

  BigDecimal calculateDiscountForItem(xmlItem) {
    if (!xmlItem.'@descuento') {
      return new BigDecimal(0)
    }
    
    BigDecimal discount = new BigDecimal(xmlItem.'@descuento')
    BigDecimal subtotal = new BigDecimal(xmlItem.'@importe')
    discount / subtotal * 100
  }
}
