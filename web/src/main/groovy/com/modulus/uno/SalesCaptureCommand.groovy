package com.modulus.uno
import grails.validation.Validateable

class SalesCaptureCommand implements Validateable{
  ArrayList<SalesCaptureArticleCommand> articles
}
