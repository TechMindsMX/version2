databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1496337061168-1") {
        dropForeignKeyConstraint(baseTableName: "commission_transaction", constraintName: "FK8w3vwxutio3xq81elclso28c8")
    }

    changeSet(author: "tim (generated)", id: "1496337061168-2") {
        dropColumn(columnName: "invoice_id", tableName: "commission_transaction")
    }

}
