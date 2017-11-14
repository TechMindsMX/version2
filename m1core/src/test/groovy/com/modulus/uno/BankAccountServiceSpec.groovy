package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import java.lang.Void as Should

@TestFor(BankAccountService)
@Mock([Bank,BankAccount,Company,BusinessEntity])
class BankAccountServiceSpec extends Specification {

  Should "save and asociate bank account with company"() {
    given:"A company"
      Company company = new Company().save(validate:false)
    and: "The bank account to save"
      BankAccount bankAccount = new BankAccount(clabe:null, accountNumber:"00012345678", branchNumber:"111", banco:new Bank(name:"Bank").save(validate:false))
    and:"The params"
      Map params = [companyBankAccount:true, company:company.id]
    when:
      def result = service.saveAndAsociateBankAccount(bankAccount, params)
    then:
      bankAccount.id
      result.controller == "company"
  }

  Should "save and asociate bank account with business entity"() {
    given:"A company"
      BusinessEntity businessEntity = new BusinessEntity().save(validate:false)
    and: "The bank account to save"
      BankAccount bankAccount = new BankAccount(clabe:null, accountNumber:"00012345678", branchNumber:"111", banco:new Bank(name:"Bank").save(validate:false))
    and:"The params"
      Map params = [businessEntityBankAccount:true, businessEntity:businessEntity.id]
    when:
      def result = service.saveAndAsociateBankAccount(bankAccount, params)
    then:
      bankAccount.id
      result.controller == "businessEntity"
  }

  Should "don't save and asociate bank account with company because already exists"() {
    given:"A company"
      Company company = new Company().save(validate:false)
      Bank bank = new Bank(name:"Bank").save(validate:false)
      BankAccount bankAccount = new BankAccount(clabe:null, accountNumber:"00012345678", branchNumber:"111", banco:bank).save(validate:false)
      company.addToBanksAccounts(bankAccount)
      company.save(validate:false)
    and: "The bank account to save"
      BankAccount repeatedBankAccount = new BankAccount(clabe:null, accountNumber:"00012345678", branchNumber:"111", banco:bank)
    and:"The params"
      Map params = [companyBankAccount:true, company:company.id]
    when:
      def result = service.saveAndAsociateBankAccount(repeatedBankAccount, params)
    then:
      result.error == "La cuenta ya está registrada"
  }

  Should "don't save and asociate bank account with business entity because already exists"() {
    given:"A company"
      BusinessEntity businessEntity = new BusinessEntity().save(validate:false)
      Bank bank = new Bank(name:"Bank").save(validate:false)
      BankAccount bankAccount = new BankAccount(clabe:null, accountNumber:"00012345678", branchNumber:"111", banco:bank).save(validate:false)
      businessEntity.addToBanksAccounts(bankAccount)
      businessEntity.save(validate:false)
    and: "The bank account to save"
      BankAccount repeatedBankAccount = new BankAccount(clabe:null, accountNumber:"00012345678", branchNumber:"111", banco:bank)
    and:"The params"
      Map params = [businessEntityBankAccount:true, businessEntity:businessEntity.id]
    when:
      def result = service.saveAndAsociateBankAccount(repeatedBankAccount, params)
    then:
      result.error == "La cuenta ya está registrada"
  }

  Should "add an account to a company"(){
    given:"a bank account"
      BankAccount bankAccount = createBankAccount()
      def company = createCompany()
    when:
      def savedBankAccount = service.addBankAccountToCompany(bankAccount, company.id)
    then:
      savedBankAccount.id
  }

  Should "add an account to a businessEntity"(){
    given:"add an account to a businessEntity"
      BankAccount bankAccount = createBankAccount()
      def businessEntity = createBusinessEntity()
    when:
      def savedBankAccount = service.addBankAccountToBusinessEntity(bankAccount, businessEntity)
    then:
      savedBankAccount.id
  }

  Should "find bank account repeated for company"() {
    given:"A bank account"
      BankAccount bankAccount = createBankAccount()
    and:"A company with bank accounts"
      BankAccount bankExisting = createBankAccount()
      bankExisting.banco = bankAccount.banco
      bankExisting.save(validate:false)
      Company company = createCompany()
      company.banksAccounts = [bankExisting]
    when:
      def repeated = service.repeatedBankAccountCompany(bankAccount, company)
    then:
      repeated
  }

  Should "not found bank account repeated for company"() {
    given:"A bank account"
      BankAccount bankAccount = createBankAccount()
    and:"A company with bank accounts"
      BankAccount bankExisting = createBankAccount()
      bankExisting.accountNumber = "09876543210"
      bankExisting.banco = bankAccount.banco
      bankExisting.save(validate:false)
      Company company = createCompany()
      company.banksAccounts = [bankExisting]
    when:
      def repeated = service.repeatedBankAccountCompany(bankAccount, company)
    then:
      !repeated
  }

  Should "create a bank account"() {
    given:"A bank account command"
      BankAccountCommand bankAccountCommand = new BankAccountCommand(accountNumber:"12345", branchNumber:"120", clabe:"123456789123456789", bank:"90902", concentradora:false)
    and:
      Bank.metaClass.static.findByBankingCode = { new Bank(bankingCode:"90902",name:"INDEVAL").save() }
    when:
      def bankAccount = service.createABankAccount(bankAccountCommand)
    then:
      bankAccount.accountNumber == "00000012345"
      bankAccount.branchNumber == "120"
      bankAccount.clabe == "123456789123456789"
      bankAccount.banco.bankingCode == "90902"
  }

  Should "find bank account repeated for business entity"() {
    given:"A bank account"
      BankAccount bankAccount = createBankAccount()
    and:"A business entity with bank accounts"
      BankAccount bankExisting = createBankAccount()
      bankExisting.banco = bankAccount.banco
      bankExisting.save(validate:false)
      BusinessEntity businessEntity = createBusinessEntity()
      businessEntity.banksAccounts = [bankExisting]
    when:
      def repeated = service.repeatedBankAccountBusinessEntity(bankAccount, businessEntity)
    then:
      repeated
  }

  Should "not found bank account repeated for businessEntity"() {
    given:"A bank account"
      BankAccount bankAccount = createBankAccount()
    and:"A business entity with bank accounts"
      BankAccount bankExisting = createBankAccount()
      bankExisting.accountNumber = "09876543210"
      bankExisting.banco = bankAccount.banco
      bankExisting.save(validate:false)
      BusinessEntity businessEntity = createBusinessEntity()
      businessEntity.banksAccounts = [bankExisting]
    when:
      def repeated = service.repeatedBankAccountBusinessEntity(bankAccount, businessEntity)
    then:
      !repeated
  }

  private def createCompany(){
    def company = new Company(rfc:"JIGE930831NZ1",
                  bussinessName:"ABCD",
                  address:new Address(street:"Bellas Artes",
                                        streetNumber:"232",
                                        suite:"S/N",
                                        zipCode:"57730"),
                  webSite:"http.//www.somecompany.com",
                  employeeNumbers:5,
                  grossAnnualBilling:4000).save(validate:false)
    company
  }

  private def createBankAccount(){
    def bank = new Bank(bankingCode:"90902",name:"INDEVAL").save()

    new BankAccount(accountNumber:"12345678901",
                    branchNumber:"201590",
                    clabe:"002115016003269411",
                    banco:bank)
  }

  private def createBusinessEntity(){
    def businessEntity = new BusinessEntity(rfc:"PRV950621001",
                         website:"www.prvuno.com",
                         type:BusinessEntityType.MORAL)

  }

  Should "obtain all data bank map from clabe when its length is 18"() {
    given:"The clabe with valid length"
      String clabe = "036180009876543217"
    and:
      Bank bank = new Bank(bankingCode:"40036", name:"Banco").save(validate:false)
    when:
      def result = service.getDataBankFromClabe(clabe)
    then:
      result.branchNumber == "180"
      result.accountNumber == "00987654321"
      result.bank.bankingCode.endsWith("036")
  }

  @Unroll
  Should "obtain empty data bank map from clabe when its length isn't 18"() {
    given:"The clabe with no valid length"
      String clabe = wrongClabe
    and:
      Bank bank = new Bank(bankingCode:"40036", name:"Banco").save(validate:false)
    when:
      def result = service.getDataBankFromClabe(clabe)
    then:
      result == emptyMap
    where:
      wrongClabe  || emptyMap
      "036180"    || [:]
      "12345678901234567890"  ||  [:]
  }

}
