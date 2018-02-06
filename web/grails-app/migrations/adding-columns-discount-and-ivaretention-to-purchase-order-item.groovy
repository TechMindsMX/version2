databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1517522205104-2") {
        addColumn(tableName: "purchase_order_item") {
            column(name: "discount", type: "decimal(5, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1517522205104-3") {
        addColumn(tableName: "purchase_order_item") {
            column(name: "iva_retention", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1517522205104-29") {
        dropColumn(columnName: "ieps", tableName: "purchase_order_item")
    }

}
