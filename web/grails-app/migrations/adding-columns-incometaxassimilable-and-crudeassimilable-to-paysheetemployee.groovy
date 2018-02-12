databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1515020220872-2") {
        addColumn(tableName: "paysheet_employee") {
            column(name: "crude_assimilable", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1515020220872-3") {
        addColumn(tableName: "paysheet_employee") {
            column(name: "income_tax_assimilable", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1515020220872-4") {
        addColumn(tableName: "paysheet_employee") {
            column(name: "net_assimilable", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "updating-net-assimilable-with-salary-assimilable-in-paysheet-employee") {
    grailsChange {
      change {
        sql.execute("update paysheet_employee set net_assimilable=salary_assimilable")
      }
    }
  }

    changeSet(author: "temoc (generated)", id: "1515020220872-29") {
        dropColumn(columnName: "salary_assimilable", tableName: "paysheet_employee")
    }

}
