databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1490830735057-1") {
        addColumn(tableName: "conciliation") {
            column(name: "banking_transaction_id", type: "bigint")
        }
    }

    changeSet(author: "tim (generated)", id: "1490830735057-3") {
        addForeignKeyConstraint(baseColumnNames: "banking_transaction_id", baseTableName: "conciliation", constraintName: "FKfl3w449g89qb87ttiok7a8d1d", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "movimientos_bancarios")
    }

}
