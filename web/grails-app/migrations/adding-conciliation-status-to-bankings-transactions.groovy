databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1490825789323-1") {
        addColumn(tableName: "movimientos_bancarios") {
            column(name: "conciliation_status", type: "varchar(255)")
        }
    }

}
