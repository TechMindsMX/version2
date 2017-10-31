databaseChangeLog = {

    changeSet(author: "temoc (manual)", id: "delete-current-paysheet-projects") {
      grailsChange {
        change {
          sql.execute("delete from paysheet_project")
        }
      }
    }

    changeSet(author: "temoc (generated)", id: "1508868024238-1") {
        addColumn(tableName: "paysheet_project") {
            column(name: "paysheet_contract_id", type: "bigint") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1508868024238-2") {
        addForeignKeyConstraint(baseColumnNames: "paysheet_contract_id", baseTableName: "paysheet_project", constraintName: "FKqhup0tock8twpnn1ig259mat3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paysheet_contract")
    }

    changeSet(author: "temoc (generated)", id: "1508868024238-3") {
        dropForeignKeyConstraint(baseTableName: "paysheet_contract_paysheet_project", constraintName: "FK2jx9urq7s72q80bqb16cod497")
    }

    changeSet(author: "temoc (generated)", id: "1508868024238-4") {
        dropForeignKeyConstraint(baseTableName: "paysheet_contract_paysheet_project", constraintName: "FK86ve27sbfvsosrg5ykk3crw5k")
    }

    changeSet(author: "temoc (generated)", id: "1508868024238-5") {
        dropForeignKeyConstraint(baseTableName: "paysheet_project", constraintName: "FKsd5mydsmyv1a5nmem2vlxjhrk")
    }

    changeSet(author: "temoc (generated)", id: "1508868024238-6") {
        dropTable(tableName: "paysheet_contract_paysheet_project")
    }

    changeSet(author: "temoc (generated)", id: "1508868024238-7") {
        dropColumn(columnName: "company_id", tableName: "paysheet_project")
    }

    changeSet(author: "temoc (generated)", id: "1508868024238-8") {
        addNotNullConstraint(columnDataType: "decimal(19,2)", columnName: "commission", tableName: "paysheet_project")
    }

}
