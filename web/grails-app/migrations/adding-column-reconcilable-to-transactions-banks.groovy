databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1490739766060-1") {
        addColumn(tableName: "movimientos_bancarios") {
            column(name: "reconcilable", type: "bit") {
                constraints(nullable: "false")
            }
        }
    }

}
