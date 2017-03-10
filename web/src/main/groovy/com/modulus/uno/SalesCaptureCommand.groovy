package com.modulus.uno
import grails.validation.Validateable

class SalesCaptureCommand implements Validateable{

  Long idSale
  Date saleDate
  def saleReference
  Integer saleDuration
  String emailForSend
  String emailForSendCopy
  List<String> itemName
  List<String> itemDescription
  List<Integer> itemsAmount
  List<Integer> priceItem
  List<Integer> taxItem
  List<Integer> amount
  Integer partialPayment
  Integer subtotal
  Integer total
  String  recipientNote
  String termsAndConditions

}
