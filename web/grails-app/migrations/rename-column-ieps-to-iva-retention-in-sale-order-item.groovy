databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1498573292531-1") {
        addColumn(tableName: "sale_order_item") {
            column(name: "iva_retention", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1498573292531-6") {
        dropColumn(columnName: "ieps", tableName: "sale_order_item")
    }

}
