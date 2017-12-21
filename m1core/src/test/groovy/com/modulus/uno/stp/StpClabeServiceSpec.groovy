package com.modulus.uno.stp

import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.TestFor
import grails.test.mixin.Mock

import com.modulus.uno.ModulusUnoAccount
import com.modulus.uno.ClientLink
import com.modulus.uno.BusinessException

@TestFor(StpClabeService)
@Mock([ModulusUnoAccount, ClientLink])
class StpClabeServiceSpec extends Specification {

  @Unroll
  def "Obtain the first main account for #payerAccount payer account with sizePrefix=#sizePrefix"(){
    given:"A payer account with no users in it"
    when:"I calculate the first main acount"
    String result = service.generateSTPMainAccount(payerAccount, sizePrefix)
    then:"I should obtain the account"
    result.substring(0,17) == mainAccount
    where:"We have the next data "
    payerAccount  | sizePrefix  || mainAccount
    "6461801119"  | 3           || "64618011190010000"
    "6461801119"  | 4           || "64618011190001000"
    "6461801120"  | 3           || "64618011200010000"
    "6461801120"  | 4           || "64618011200001000"
    "6461801122"  | 3           || "64618011220010000"
    "6461801122"  | 4           || "64618011220001000"
  }

  @Unroll
  def "Should thrown an BusinessException when serial exceed max value for payer account when sizePrefix=#sizePrefix and last stp clabe user existing is #stpClabe"() {
    given:
    String payerAccount = "6461801119"
    and:
    ModulusUnoAccount m1Account = new ModulusUnoAccount(stpClabe:stpClabe).save(validate:false)
    ModulusUnoAccount.metaClass.static.findAllByStpClabeLike = { [m1Account] }
    when:
    String result = service.generateSTPMainAccount(payerAccount, sizePrefix)
    then:
    thrown BusinessException
    where:
    stpClabe              | sizePrefix
    "646180111999900001"  | 3
    "646180111999990001"  | 4
  }

  @Unroll
  def "Obtain accounts for payer account #payerAccount with exist account with sizePrefix #sizePrefix"(){
    given:"A payer account with an exist account"
    ModulusUnoAccount.metaClass.static.findAllByStpClabeLike = { accountsExisting }
    when:"I calculate the next account"
    String result = service.generateSTPMainAccount(payerAccount, sizePrefix)
    then:"I should obtain the account"
    result.substring(0,17) == mainAccount
    where:"We have the next data "
    payerAccount |  sizePrefix  | accountsExisting                                      || mainAccount
    "6461801119" |  3           | []                                                    || "64618011190010000"
    "6461801119" |  4           | []                                                    || "64618011190001000"
    "6461801119" |  3           | [new ModulusUnoAccount(stpClabe:"64618011190010000")] || "64618011190020000"
    "6461801119" |  4           | [new ModulusUnoAccount(stpClabe:"64618011190001000")] || "64618011190002000"
    "6461801119" |  3           | [new ModulusUnoAccount(stpClabe:"64618011190020000")] || "64618011190030000"
    "6461801119" |  4           | [new ModulusUnoAccount(stpClabe:"64618011190002000")] || "64618011190003000"
    "6461801120" |  3           | []                                                    || "64618011200010000"
    "6461801120" |  4           | []                                                    || "64618011200001000"
    "6461801120" |  3           | [new ModulusUnoAccount(stpClabe:"64618011200010000")] || "64618011200020000"
    "6461801120" |  4           | [new ModulusUnoAccount(stpClabe:"64618011200001000")] || "64618011200002000"
    "6461801120" |  3           | [new ModulusUnoAccount(stpClabe:"64618011200020000")] || "64618011200030000"
    "6461801120" |  4           | [new ModulusUnoAccount(stpClabe:"64618011200002000")] || "64618011200003000"
    "6461801120" |  3           | [new ModulusUnoAccount(stpClabe:"64618011200030000")] || "64618011200040000"
    "6461801120" |  4           | [new ModulusUnoAccount(stpClabe:"64618011200003000")] || "64618011200004000"
    "6461801120" |  3           | [new ModulusUnoAccount(stpClabe:"64618011200040000")] || "64618011200050000"
    "6461801120" |  4           | [new ModulusUnoAccount(stpClabe:"64618011200004000")] || "64618011200005000"
    "6461801120" |  3           | [new ModulusUnoAccount(stpClabe:"64618011200050000")] || "64618011200060000"
    "6461801120" |  4           | [new ModulusUnoAccount(stpClabe:"64618011200005000")] || "64618011200006000"
    "6461801120" |  3           | [new ModulusUnoAccount(stpClabe:"64618011200060000")] || "64618011200070000"
    "6461801120" |  4           | [new ModulusUnoAccount(stpClabe:"64618011200006000")] || "64618011200007000"
    "6461801120" |  3           | [new ModulusUnoAccount(stpClabe:"64618011200070000")] || "64618011200080000"
    "6461801120" |  4           | [new ModulusUnoAccount(stpClabe:"64618011200007000")] || "64618011200008000"
    "6461801120" |  3           | [new ModulusUnoAccount(stpClabe:"64618011200080000")] || "64618011200090000"
    "6461801120" |  4           | [new ModulusUnoAccount(stpClabe:"64618011200008000")] || "64618011200009000"
    "6461801120" |  3           | [new ModulusUnoAccount(stpClabe:"64618011200090000")] || "64618011200100000"
    "6461801120" |  4           | [new ModulusUnoAccount(stpClabe:"64618011200009000")] || "64618011200010000"
  }

  @Unroll
  def "Obtain first sub-account #subAccount for mainAccount #mainAccount with sizeSubPrefix = #sizeSubPrefix"(){
    given:"A main account"
    when:"I calculate the next sub-account"
    String result = service.generateSTPSubAccount(mainAccount, sizeSubPrefix)
    then:"I should obtain the sub-account"
    result.substring(0,17) == subAccount
    where:"We have the next data "
    mainAccount         | sizeSubPrefix || subAccount
    "64618011190001000" | 3             || "64618011190001001"
    "64618011190010000" | 4             || "64618011190010001"
    "64618011190002000" | 3             || "64618011190002001"
    "64618011190020000" | 4             || "64618011190020001"
    "64618011190003000" | 3             || "64618011190003001"
    "64618011190030000" | 4             || "64618011190030001"
  }

  @Unroll
  def "Should thrown an BusinessException when serial exceed max value for sub account with sizeSubPrefix = #sizeSubPrefix and stpClabe #stpClabe and mainAccount #mainAccount"() {
    given:
    ClientLink client = new ClientLink(stpClabe:stpClabe).save(validate:false)
    ClientLink.metaClass.static.findAllByStpClabeLike = { [client] }
    when:
    String result = service.generateSTPSubAccount(mainAccount, sizeSubPrefix)
    then:
    thrown BusinessException
    where:
    mainAccount           | stpClabe              | sizeSubPrefix
    "646180111900010007"  | "646180111900019991"  | 3
    "646180111900100007"  | "646180111900199991"  | 4
  }

  @Unroll
  def "Obtain sub account #subAccount for main account #mainAccount with sizeSubPrefix = #sizeSubPrefix"(){
    given:"A main account with an exist account"
    ClientLink.metaClass.static.findAllByStpClabeLike = { clientsExisting }
    when:"I calculate the next account"
    String result = service.generateSTPSubAccount(mainAccount, sizeSubPrefix)
    then:"I should obtain the account"
    result.substring(0,17) == subAccount
    where:"We have the next data "
    mainAccount          | clientsExisting                                |  sizeSubPrefix || subAccount
    "646180111900010001" | []                                             |  3             || "64618011190001001"
    "646180111900100001" | []                                             |  4             || "64618011190010001"
    "646180111900010001" | [new ClientLink(stpClabe:"64618011190001001")] |  3             || "64618011190001002"
    "646180111900100001" | [new ClientLink(stpClabe:"64618011190010001")] |  4             || "64618011190010002"
    "646180111900010001" | [new ClientLink(stpClabe:"64618011190001002")] |  3             || "64618011190001003"
    "646180111900100001" | [new ClientLink(stpClabe:"64618011190010002")] |  4             || "64618011190010003"
    "646180111900020001" | []                                             |  3             || "64618011190002001"
    "646180111900200001" | []                                             |  4             || "64618011190020001"
    "646180111900020001" | [new ClientLink(stpClabe:"64618011190002001")] |  3             || "64618011190002002"
    "646180111900200001" | [new ClientLink(stpClabe:"64618011190020001")] |  4             || "64618011190020002"
    "646180111900020001" | [new ClientLink(stpClabe:"64618011190002002")] |  3             || "64618011190002003"
    "646180111900200001" | [new ClientLink(stpClabe:"64618011190020002")] |  4             || "64618011190020003"
    "646180111900020001" | [new ClientLink(stpClabe:"64618011190002003")] |  3             || "64618011190002004"
    "646180111900200001" | [new ClientLink(stpClabe:"64618011190020003")] |  4             || "64618011190020004"
    "646180111900020001" | [new ClientLink(stpClabe:"64618011190002009")] |  3             || "64618011190002010"
    "646180111900200001" | [new ClientLink(stpClabe:"64618011190020009")] |  4             || "64618011190020010"
  }


}

