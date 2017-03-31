databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1490995307128-1") {
    createTable(tableName: "combination_link_combination") {
      column(name: "combination_link_combinations_id", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "combination_id", type: "BIGINT")
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1490995307128-2") {
    addForeignKeyConstraint(baseColumnNames: "combination_id", baseTableName: "combination_link_combination", constraintName: "FK32bjjmfnypwndv5oxpw1i7cr2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "combination")
  }

  changeSet(author: "makingdevs (generated)", id: "1490995307128-3") {
    addForeignKeyConstraint(baseColumnNames: "combination_link_combinations_id", baseTableName: "combination_link_combination", constraintName: "FK9ccs63ual08muwxtcgj2wi0iq", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "combination_link")
  }

  changeSet(author: "makingdevs (generated)", id: "1490995307128-4") {
    dropForeignKeyConstraint(baseTableName: "combination_link", constraintName: "FKrryphlpdlf6swyganswjtfhvs")
  }

  changeSet(author: "makingdevs (generated)", id: "1490995307128-5") {
    dropColumn(columnName: "combination_id", tableName: "combination_link")
  }

}
