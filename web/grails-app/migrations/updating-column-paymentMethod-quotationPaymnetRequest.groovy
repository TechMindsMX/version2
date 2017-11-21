databaseChangeLog = {

    changeSet(author: "brandonVergara (generated)", id: "1511194752012-3") {
        addColumn(tableName: "quotation_payment_request") {
            column(name: "payment_method", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "brandonVergara (manual)", id: "updating-payment-method") {
    grailsChange {
      change {
        sql.execute("update quotation_payment_request set payment_method='EFECTIVO'")
      }
    }
  }
    changeSet(author: "brandonVergara (generated)", id: "1511194752012-15") {
        dropColumn(columnName: "payment_way", tableName: "quotation_payment_request")
    }
    


}
