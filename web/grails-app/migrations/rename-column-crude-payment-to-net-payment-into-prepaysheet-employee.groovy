databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1516250068626-2") {
        addColumn(tableName: "pre_paysheet_employee") {
            column(name: "net_payment", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "updating-net-payment-with-crude-payment-in-prepaysheet-employee") {
    grailsChange {
      change {
        sql.execute("update pre_paysheet_employee set net_payment=crude_payment")
      }
    }
  }

    changeSet(author: "temoc (generated)", id: "1516250068626-25") {
        dropColumn(columnName: "crude_payment", tableName: "pre_paysheet_employee")
    }
}
