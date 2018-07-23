databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1525296295714-1") {
        addColumn(tableName: "business_entity") {
            column(name: "email", type: "varchar(200)")
        }
    }
}
