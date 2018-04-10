databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1521737629618-1") {
        addColumn(tableName: "pre_paysheet_employee_incidence") {
            column(name: "exempt_amount", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1521737629618-2") {
        addColumn(tableName: "pre_paysheet_employee_incidence") {
            column(name: "internal_key", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1521737629618-3") {
        addColumn(tableName: "pre_paysheet_employee_incidence") {
            column(name: "key_type", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1521737629618-4") {
        addColumn(tableName: "pre_paysheet_employee_incidence") {
            column(name: "taxed_amount", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "initializing-new-colums-into-pre-paysheet-employee-incidence") {
    grailsChange {
      change {
        sql.execute("update pre_paysheet_employee_incidence set exempt_amount=amount, taxed_amount=0")
        sql.execute("update pre_paysheet_employee_incidence set internal_key='P038', key_type='038' where type='PERCEPTION'")
        sql.execute("update pre_paysheet_employee_incidence set internal_key='D004', key_type='004' where type='DEDUCTION'")
      }
    }
  }

    changeSet(author: "temoc (generated)", id: "1521737629618-5") {
        dropColumn(columnName: "amount", tableName: "pre_paysheet_employee_incidence")
    }
}
