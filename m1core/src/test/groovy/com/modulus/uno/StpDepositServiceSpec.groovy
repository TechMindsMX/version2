package com.modulus.uno

import spock.lang.Specification
import grails.test.mixin.TestFor
import grails.test.mixin.Mock

@TestFor(StpDepositService)
@Mock([StpDeposit, Payment])
class StpDepositServiceSpec extends Specification {

  ModulusUnoService modulusUnoService = Mock(ModulusUnoService)

  def setup() {
    service.modulusUnoService = modulusUnoService
  }

  void "Should obtain a accepted status for a stp deposit which account is from company"(){
    given:"A string notification with xml"
      String clave = "1101"
      String rastreo = "ABC001"
      String clabe = "646180132400800007"
      String notification = createNotificationWithData(clave, rastreo, clabe)
    and:
      StpDeposit.metaClass.static.findAllByOperationNumberAndTracingKeyAndIdNotEqualAndStatusNotEqual = { [] }
    and:
      ModulusUnoAccount m1Account = Mock(ModulusUnoAccount)
      m1Account.save(validate:false)
      ModulusUnoAccount.metaClass.static.findByStpClabe = { m1Account }
    and:
      ClientLink.metaClass.static.findByStpClabe = { null }
    when:"We process the notification"
      def result = service.notificationDepositFromStp(notification)
    then:"We validate"
      result.estatus == StpDepositStatus.ACEPTADO
  }

  void "Should obtain a accepted status for a stp deposit which account is from client"(){
    given:"A string notification with xml"
      String clave = "1101"
      String rastreo = "ABC001"
      String clabe = "646180132400800007"
      String notification = createNotificationWithData(clave, rastreo, clabe)
    and:
      StpDeposit.metaClass.static.findAllByOperationNumberAndTracingKeyAndIdNotEqualAndStatusNotEqual = { [] }
    and:
      ModulusUnoAccount.metaClass.static.findByStpClabe = { null }
    and:
      ClientLink client = Mock(ClientLink)
      client.save(validate:false)
      ClientLink.metaClass.static.findByStpClabe = { client }
    when:"We process the notification"
      def result = service.notificationDepositFromStp(notification)
    then:"We validate"
      result.estatus == StpDepositStatus.ACEPTADO
  }

  void "Should obtain a rejected status for a stp deposit which account is not from client and neither company"(){
    given:"A string notification with xml"
      String clave = "1101"
      String rastreo = "ABC001"
      String clabe = "646180132400800007"
      String notification = createNotificationWithData(clave, rastreo, clabe)
    and:
      StpDeposit.metaClass.static.findAllByOperationNumberAndTracingKeyAndIdNotEqualAndStatusNotEqual = { [] }
    and:
      ModulusUnoAccount.metaClass.static.findByStpClabe = { null }
    and:
      ClientLink.metaClass.static.findByStpClabe = { null }
    when:"We process the notification"
      def result = service.notificationDepositFromStp(notification)
    then:"We validate"
      result.estatus == StpDepositStatus.RECHAZADO
  }

  private String createNotificationWithData(String clave, String rastreo, String clabe) {
    String notification = """<Abono>
      <Clave>${clave}</Clave>
      <FechaOperacion>20100323</FechaOperacion>
      <InstitucionOrdenante clave="846"/>
      <InstitucionBeneficiaria clave="90646"/>
      <ClaveRastreo>${rastreo}</ClaveRastreo>
      <Monto>200</Monto>
      <NombreBeneficiario>Techminds</NombreBeneficiario>
      <TipoCuentaBeneficiario clave="40"/>
      <CuentaBeneficiario>${clabe}</CuentaBeneficiario>
      <RfcCurpBeneficiario>RFC121212ABC</RfcCurpBeneficiario>
      <ConceptoPago>prueba concepto</ConceptoPago>
      <ReferenciaNumerica>2</ReferenciaNumerica>
      <Empresa>STP</Empresa>
      </Abono>
      """
  }

}
