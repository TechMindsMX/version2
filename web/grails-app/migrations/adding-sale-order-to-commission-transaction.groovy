databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1496337983504-1") {
        addColumn(tableName: "commission_transaction") {
            column(name: "invoice_id", type: "bigint")
        }
    }

    changeSet(author: "tim (generated)", id: "1496337983504-2") {
        addForeignKeyConstraint(baseColumnNames: "invoice_id", baseTableName: "commission_transaction", constraintName: "FKi5joadto09mh5ndypyplvebxi", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "sale_order")
    }

}
