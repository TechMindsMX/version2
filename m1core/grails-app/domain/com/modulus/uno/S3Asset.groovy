package com.modulus.uno
import grails.converters.JSON

class S3Asset {

  String bucket
  String title
  String mimeType
  String localPath
  String localUrl
  String originalName
  String type
  Boolean status

  static constraints = {
    bucket nullable:false
    title nullable:false
    mimeType nullable:false
    localPath nullable:false
    localUrl nullable:false
    originalName nullable:false
  }

  String toString() {
    originalName
  }

  static marshaller = {
    JSON.registerObjectMarshaller(S3Asset, 1) { m ->
      return [
      id: m.id,
      url:m.localUrl,
      orinalName:m.originalName
      ]
    }
  }



}
