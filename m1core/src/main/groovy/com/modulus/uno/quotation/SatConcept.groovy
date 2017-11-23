package com.modulus.uno.quotation

enum SatConcept {
  S1( 01010101 ,"No existe en el catálogo"),
  S2( 10101500,"Animales vivos de granja"),
  S3( 10101501,"Gatos vivos" ),
  S4( 10101504,"Perros" ),
  S5( 10101506,"Visón" ),
  S6( 10101507,"Ovejas" )


  private final Integer number
  private final String concept

  SatConcept(Integer number, String concept){
    this.number = number
    this.concept = concept
  }

  public Integer getNumber(){
    this.number
  }

  public String getConcetp(){
    this.concept
  }

}
