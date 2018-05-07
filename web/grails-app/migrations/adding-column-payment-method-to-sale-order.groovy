databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1486681466430-1") {
        addColumn(tableName: "sale_order") {
            column(name: "payment_method", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (manual)", id: "initialize-payment-method-to-existing-sale-orders") {
      grailsChange {
        change {
          sql.execute("update sale_order set payment_method='TRANSFERENCIA_ELECTRONICA'")
        }
      }
    }
}
