databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1539367599293-1") {
        addColumn(tableName: "movimientos_bancarios") {
            column(name: "payment_complement_status", type: "varchar(255)")
        }
    }

}
