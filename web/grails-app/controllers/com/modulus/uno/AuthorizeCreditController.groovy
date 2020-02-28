package com.modulus.uno

class AuthorizeCreditController {

  def creditService

  def index() {
    println (session.corporate)
    println ("😱"*10)
    println "What"


    Company company = Company.get(session.company.toLong())
    def data = creditService.list(company, params)

println "Esta es la lista de las companies: ${data.credits.dump()}"

    respond data.credits, model: [credits: data.credits, total: data.total]

  }
}