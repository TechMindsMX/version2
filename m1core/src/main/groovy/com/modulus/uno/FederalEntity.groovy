package com.modulus.uno

enum FederalEntity {

  AGU ("Aguascalientes"),
  BCN ("Baja California"),
  BCS ("Baja California Sur"),
  CAM ("Campeche"),
  CHP ("Chiapas"),
  CHH ("Chihuahua"),
  COA ("Coahuila"),
  COL ("Colima"),
  DIF ("Ciudad de México"),
  DUR ("Durango"),
  GUA ("Guanajuato"),
  GRO ("Guerrero"),
  HID ("Hidalgo"),
  JAL ("Jalisco"),
  MEX ("Estado de México"),
  MIC ("Michoacán"),
  MOR ("Morelos"),
  NAY ("Nayarit"),
  NLE ("Nuevo León"),
  OAX ("Oaxaca"),
  PUE ("Puebla"),
  QUE ("Querétaro"),
  ROO ("Quintana Roo"),
  SLP ("San Luis Potosí"),
  SIN ("Sinaloa"),
  SON ("Sonora"),
  TAB ("Tabasco"),
  TAM ("Tamaulipas"),
  TLA ("Tlaxcala"),
  VER ("Veracruz"),
  YUC ("Yucatán"),
  ZAC ("Zacatecas")

  private final String entity

  FederalEntity(String entity) {
    this.entity = entity
  }

  String toString() {
    this.entity
  }

}
