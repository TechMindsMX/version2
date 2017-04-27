databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1492610512462-2") {
        addColumn(tableName: "commission_transaction") {
            column(name: "invoice_id", type: "bigint")
        }
    }

    changeSet(author: "tim (generated)", id: "1492610512462-3") {
        addForeignKeyConstraint(baseColumnNames: "invoice_id", baseTableName: "commission_transaction", constraintName: "FK8w3vwxutio3xq81elclso28c8", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "commissions_invoice")
    }

    changeSet(author: "tim (generated)", id: "1492610512462-5") {
        dropForeignKeyConstraint(baseTableName: "commission_transaction", constraintName: "FKqypmg7qp3bwqkkv6vi85k1keq")
    }

    changeSet(author: "tim (generated)", id: "1492610512462-6") {
        dropColumn(columnName: "sale_order_of_charge_id", tableName: "commission_transaction")
    }


}
