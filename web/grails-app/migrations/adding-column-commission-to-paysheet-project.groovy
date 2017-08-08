databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1502148136359-1") {
        addColumn(tableName: "paysheet_project") {
            column(name: "commission", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

}
