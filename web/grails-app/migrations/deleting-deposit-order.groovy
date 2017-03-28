databaseChangeLog = {

  changeSet(author: "cggg88jorge (generated)", id: "1490652708746-1") {
    dropForeignKeyConstraint(baseTableName: "deposit_order_s3asset", constraintName: "FK_78mxqus4c33f8agef1720orat")
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490652708746-2") {
    dropForeignKeyConstraint(baseTableName: "deposit_order_s3asset", constraintName: "FK_fc4511gyg27b7fsbnh548xidq")
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490652708746-3") {
    dropForeignKeyConstraint(baseTableName: "deposit_order_authorization", constraintName: "FK_gd2dhyoj1fmwk484482lnquso")
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490652708746-4") {
    dropForeignKeyConstraint(baseTableName: "deposit_order_authorization", constraintName: "FK_lfljxhh8n5pik85wl02g2cuj4")
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490652708746-5") {
    dropForeignKeyConstraint(baseTableName: "deposit_order", constraintName: "FK_qxtq9xmyhwqmc3b2o4ty4ge8g")
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490652708746-7") {
    dropTable(tableName: "deposit_order")
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490652708746-8") {
    dropTable(tableName: "deposit_order_authorization")
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490652708746-9") {
    dropTable(tableName: "deposit_order_s3asset")
  }

}
