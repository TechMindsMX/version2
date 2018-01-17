databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1516139134037-2") {
        addColumn(tableName: "sale_order") {
            column(name: "invoice_purpose", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1516139134037-5") {
        addColumn(tableName: "sale_order") {
            column(name: "payment_method", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "initializing-payment-method-and-invoice-purpose-into-sale-orders") {
    grailsChange {
      change {
        sql.execute("update sale_order set payment_method='PUE', invoice_purpose='G01'")
      }
    }
  }

}
