databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1507215154055-1") {
        addColumn(tableName: "bank_account") {
            column(name: "client_number", type: "varchar(255)")
        }
    }

}
