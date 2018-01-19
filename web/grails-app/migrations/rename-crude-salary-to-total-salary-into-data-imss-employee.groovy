databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1516219550004-3") {
        addColumn(tableName: "data_imss_employee") {
            column(name: "total_monthly_salary", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "updating-total-payment-with-crude-payment") {
    grailsChange {
      change {
        sql.execute("update data_imss_employee set total_monthly_salary=crude_monthly_salary")
      }
    }
  }


    changeSet(author: "temoc (generated)", id: "1516219550004-25") {
        dropColumn(columnName: "crude_monthly_salary", tableName: "data_imss_employee")
    }

}
