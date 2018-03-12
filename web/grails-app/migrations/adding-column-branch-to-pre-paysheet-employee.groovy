databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1520879810099-1") {
        addColumn(tableName: "pre_paysheet_employee") {
            column(name: "branch", type: "varchar(255)")
        }
    }
}
