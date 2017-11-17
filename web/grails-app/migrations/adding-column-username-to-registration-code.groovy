databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1510766945039-1") {
        addColumn(tableName: "registration_code") {
            column(name: "username", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

}
