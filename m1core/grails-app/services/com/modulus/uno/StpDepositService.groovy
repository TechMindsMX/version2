package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class StpDepositService {

  TransactionService transactionService

  def notificationDepositFromStp(StpDeposit stpDeposit) {
    stpDeposit = saveNotification(stpDeposit)
    StpDepositStatus status = defineStpDepositStatus(stpDeposit)
    if (status == StpDepositStatus.ACEPTADO) {
      processStpDeposit(stpDeposit)
    }
    [clave:stpDeposit.operationNumber, rastreo:stpDeposit.tracingKey, estatus:status]
  }

  private def processStpDeposit(StpDeposit stpDeposit) {
    ModulusUnoAccount m1Account = ModulusUnoAccount.findByStpClabe(stpDeposit.accountBeneficiary)
    ClientLink client = ClientLink.findByStpClabe(stpDeposit.accountBeneficiary)

    log.info "Accoun company: ${m1Account}"
    log.info "Account client: ${client}"

    stpDeposit.status = StpDepositStatus.APLICADO
    stpDeposit.save()
    
    generatePaymentToConciliateBill(m1Account, client, stpDeposit)
  }

  private def generatePaymentToConciliateBill(ModulusUnoAccount m1Account, ClientLink client, StpDeposit stpDeposit) {
    Payment payment = new Payment(amount:stpDeposit.amount)
    payment.rfc = client ? client.clientRef : null
    payment.company = m1Account ? m1Account.company : client.company
    payment.transaction = createAndSaveTransaction(stpDeposit)
    payment.save()
    log.info "Payment was generated: ${payment?.dump()}"
  }

  private StpDeposit saveNotification(StpDeposit stpDeposit) {
    log.info "Recording deposit: ${stpDeposit.dump()}"
    stpDeposit.save()
    stpDeposit
  }

  private StpDepositStatus defineStpDepositStatus(StpDeposit stpDeposit) {
    def status = StpDepositStatus.RECHAZADO
    //checar si la notificación ya ha sido enviada anteriormente
    def stpDeposits = StpDeposit.findAllByOperationNumberAndTracingKeyAndIdNotEqualAndStatusNotEqual(stpDeposit.operationNumber, stpDeposit.tracingKey, stpDeposit.id, StpDepositStatus.RECIBIDO)
    if (stpDeposits) {
      StpDeposit lastDeposit = stpDeposits.sort { it.dateCreated }.last()
      status = lastDeposit.status
    } else {
      //validar si cuenta es de company o client
      ModulusUnoAccount account = ModulusUnoAccount.findByStpClabe(stpDeposit.accountBeneficiary)
      ClientLink client = ClientLink.findByStpClabe(stpDeposit.accountBeneficiary)
      if (account || client) {
        status = StpDepositStatus.ACEPTADO
      }
    }
    stpDeposit.status = status
    stpDeposit.save()
    log.info "Defined status:${stpDeposit.status}"
    status
  }

  private Transaction createAndSaveTransaction(StpDeposit stpDeposit){
    String keyAccountCompany = "${stpDeposit.accountBeneficiary}".substring(0,13)
    ModulusUnoAccount modulusUnoAccount = ModulusUnoAccount.findByStpClabeLike("${keyAccountCompany}%")
    Map parameters = [keyTransaction:"${stpDeposit.operationNumber}",
                      trackingKey:stpDeposit.tracingKey,
                      amount:stpDeposit.amount,
                      paymentConcept:stpDeposit.paymentConcept,
                      keyAccount:modulusUnoAccount.stpClabe,
                      referenceNumber:"${stpDeposit.numericalReference}",
                      transactionType:TransactionType.DEPOSIT,
                      transactionStatus:TransactionStatus.AUTHORIZED]
    transactionService.saveTransaction(new Transaction(parameters))
  }

}
