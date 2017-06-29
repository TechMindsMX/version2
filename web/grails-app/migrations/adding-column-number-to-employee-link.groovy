databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1498761036578-3") {
        addColumn(tableName: "employee_link") {
            column(name: "number", type: "varchar(255)") {
                constraints(nullable: "true")
            }
        }
    }

}
