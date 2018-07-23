package com.modulus.uno.paysheet

enum OtherPerceptionType {

  O001 ("001","Reintegro de ISR pagado en exceso (siempre que no haya sido enterado al SAT)."),
  O002 ("002", "Subsidio para el empleo (efectivamente entregado al trabajador)."),
  O003 ("003", "Viáticos (entregados al trabajador)."),
  O004 ("004", "Aplicación de saldo a favor por compensación anual."),
  O005 ("005", "Reintegro de ISR retenido en exceso de ejercicio anterior (siempre que no haya sido enterado al SAT)."),
  O999 ("999", "Pagos distintos a los listados y que no deben considerarse como ingreso por sueldos, salarios o ingresos asimilados.")

  private final String key
  private final String description

  OtherPerceptionType(String key, String description) {
    this.key = key
    this.description = description
  }

  String getKey() {
    this.key
  }

  String getDescription() {
    this.description
  }

  String toString() {
    "${this.key} - ${this.description}"
  }

}
