databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1515001745299-2") {
        addColumn(tableName: "pre_paysheet_employee") {
            column(name: "crude_payment", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "updating-crude-payment-with-net-payment-in-prepaysheetemployee") {
    grailsChange {
      change {
        sql.execute("update pre_paysheet_employee set crude_payment=net_payment")
      }
    }
  }

    changeSet(author: "temoc (generated)", id: "1515001745299-26") {
        dropColumn(columnName: "net_payment", tableName: "pre_paysheet_employee")
    }
}
