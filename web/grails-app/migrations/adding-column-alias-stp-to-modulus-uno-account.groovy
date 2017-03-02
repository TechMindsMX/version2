databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1487715378040-1") {
        addColumn(tableName: "modulus_uno_account") {
            column(name: "alias_stp", type: "varchar(255)")
        }
    }

}
