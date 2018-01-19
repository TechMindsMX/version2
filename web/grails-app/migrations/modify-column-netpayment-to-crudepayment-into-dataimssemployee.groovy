databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1514526595721-2") {
        addColumn(tableName: "data_imss_employee") {
            column(name: "crude_monthly_salary", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "updating-crude-payment-with-net-payment") {
    grailsChange {
      change {
        sql.execute("update data_imss_employee set crude_monthly_salary=net_monthly_salary")
      }
    }
  }

    changeSet(author: "temoc (generated)", id: "1514526595721-26") {
        dropColumn(columnName: "net_monthly_salary", tableName: "data_imss_employee")
    }

}
