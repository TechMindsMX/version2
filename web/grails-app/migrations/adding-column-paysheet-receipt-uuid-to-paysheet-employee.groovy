databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1522873025478-1") {
        addColumn(tableName: "paysheet_employee") {
            column(name: "paysheet_receipt_uuid", type: "varchar(255)")
        }
    }

    changeSet(author: "temoc (generated)", id: "1522873025478-2") {
        dropNotNullConstraint(columnDataType: "varchar(255)", columnName: "description", tableName: "paysheet_project")
    }
}
