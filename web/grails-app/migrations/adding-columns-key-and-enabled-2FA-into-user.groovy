databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1530747750478-1") {
        addColumn(tableName: "user") {
            column(name: "enable2fa", type: "bit") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1530747750478-2") {
        addColumn(tableName: "user") {
            column(name: "key2fa", type: "varchar(255)")
        }
    }

}
