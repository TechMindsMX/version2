databaseChangeLog = {

    changeSet(author: "cggg88jorge (generated)", id: "1501599553442-1") {
        addColumn(tableName: "menu") {
            column(name: "parameters", type: "varchar(255)")
        }
    }
    
}
