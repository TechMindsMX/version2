databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1487955227017-1") {
        addColumn(tableName: "sale_order_item") {
            column(name: "discount", type: "decimal(5, 2)") {
                constraints(nullable: "false")
            }
        }
    }

}
