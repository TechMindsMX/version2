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
  def plugin = [awssdk:[domain:[base:[url:"-qa.modulusuno.com"]]]]
  def path = [server:last, plugin:plugin]
  def m1emitter = [rfc:"AAA010101AAA", businessName:"TECHMINDS", stpClabe:"646180132400000007", address:[street:'Calzada Las Águilas', streetNumber:'2370', suite:'', zipCode:'01820', colony:'Lomas de Axomiatla', neighboorhood:'', country:'México', city:'Ciudad de México', town:'Álvaro Obregón', federalEntity:'Ciudad de México']]
  def config = [emailer:template, recovery:page, modulus:modulus, grails:path, iva:16, speiFee:8.50, folio:saleOrder, factura: datosDeFacturacion, m1emitter:m1emitter]

}
