databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1498253975953-1") {
        addColumn(tableName: "payment_to_purchase") {
            column(name: "status", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "initializing-status-to-payments-to-purchase-existing") {
    grailsChange {
      change {
        sql.execute("update payment_to_purchase set status='APPLIED'")
      }
    }
  }

}
