databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1506630306119-1") {
        addColumn(tableName: "conciliation") {
            column(name: "payment_to_purchase_id", type: "bigint")
        }
    }

    changeSet(author: "temoc (generated)", id: "1506630306119-2") {
        addForeignKeyConstraint(baseColumnNames: "payment_to_purchase_id", baseTableName: "conciliation", constraintName: "FKbushtaw84ys2d5d64xkp4rtf9", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "payment_to_purchase")
    }

}
