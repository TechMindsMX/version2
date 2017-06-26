package com.modulus.uno

enum CauseRefundStp {
  DONT_SPECIFIED(0),
  ACCOUNT_NOT_EXISTS(1), //Cuenta inexistente
  BLOCKED_ACCOUNT(2), //Cuenta bloqueada
  CANCELED_ACCOUNT(3), //Cuenta cancelada
  DISTINCT_CURRENCY(4), //Cuenta en otra divisa
  DISTINCT_BANK(5), //Cuenta no pertenece al banco receptor
  UNKNOWN_PAYMENT(6), //Beneficiario no reconoce el pago
  MISSING_DATA(7), //Falta información mandataria para completar el pago
  ERROR_PAYMENT_TYPE(8), //Tipo de pago erróneo
  ERROR_OPERATION_TYPE(9), //Tipo de operación errónea
  ERROR_ACCOUNT_TYPE(10), //Tipo de cuenta no corresponde
  REQUEST_EMITTER(11), //A solicitud del emisor
  INVALID_CHARACTER(12), //Carácter inválido
  BALANCE_EXCEEDED(13), //Excede el límite de saldo autorizado de la cuenta
  DEPOSITS_EXCEEDED(14), //Excede el límite de abonos permitidos en el mes en la cuenta
  UNKNOWN_PHONE(15), //Número de línea de telefonía móvil no registrado
  UNSUPPORTED_PAYMENT_IN_ADITIONAL_ACCOUNT(16), //Cuenta adicional no recibe pagos que no proceden de Banxico
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
