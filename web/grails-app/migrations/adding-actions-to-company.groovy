databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1487803417491-1") {
    addColumn(tableName: "action") {
      column(name: "company_id", type: "bigint") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1487803417491-2") {
    addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "action", constraintName: "FK_eyb39jmfkulkar6tr9n2j0rcm", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
  }

}
