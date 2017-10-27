databaseChangeLog = {

    changeSet(author: "temoc (manual)", id: "delete-current-paysheets") {
      grailsChange {
        change {
          sql.execute("delete from paysheet_employee")
          sql.execute("delete from paysheet_s3asset")
          sql.execute("delete from paysheet")
        }
      }
    }

    changeSet(author: "temoc (generated)", id: "1509139408925-1") {
        addColumn(tableName: "paysheet") {
            column(name: "paysheet_contract_id", type: "bigint") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1509139408925-2") {
        addForeignKeyConstraint(baseColumnNames: "paysheet_contract_id", baseTableName: "paysheet", constraintName: "FKpykd43i53md9mjsdeag4f8fci", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paysheet_contract")
    }

    changeSet(author: "temoc (generated)", id: "1509139408925-3") {
        dropForeignKeyConstraint(baseTableName: "paysheet", constraintName: "FKedbi45b914pvpib0q2fyo7iu")
    }

    changeSet(author: "temoc (generated)", id: "1509139408925-4") {
        dropColumn(columnName: "company_id", tableName: "paysheet")
    }

}
