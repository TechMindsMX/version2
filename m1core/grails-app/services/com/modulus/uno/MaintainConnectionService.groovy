package com.modulus.uno

import groovy.sql.Sql

class MaintainConnectionService {

  def dataSource

  void executeQueryForMaintainConnection() {
    log.info "Executing query to db at ${new Date().format('dd-MM-yyyy hh:mm:ss')}"
    def sql = new Sql(dataSource)
    def rows = sql.rows("select id, username from user where enabled=true")
    log.info "Result query: ${rows.size()}"
  }

}
