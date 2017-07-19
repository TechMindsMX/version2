package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import java.lang.Void as Should
import org.springframework.mock.web.MockMultipartFile

@TestFor(AccountStatementBankAccountService)
@Mock([BankAccount, AccountStatementBankAccount, S3Asset])
class AccountStatementBankAccountServiceSpec extends Specification {

  S3AssetService s3AssetService = Mock(S3AssetService)

  def setup() {
    service.s3AssetService = s3AssetService
  }

  void "Should save an account statement for bank account"() {
    given:"The account statement"
      BankAccount bankAccount = new BankAccount().save(validate:false)
      Date month = new Date().parse("dd-MM-yyyy","01-06-2017")
      AccountStatementBankAccount accountStatementDoc = new AccountStatementBankAccount(month:month, bankAccount:bankAccount)
    and:"The document"
      def document = new MockMultipartFile("file.txt", "", "plain/text", [] as byte[])
    when:
      def result = service.saveAccountStatementForBankAccount(accountStatementDoc, document)
    then:
      1 * s3AssetService.createTempFilesOfMultipartsFiles(_,_)
  }

  void "Should throw business exception when the account statement for bank account to selected month/year is already exists"() {
    given:"The account statement"
      BankAccount bankAccount = new BankAccount().save(validate:false)
      Date month = new Date().parse("dd-MM-yyyy","01-06-2017")
      AccountStatementBankAccount accountStatementDoc = new AccountStatementBankAccount(month:month, bankAccount:bankAccount)
    and:"The document"
      def document = new MockMultipartFile("file.txt", "", "plain/text", [] as byte[])
    and:
      AccountStatementBankAccount.metaClass.static.findByMonth = { new AccountStatementBankAccount() }
    when:
      def result = service.saveAccountStatementForBankAccount(accountStatementDoc, document)
    then:
      thrown BusinessException
  }

  void "Should throw business exception when the account statement for bank account with selected month/year is after current month/year"() {
    given:"The account statement"
      BankAccount bankAccount = new BankAccount().save(validate:false)
      Date month = Date.parse("MM-yyyy", new Date().format("MM-yyyy"))+35
      AccountStatementBankAccount accountStatementDoc = new AccountStatementBankAccount(month:month, bankAccount:bankAccount)
    and:"The document"
      def document = new MockMultipartFile("file.txt", "", "plain/text", [] as byte[])
    when:
      def result = service.saveAccountStatementForBankAccount(accountStatementDoc, document)
    then:
      thrown BusinessException
  }

}
