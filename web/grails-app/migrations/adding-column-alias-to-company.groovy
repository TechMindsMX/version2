databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1518543072787-1") {
        addColumn(tableName: "company") {
            column(name: "alias_company", type: "varchar(255)")
        }
    }

}
