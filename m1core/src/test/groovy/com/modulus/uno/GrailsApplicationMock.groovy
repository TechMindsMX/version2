class GrailsApplicationMock  {
  def template = [register:'register',newUser:'newUser',forgot:'forgot',clientProvider:'clientProvider',authorizeSaleOrder:'authorizeSaleOrder']
  def modulus = [users:'users',cashin: 'cashin', fee:'fee',loanCreate:'loanCreate', cashout:'cashout', integratorTransactions:'integratorTransactions']
  def page = [register:'register',forgot:'forgot']
  def last = [url:'http://localhost:8080/']
  def saleOrder = [serie:'B1']
  def datosDeFacturacion = [razonSocial:'Integradora de Emprendimientos Culturales S.A. de C.V.'
  ,rfc:'AAD990814BP7'
  ,codigoPostal:'11850'
  ,pais:'México'
  ,ciudad:'Ciudad de México'
  ,delegacion:'Miguel Hidalgo'
  ,calle:'Tiburcio Montiel'
  ,colonia:'San Miguel Chapultepec'
  ,noExterior:'80'
  ,noInterior:'B3'
  ,regimen:'Régimen General de Ley Personal Morales']
  def path = [server:last]
  def config = [emailer:template, recovery:page, modulus:modulus, grails:path, iva:16, speiFee:8.50, folio:saleOrder, factura: datosDeFacturacion]

}
