databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1538590756238-1") {
        addColumn(tableName: "movimientos_bancarios") {
            column(name: "create_payment_complement", type: "bit") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1538590756238-2") {
        addColumn(tableName: "movimientos_bancarios") {
            column(name: "payment_complement_uuid", type: "varchar(255)")
        }
    }

}
