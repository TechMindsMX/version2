databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1522091703269-1") {
        addColumn(tableName: "data_imss_employee") {
            column(name: "discharge_date", type: "datetime")
        }
    }
}
