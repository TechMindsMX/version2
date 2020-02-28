databaseChangeLog = {

    changeSet(author: "makingdevs (generated)", id: "1582927066054-1") {
        addColumn(tableName: "credit") {
            column(name: "authorize", type: "bit") {
                constraints(nullable: "false")
            }
        }
    }
}
