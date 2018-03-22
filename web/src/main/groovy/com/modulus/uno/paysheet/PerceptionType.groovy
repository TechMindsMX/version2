package com.modulus.uno.paysheet

enum PerceptionType {

  P001 ("001","Sueldos, Salarios  Rayas y Jornales", "G"),
  P002 ("002","Gratificación Anual (Aguinaldo)", "G"),
  P003 ("003", "Participación de los Trabajadores en las Utilidades PTU", "G"),
  P004 ("004", "Reembolso de Gastos Médicos Dentales y Hospitalarios", "G"),
  P005 ("005", "Fondo de Ahorro", "G"),
  P006 ("006", "Caja de ahorro", "G"),
  P009 ("009", "Contribuciones a Cargo del Trabajador Pagadas por el Patrón", "G"),
  P010 ("010", "Premios por puntualidad", "G"),
  P011 ("011", "Prima de Seguro de vida", "G"),
  P012 ("012", "Seguro de Gastos Médicos Mayores", "G"),
  P013 ("013", "Cuotas Sindicales Pagadas por el Patrón", "G"),
  P014 ("014", "Subsidios por incapacidad", "G"),
  P015 ("015", "Becas para trabajadores y/o hijos", "G"),
  P019 ("019", "Horas extra", "G"),
  P020 ("020", "Prima dominical", "G"),
  P021 ("021", "Prima vacacional", "G"),
  P022 ("022", "Prima por antigüedad", "G"),
  P023 ("023", "Pagos por separación", "G"),
  P024 ("024", "Seguro de retiro", "G"),
  P025 ("025", "Indemnizaciones", "G"),
  P026 ("026", "Reembolso por funeral", "G"),
  P027 ("027", "Cuotas de seguridad social pagadas por el patrón", "G"),
  P028 ("028", "Comisiones", "G"),
  P029 ("029", "Vales de despensa", "G"),
  P030 ("030", "Vales de restaurante", "G"),
  P031 ("031", "Vales de gasolina", "G"),
  P032 ("032", "Vales de ropa", "G"),
  P033 ("033", "Ayuda para renta", "G"),
  P034 ("034", "Ayuda para artículos escolares", "G"),
  P035 ("035", "Ayuda para anteojos", "G"),
  P036 ("036", "Ayuda para transporte", "G"),
  P037 ("037", "Ayuda para gastos de funeral", "G"),
  P038 ("038", "Otros ingresos por salarios", "G"),
  P039 ("039", "Jubilaciones, pensiones o haberes de retiro", "G"),
  P044 ("044", "Jubilaciones, pensiones o haberes de retiro en parcialidades", "G"),
  P045 ("045", "Ingresos en acciones o títulos valor que representan bienes", "G"),
  P046 ("046", "Ingresos asimilados a salarios", "G"),
  P047 ("047", "Alimentación", "G"),
  P048 ("048", "Habitación", "G"),
  P049 ("049", "Premios por asistencia", "G"),
  P050 ("050", "Viáticos", "G")

  private final String key
  private final String description
  private final String type

  PerceptionType(String key, String description, String type) {
    this.key = key
    this.description = description
    this.type = type
  }

  String toString() {
    "${this.key} - ${this.description}"
  }

}
