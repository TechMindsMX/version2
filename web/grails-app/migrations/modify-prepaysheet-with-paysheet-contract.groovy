databaseChangeLog = {

    changeSet(author: "temoc (manual)", id: "delete-current-prepaysheets-and-paysheets") {
      grailsChange {
        change {
          sql.execute("delete from paysheet_employee")
          sql.execute("delete from paysheet_s3asset")
          sql.execute("delete from paysheet")
          sql.execute("delete from pre_paysheet_employee_incidence")
          sql.execute("delete from pre_paysheet_employee")
          sql.execute("delete from pre_paysheet")
        }
      }
    }

    changeSet(author: "temoc (generated)", id: "1508882469292-1") {
        addColumn(tableName: "pre_paysheet") {
            column(name: "paysheet_contract_id", type: "bigint") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1508882469292-2") {
        addForeignKeyConstraint(baseColumnNames: "paysheet_contract_id", baseTableName: "pre_paysheet", constraintName: "FKedqwvss6jlkoqsa89d1r53dyt", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paysheet_contract")
    }

    changeSet(author: "temoc (generated)", id: "1508882469292-3") {
        dropForeignKeyConstraint(baseTableName: "pre_paysheet", constraintName: "FKaged9hw8yh0hvb39rec7pb7p")
    }

    changeSet(author: "temoc (generated)", id: "1508882469292-4") {
        dropColumn(columnName: "company_id", tableName: "pre_paysheet")
    }

}
