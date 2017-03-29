databaseChangeLog = {

  changeSet(author: "cggg88jorge (generated)", id: "1490281103111-2") {
    addColumn(tableName: "cash_out_order") {
      column(name: "transaction_id", type: "bigint")
    }
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490281103111-3") {
    addColumn(tableName: "fees_receipt") {
      column(name: "transaction_id", type: "bigint")
    }
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490281103111-4") {
    addColumn(tableName: "payment") {
      column(name: "transaction_id", type: "bigint")
    }
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490281103111-5") {
    addColumn(tableName: "payment_to_purchase") {
      column(name: "transaction_id", type: "bigint")
    }
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490281103111-6") {
    addForeignKeyConstraint(baseColumnNames: "transaction_id", baseTableName: "payment_to_purchase", constraintName: "FK3v7xhvvtaylrsmswqpjpcuf9i", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "transaction")
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490281103111-7") {
    addForeignKeyConstraint(baseColumnNames: "transaction_id", baseTableName: "payment", constraintName: "FK53qo12unt0o5flr83axs6v2i7", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "transaction")
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490281103111-8") {
    addForeignKeyConstraint(baseColumnNames: "transaction_id", baseTableName: "fees_receipt", constraintName: "FK7ldehp71lhn2seavmsteb7fcx", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "transaction")
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490281103111-9") {
    addForeignKeyConstraint(baseColumnNames: "transaction_id", baseTableName: "cash_out_order", constraintName: "FKeowph9pjwcueoi2gy4fsv95bw", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "transaction")
  }

}
