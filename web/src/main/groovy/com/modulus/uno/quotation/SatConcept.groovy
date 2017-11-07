package com.modulus.uno.quotation

enum SatConcept {
  S1( "01010101", "No existe en el catálogo"),
  S2( "10101500", "Animales vivos de granja"),
  S3( "10101501", "Gatos vivos"),
  S4( "10101504", "Perros"),
  S5( "10101506", "Visón"),
  S6( "10101507", "Ovejas")


  private final String key
  private final String concept
  private final String value

  SatConcept(String key, String concept){
    this.key = key
    this.concept = concept
  }

  public String getKey(){
    this.key
  }

  public String getConcept(){
    this.concept
  }

  public String getValue(){
    this.name()
  }

  String toString() {
    "${key} - ${concept}"
  }

}