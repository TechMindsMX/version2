databaseChangeLog = {

  changeSet(author: "cggg88jorge (generated)", id: "1490197096363-1") {
    createTable(tableName: "transaction") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "transactionPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "amount", type: "DECIMAL(19, 2)") {
        constraints(nullable: "false")
      }

      column(name: "date_created", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "key_account", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "key_transaction", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "last_updated", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "payment_concept", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "reference_number", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "tracking_key", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "transaction_status", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "transaction_type", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "uuid", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }

}
