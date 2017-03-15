
databaseChangeLog = {
    changeSet(author: "temoc (manual)", id: "drop-not-null-constraint-to-change-type-from-sale-order") {
        dropNotNullConstraint(tableName: "sale_order", columnName: "change_type", columnDataType: "decimal(19, 2)")
    }
}

