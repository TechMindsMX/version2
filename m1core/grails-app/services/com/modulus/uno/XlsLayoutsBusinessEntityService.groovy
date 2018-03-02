package com.modulus.uno

import pl.touk.excel.export.WebXlsxExporter
import com.modulus.uno.BusinessEntity

class XlsLayoutsBusinessEntityService {

  def businessEntityService

  def generateLayoutForCLIENTE() {
    def headers = ['PERSONA','RFC','SITIO_WEB','RAZON_SOCIAL','PATERNO','MATERNO','NOMBRE', 'CLAVE_BANCO', 'ULTIMOS_4_DIGITOS_TARJETA',  'CALLE', 'NUMEXTERIOR', 'NUMINTERIOR', 'CODIGO_POSTAL', 'COLONIA', 'DELEGACION/MUNICIPIO', 'PAIS', 'CIUDAD', 'ENTIDAD_FEDERATIVA', 'TIPO_DE_DIRECCION']
    def descriptions = ['FISICA ó MORAL', '', 'www.ejemplo.com', '', '', '', '', 'http://www.sat.gob.mx/fichas_tematicas/buzon_tributario/documents/catalogo_bancos.pdf', '####', '', '', '', '', '', '', '', '', '', 'FISCAL ó SOCIAL ó COMERCIAL']
    new WebXlsxExporter().with {
      fillRow(headers, 0)
      fillRow(descriptions, 1)
    }
  }

  def generateLayoutForCLIENTE_PROVEEDOR() {
    def headers = ['PERSONA','RFC','SITIO_WEB','RAZON_SOCIAL','PATERNO','MATERNO','NOMBRE', 'CLABE', 'CALLE', 'NUMEXTERIOR', 'NUMINTERIOR', 'CODIGO_POSTAL', 'COLONIA', 'DELEGACION/MUNICIPIO', 'PAIS', 'CIUDAD', 'ENTIDAD_FEDERATIVA', 'TIPO_DE_DIRECCION']
    def descriptions = ['FISICA ó MORAL', '', 'www.ejemplo.com', '', '', '', '', '18 dígitos', '', '', '', '', '', '', '', '', '', 'FISCAL ó SOCIAL ó COMERCIAL']
    new WebXlsxExporter().with {
      fillRow(headers, 0)
      fillRow(descriptions, 1)    
    }
  }

  def generateLayoutForPROVEEDOR() {
    def headers = ['PERSONA','RFC','SITIO_WEB','RAZON_SOCIAL','PATERNO','MATERNO','NOMBRE', 'CLABE', 'CALLE', 'NUMEXTERIOR', 'NUMINTERIOR', 'CODIGO_POSTAL', 'COLONIA', 'DELEGACION/MUNICIPIO', 'PAIS', 'CIUDAD', 'ENTIDAD_FEDERATIVA', 'TIPO_DE_DIRECCION']
    def descriptions = ['FISICA ó MORAL', '', 'www.ejemplo.com', '', '', '', '', '18 dígitos', '', '', '', '', '', '', '', '', '', 'FISCAL ó SOCIAL ó COMERCIAL']
    new WebXlsxExporter().with {
      fillRow(headers, 0)
      fillRow(descriptions, 1)  
    }
  }

  def generateLayoutForEMPLEADO() {
    def headers = ['RFC','CURP','PATERNO','MATERNO','NOMBRE','NUM_EMPL','BANCO','CLABE','CUENTA','SUCURSAL','NUMTARJETA',"IMSS","NSS","FECHA_ALTA", "SA_BRUTO", "NETO", "PRIMA_VAC", "DIAS_AGUINALDO", "PERIODO_PAGO"]
    def descriptions = ['Reemplazar esta fila', '', '', '', '', 'No. de Empleado', 'Código del Banco', '18 dígitos','11 dígitos máximo','3 dígitos', '16 dígitos', "S ó N (Si usa N ningún valor de los siguietes se tomará en cuenta)","", "dd-MM-yyyy", "Salario SA Bruto mensual", "Salario Total Neto mensual", "En porcentaje", "", "Semanal, Catorcenal, Quincenal, Mensual"]
    new WebXlsxExporter().with {
      fillRow(headers, 0)
      fillRow(descriptions, 1)
    }
  }

  WebXlsxExporter exportListOfBusinessEntities(List<BusinessEntity> businessEntityList){
    def businessEntities = getBusinessToExport(businessEntityList)
    new WebXlsxExporter().with{
      fillRow(businessEntities.headers,0)
      add(businessEntities.data, businessEntities.properties,1)
    }
  }

  Map getBusinessToExport(List<BusinessEntity> businessEntityList) {
    Map businessEntities = [:]
    businessEntities.headers = ['RFC', 'NOMBRE/RAZON_SOCIAL', 'SITIO_WEB', 'PERSONA', 'TIPO_DE_RELACIÓN', 'ESTATUS']
    businessEntities.properties = ['rfc', 'name', 'website', 'type', 'businessEntityType', 'status']
    businessEntities.data = getMapBusinessEntity(businessEntityList)
    businessEntities
  }

  List<Map> getMapBusinessEntity(List<BusinessEntity> businessEntityList){
    List<Map> businessEntitiesList = []
    businessEntityList.each{ business ->
      Map businessEntities = [
        rfc: business.rfc,
        name: business.toString() ?: "Sin Nombre",
        website: business?.website,
        type: business.type,
        businessEntityType: business.getBusinessEntityType(),
        status: business.status.getDescription()
      ]
      businessEntitiesList << businessEntities
    }
    businessEntitiesList
  }

}
