databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1516124144049-4") {
        addColumn(tableName: "sale_order") {
            column(name: "payment_way", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "update-payment-way-with-payment-method-in-sale-order") {
    grailsChange {
      change {
        sql.execute("update sale_order set payment_way=payment_method")
      }
    }
  }

    changeSet(author: "temoc (generated)", id: "1516124144049-29") {
        dropColumn(columnName: "payment_method", tableName: "sale_order")
    }

}
