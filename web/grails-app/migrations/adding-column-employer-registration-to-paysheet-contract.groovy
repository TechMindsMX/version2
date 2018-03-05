databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1520267162482-1") {
        addColumn(tableName: "paysheet_contract") {
            column(name: "employer_registration", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

}
