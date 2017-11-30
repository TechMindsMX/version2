databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1511967814409-1") {
        createTable(tableName: "paysheet_contract_user") {
            column(name: "paysheet_contract_users_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "user_id", type: "BIGINT")
        }
    }

    changeSet(author: "temoc (generated)", id: "1511967814409-2") {
        addForeignKeyConstraint(baseColumnNames: "paysheet_contract_users_id", baseTableName: "paysheet_contract_user", constraintName: "FK22quin5m9s14765naknnh8ul3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paysheet_contract")
    }

    changeSet(author: "temoc (generated)", id: "1511967814409-3") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "paysheet_contract_user", constraintName: "FKeoc2793ep0u5se5ex92g8isoi", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }

}
