databaseChangeLog = {

    changeSet(author: "iMacLeo (generated)", id: "1512074746879-4") {
        addColumn(tableName: "quotation_request") {
            column(name: "product_id", type: "bigint")
        }
    }

    changeSet(author: "iMacLeo (generated)", id: "1512074746879-5") {
        addForeignKeyConstraint(baseColumnNames: "product_id", baseTableName: "quotation_request", constraintName: "FKadimgkbd3lujgqfan4n0n3nl2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "product")
    }

    changeSet(author: "iMacLeo (generated)", id: "1512074746879-21") {
        dropColumn(columnName: "sat_concept", tableName: "quotation_request")
    }

  changeSet(author: "brandonVergara (manual)", id: "delete_quotation_reuqest") {
    grailsChange {
      change {
        sql.execute("update quotation_request set product_id=2")
      }
    }
  }

}
