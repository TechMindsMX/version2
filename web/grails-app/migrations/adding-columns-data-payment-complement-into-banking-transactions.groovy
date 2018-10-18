databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1539886145353-1") {
        addColumn(tableName: "movimientos_bancarios") {
            column(name: "payment_way", type: "varchar(255)")
        }
    }

    changeSet(author: "temoc (generated)", id: "1539886145353-2") {
        addColumn(tableName: "movimientos_bancarios") {
            column(name: "source_account", type: "varchar(255)")
        }
    }

    changeSet(author: "temoc (generated)", id: "1539886145353-3") {
        addColumn(tableName: "movimientos_bancarios") {
            column(name: "source_bank_id", type: "bigint")
        }
    }

    changeSet(author: "temoc (generated)", id: "1539886145353-4") {
        addForeignKeyConstraint(baseColumnNames: "source_bank_id", baseTableName: "movimientos_bancarios", constraintName: "FKsda9d323puge8m38i9a9w5t3x", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "bank")
    }

}
