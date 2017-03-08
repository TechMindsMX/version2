package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class BankService {

  Bank computeClabeAndGetBank(String clabe) {
    String prefix = clabe[0..2]
    Bank.findByBankingCodeLike("%${prefix}")
  }

}
