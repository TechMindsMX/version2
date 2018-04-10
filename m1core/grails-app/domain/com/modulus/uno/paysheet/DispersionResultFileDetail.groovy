package com.modulus.uno.paysheet

class DispersionResultFileDetail {

  String account
  String resultMessage
  DispersionResultFileDetailStatus status

  static belongsTo = [dispersionResultFile:DispersionResultFile]

  Date dateCreated
  Date lastUpdated

}
