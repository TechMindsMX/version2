package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class BankAccountService {

  def addBankAccountToRelationShip(BankAccount bankAccount, Long companyId = 0, Long businessEntityId = 0, def params) {
    if (businessEntityId != 0) {
      def businessEntity = BusinessEntity.get(businessEntityId)
        if (params.relation == "CLIENTE"){
          bankAccount.branchNumber = "*".padLeft(5,"0")
          bankAccount.accountNumber = bankAccount.accountNumber.padLeft(11,"*")
        }
        addBankAccountToBusinessEntity(bankAccount, businessEntity)
        return ["businessEntity", businessEntityId]
    }
    if (companyId != 0) {
        addBankAccountToCompany(bankAccount, companyId)
        return ["company",companyId]
    }

  }

  def saveAndAsociateBankAccount(BankAccount bankAccount, Map params) {
    Map result = [:]
    if (params.companyBankAccount) {
      Company company = Company.get(params.company)
      if (repeatedBankAccountCompany(bankAccount, company)) {
        result = [error:"La cuenta ya está registrada"]
        return result
      }

      bankAccount.save()
      company.addToBanksAccounts(bankAccount)
      company.save()
      result = [controller:"company", id:company.id]
    } else if (params.businessEntityBankAccount) {
      BusinessEntity businessEntity = BusinessEntity.get(params.businessEntity)
      if (repeatedBankAccountBusinessEntity(bankAccount, businessEntity)) {
        result = [error:"La cuenta ya está registrada"]
        return result
      }

      if (params.relation == "CLIENTE"){
        bankAccount.branchNumber = "*".padLeft(5,"0")
        bankAccount.accountNumber = (bankAccount.accountNumber - bankAccount.accountNumber.substring(0,7)).padLeft(11,"*")
      }
      bankAccount.save()
      businessEntity.addToBanksAccounts(bankAccount)
      businessEntity.save()
      result = [controller:"businessEntity", id:businessEntity.id]
    }
    result
  } 

  def repeatedBankAccountCompany(BankAccount bankAccount, Company company){
    def repeatedAccount = company.banksAccounts.find{ cuenta ->
      cuenta.banco.id == bankAccount.banco.id && (bankAccount.accountNumber ? cuenta.accountNumber == bankAccount.accountNumber : bankAccount.cardNumber ? cuenta.cardNumber == bankAccount.cardNumber : false) && cuenta.id != bankAccount.id
    }

  }

  def createABankAccount(BankAccountCommand command){
    def bankAccount = command.createBankAccount()
    bankAccount.banco = Bank.findByBankingCode(command.bank)
    bankAccount
  }


  def createABankAccountCommandByParams(Map properties){
    def command = new BankAccountCommand()
    command.accountNumber = properties.clabe.substring(6,17)
    command.branchNumber = properties.clabe.substring(3,6)
    command.bank = Bank.findByBankingCodeLike("%${properties.clabe.substring(0,3)}").bankingCode
    command.clabe = properties.clabe
    command
  }

  def addBankAccountToCompany(BankAccount bankAccount, def companyId){
    def company = Company.get(companyId)
    if(repeatedBankAccountCompany(bankAccount, company))
      throw new Exception("La cuenta indicada ya existe")
    bankAccount.save()
    log.info "BankAccount saved: ${bankAccount}"
    company.addToBanksAccounts(bankAccount)
    company.save()

  }

  def updateBankAccountCompany(BankAccount bankAccount, company){
    company = Company.get(company)
    if(repeatedBankAccountCompany(bankAccount, company))
      throw new Exception("La cuenta indicada ya existe")
    bankAccount.save()
    bankAccount
  }

  def repeatedBankAccountBusinessEntity(BankAccount bankAccount, BusinessEntity businessEntity){
    def repeatedAccount = businessEntity?.banksAccounts?.find{ cuenta ->
      cuenta.banco.id == bankAccount.banco.id && (bankAccount.accountNumber ? cuenta.accountNumber == bankAccount.accountNumber : bankAccount.cardNumber ? cuenta.cardNumber == bankAccount.cardNumber : false) && cuenta.id != bankAccount.id
    }
  }

  def addBankAccountToBusinessEntity(BankAccount bankAccount, def businessEntity){
    if(repeatedBankAccountBusinessEntity(bankAccount, businessEntity)){
      throw new Exception("La cuenta indicada ya existe")
    }

    bankAccount.save()

    businessEntity.addToBanksAccounts(bankAccount)
    businessEntity.save flush:true

    bankAccount
  }

  def updateBankAccountBusinessEntity(BankAccount bankAccount, BusinessEntity businessEntity){
    if(repeatedBankAccountBusinessEntity(bankAccount, businessEntity))
      throw new Exception("La cuenta indicada ya existe")

    bankAccount.save flush:true

    bankAccount
  }

  BankAccount createBankAccountForBusinessEntityFromRowEmployee(BusinessEntity businessEntity, Map rowEmployee) {
    Map dataBank = getDataBankFromClabe(rowEmployee.CLABE)
    BankAccount bankAccount = new BankAccount(
      accountNumber:dataBank.accountNumber,
      branchNumber:dataBank.branchNumber,
      clabe:rowEmployee.CLABE,
      cardNumber:rowEmployee.NUMTARJETA,
      banco:dataBank.bank
    )

    bankAccount.save()

    if (bankAccount.id) {
      businessEntity.addToBanksAccounts(bankAccount)
      businessEntity.save()
    }

    bankAccount
  }

  Map getDataBankFromClabe(String clabe) {
    Map data = [:]
    if (clabe.length() == 18) {
      String codeBank = clabe.substring(0,3)
      data.branchNumber = clabe.substring(3,6)
      data.accountNumber = clabe.substring(6,17)
      data.bank = Bank.findByBankingCodeLike("%${codeBank}")
    }
    data
  }
}
