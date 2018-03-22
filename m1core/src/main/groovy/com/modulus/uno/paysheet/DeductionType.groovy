package com.modulus.uno.paysheet

enum DeductionType {

  D001 ('001','Seguridad social'),
  D002 ('002','ISR'),
  D003 ('003','Aportaciones a retiro, cesantía en edad avanzada y vejez.'),
  D004 ('004','Otros'),
  D005 ('005','Aportaciones a Fondo de vivienda'),
  D006 ('006','Descuento por incapacidad'),
  D007 ('007','Pensión alimenticia'),
  D008 ('008','Renta'),
  D009 ('009','Préstamos provenientes del Fondo Nacional de la Vivienda para los Trabajadores'),
  D010 ('010','Pago por crédito de vivienda'),
  D011 ('011','Pago de abonos INFONACOT'),
  D012 ('012','Anticipo de salarios'),
  D013 ('013','Pagos hechos con exceso al trabajador'),
  D014 ('014','Errores'),
  D015 ('015','Pérdidas'),
  D016 ('016','Averías'),
  D017 ('017','Adquisición de artículos producidos por la empresa o establecimiento'),
  D018 ('018','Cuotas para la constitución y fomento de sociedades cooperativas y de cajas de ahorro'),
  D019 ('019','Cuotas sindicales'),
  D020 ('020','Ausencia (Ausentismo)'),
  D021 ('021','Cuotas obrero patronales'),
  D022 ('022','Impuestos Locales'),
  D023 ('023','Aportaciones voluntarias'),
  D024 ('024','Ajuste en Gratificación Anual (Aguinaldo) Exento'),
  D025 ('025','Ajuste en Gratificación Anual (Aguinaldo) Gravado'),
  D026 ('026','Ajuste en Participación de los Trabajadores en las Utilidades PTU Exento'),
  D027 ('027','Ajuste en Participación de los Trabajadores en las Utilidades PTU Gravado'),
  D028 ('028','Ajuste en Reembolso de Gastos Médicos Dentales y Hospitalarios Exento'),
  D029 ('029','Ajuste en Fondo de ahorro Exento'),
  D030 ('030','Ajuste en Caja de ahorro Exento'),
  D031 ('031','Ajuste en Contribuciones a Cargo del Trabajador Pagadas por el Patrón Exento'),
  D032 ('032','Ajuste en Premios por puntualidad Gravado'),
  D033 ('033','Ajuste en Prima de Seguro de vida Exento'),
  D034 ('034','Ajuste en Seguro de Gastos Médicos Mayores Exento'),
  D035 ('035','Ajuste en Cuotas Sindicales Pagadas por el Patrón Exento'),
  D036 ('036','Ajuste en Subsidios por incapacidad Exento'),
  D037 ('037','Ajuste en Becas para trabajadores y/o hijos Exento'),
  D038 ('038','Ajuste en Horas extra Exento'),
  D039 ('039','Ajuste en Horas extra Gravado'),
  D040 ('040','Ajuste en Prima dominical Exento'),
  D041 ('041','Ajuste en Prima dominical Gravado'),
  D042 ('042','Ajuste en Prima vacacional Exento'),
  D043 ('043','Ajuste en Prima vacacional Gravado'),
  D044 ('044','Ajuste en Prima por antigüedad Exento'),
  D045 ('045','Ajuste en Prima por antigüedad Gravado'),
  D046 ('046','Ajuste en Pagos por separación Exento'),
  D047 ('047','Ajuste en Pagos por separación Gravado'),
  D048 ('048','Ajuste en Seguro de retiro Exento'),
  D049 ('049','Ajuste en Indemnizaciones Exento'),
  D050 ('050','Ajuste en Indemnizaciones Gravado'),
  D051 ('051','Ajuste en Reembolso por funeral Exento'),
  D052 ('052','Ajuste en Cuotas de seguridad social pagadas por el patrón Exento'),
  D053 ('053','Ajuste en Comisiones Gravado'),
  D054 ('054','Ajuste en Vales de despensa Exento'),
  D055 ('055','Ajuste en Vales de restaurante Exento'),
  D056 ('056','Ajuste en Vales de gasolina Exento'),
  D057 ('057','Ajuste en Vales de ropa Exento'),
  D058 ('058','Ajuste en Ayuda para renta Exento'),
  D059 ('059','Ajuste en Ayuda para artículos escolares Exento'),
  D060 ('060','Ajuste en Ayuda para anteojos Exento'),
  D061 ('061','Ajuste en Ayuda para transporte Exento'),
  D062 ('062','Ajuste en Ayuda para gastos de funeral Exento'),
  D063 ('063','Ajuste en Otros ingresos por salarios Exento'),
  D064 ('064','Ajuste en Otros ingresos por salarios Gravado'),
  D065 ('065','Ajuste en Jubilaciones, pensiones o haberes de retiro Exento'),
  D066 ('066','Ajuste en Jubilaciones, pensiones o haberes de retiro Gravado'),
  D067 ('067','Ajuste en Pagos por separación Acumulable'),
  D068 ('068','Ajuste en Pagos por separación No acumulable'),
  D069 ('069','Ajuste en Jubilaciones, pensiones o haberes de retiro Acumulable'),
  D070 ('070','Ajuste en Jubilaciones, pensiones o haberes de retiro No acumulable'),
  D071 ('071','Ajuste en Subsidio para el empleo (efectivamente entregado al trabajador)'),
  D072 ('072','Ajuste en Ingresos en acciones o títulos valor que representan bienes Exento'),
  D073 ('073','Ajuste en Ingresos en acciones o títulos valor que representan bienes Gravado'),
  D074 ('074','Ajuste en Alimentación Exento'),
  D075 ('075','Ajuste en Alimentación Gravado'),
  D076 ('076','Ajuste en Habitación Exento'),
  D077 ('077','Ajuste en Habitación Gravado'),
  D078 ('078','Ajuste en Premios por asistencia'),
  D079 ('079','Ajuste en Pagos distintos a los listados y que no deben considerarse como ingreso por sueldos, salarios o ingresos asimilados.'),
  D080 ('080','Ajuste en Viáticos gravados'),
  D081 ('081','Ajuste en Viáticos (entregados al trabajador)'),
  D082 ('082','Ajuste en Fondo de ahorro Gravado'),
  D083 ('083','Ajuste en Caja de ahorro Gravado'),
  D084 ('084','Ajuste en Prima de Seguro de vida Gravado'),
  D085 ('085','Ajuste en Seguro de Gastos Médicos Mayores Gravado'),
  D086 ('086','Ajuste en Subsidios por incapacidad Gravado'),
  D087 ('087','Ajuste en Becas para trabajadores y/o hijos Gravado'),
  D088 ('088','Ajuste en Seguro de retiro Gravado'),
  D089 ('089','Ajuste en Vales de despensa Gravado'),
  D090 ('090','Ajuste en Vales de restaurante Gravado'),
  D091 ('091','Ajuste en Vales de gasolina Gravado'),
  D092 ('092','Ajuste en Vales de ropa Gravado'),
  D093 ('093','Ajuste en Ayuda para renta Gravado'),
  D094 ('094','Ajuste en Ayuda para artículos escolares Gravado'),
  D095 ('095','Ajuste en Ayuda para anteojos Gravado'),
  D096 ('096','Ajuste en Ayuda para transporte Gravado'),
  D097 ('097','Ajuste en Ayuda para gastos de funeral Gravado'),
  D098 ('098','Ajuste a ingresos asimilados a salarios gravados'),
  D099 ('099','Ajuste a ingresos por sueldos y salarios gravados'),
  D100 ('100','Ajuste en Viáticos exentos'),
  D101 ('101','ISR Retenido de ejercicio anterior')

  private final String key
  private final String description

  DeductionType(String key, String description) {
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
