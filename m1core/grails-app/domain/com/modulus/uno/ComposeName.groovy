package com.modulus.uno

import grails.converters.JSON

class ComposeName {
  String value
  NameType type

  static belongsTo = [businessEntity:BusinessEntity]

  static constraints = {
    value blank:false,size:1..100
  }

  static marshaller = {
    JSON.registerObjectMarshaller(ComposeName, 1) { m ->
      return [
      id: m.id,
      value: m.value,
      type: m.type
      ]
    }
  }
}
