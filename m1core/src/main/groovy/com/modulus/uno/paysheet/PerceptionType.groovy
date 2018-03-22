package com.modulus.uno.paysheet

enum PerceptionType {

  P001 ("001","Sueldos, Salarios  Rayas y Jornales"),
  P002 ("002","Gratificación Anual (Aguinaldo)"),
  P003 ("003", "Participación de los Trabajadores en las Utilidades PTU"),
  P004 ("004", "Reembolso de Gastos Médicos Dentales y Hospitalarios"),
  P005 ("005", "Fondo de Ahorro"),
  P006 ("006", "Caja de ahorro"),
  P009 ("009", "Contribuciones a Cargo del Trabajador Pagadas por el Patrón"),
  P010 ("010", "Premios por puntualidad"),
  P011 ("011", "Prima de Seguro de vida"),
  P012 ("012", "Seguro de Gastos Médicos Mayores"),
  P013 ("013", "Cuotas Sindicales Pagadas por el Patrón"),
  P014 ("014", "Subsidios por incapacidad"),
  P015 ("015", "Becas para trabajadores y/o hijos"),
  P019 ("019", "Horas extra"),
  P020 ("020", "Prima dominical"),
  P021 ("021", "Prima vacacional"),
  P022 ("022", "Prima por antigüedad"),
  P023 ("023", "Pagos por separación"),
  P024 ("024", "Seguro de retiro"),
  P025 ("025", "Indemnizaciones"),
  P026 ("026", "Reembolso por funeral"),
  P027 ("027", "Cuotas de seguridad social pagadas por el patrón"),
  P028 ("028", "Comisiones"),
  P029 ("029", "Vales de despensa"),
  P030 ("030", "Vales de restaurante"),
  P031 ("031", "Vales de gasolina"),
  P032 ("032", "Vales de ropa"),
  P033 ("033", "Ayuda para renta"),
  P034 ("034", "Ayuda para artículos escolares"),
  P035 ("035", "Ayuda para anteojos"),
  P036 ("036", "Ayuda para transporte"),
  P037 ("037", "Ayuda para gastos de funeral"),
  P038 ("038", "Otros ingresos por salarios"),
  P039 ("039", "Jubilaciones, pensiones o haberes de retiro"),
  P044 ("044", "Jubilaciones, pensiones o haberes de retiro en parcialidades"),
  P045 ("045", "Ingresos en acciones o títulos valor que representan bienes"),
  P046 ("046", "Ingresos asimilados a salarios"),
  P047 ("047", "Alimentación"),
  P048 ("048", "Habitación"),
  P049 ("049", "Premios por asistencia"),
  P050 ("050", "Viáticos")

  private final String key
  private final String description

  PerceptionType(String key, String description) {
    this.key = key
    this.description = description
  }

  String toString() {
    "${this.key} - ${this.description}"
  }

}
