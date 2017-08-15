databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1502812071490-1") {
        addColumn(tableName: "paysheet") {
            column(name: "reject_reason", type: "varchar(255)") {
              constraints(nullable: "true")
            }
        }
    }

}
