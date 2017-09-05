databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1499358445166-2") {
        addColumn(tableName: "bank_account") {
            column(name: "card_number", type: "varchar(255)")
        }
    }

}
