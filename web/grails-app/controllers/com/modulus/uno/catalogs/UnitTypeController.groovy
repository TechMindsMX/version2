package com.modulus.uno.catalogs

class UnitTypeController {
  
  def index() {
    params.max = 25
    [unitTypeList:UnitType.list(params), unitTypeCount:UnitType.count()]
  }

  def create() {
    respond new UnitType()
  }

}
