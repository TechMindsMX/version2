package com.modulus.uno

enum CauseRefundStp {
  DONT_SPECIFIED(0),
  ACCOUNT_NOT_EXISTS(1), //Cuenta inexistente
  BLOCKED_ACCOUNT(2), //Cuenta bloqueada
  CANCELED_ACCOUNT(3), //Cuenta cancelada
  DISTINCT_CURRENCY(5), //Cuenta en otra divisa
  DISTINCT_BANK(6), //Cuenta no pertenece al banco receptor
  UNKNOWN_PAYMENT(13), //Beneficiario no reconoce el pago
  MISSING_DATA(14), //Falta información mandataria para completar el pago
  ERROR_PAYMENT_TYPE(15), //Tipo de pago erróneo
  ERROR_OPERATION_TYPE(16), //Tipo de operación errónea
  ERROR_ACCOUNT_TYPE(17), //Tipo de cuenta no corresponde
  REQUEST_EMITTER(18), //A solicitud del emisor
  INVALID_CHARACTER(19), //Carácter inválido
  BALANCE_EXCEEDED(20), //Excede el límite de saldo autorizado de la cuenta
  DEPOSITS_EXCEEDED(21), //Excede el límite de abonos permitidos en el mes en la cuenta
  UNKNOWN_PHONE(22), //Número de línea de telefonía móvil no registrado
  UNSUPPORTED_PAYMENT_IN_ADITIONAL_ACCOUNT(23), //Cuenta adicional no recibe pagos que no proceden de Banxico
  UNKNOWN_CAUSE(999)

  CauseRefundStp(int id) {
    this.id = id
  }

  private final int id

  int getId() {
    id
  }

  public String toString() {
    return name()
  }
}
