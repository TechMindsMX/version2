databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1523457255698-1") {
        addColumn(tableName: "paysheet_employee") {
            column(name: "paysheet_receipt_uuidias", type: "varchar(255)")
        }
    }

    changeSet(author: "temoc (generated)", id: "1523457255698-2") {
        addColumn(tableName: "paysheet_employee") {
            column(name: "paysheet_receipt_uuidsa", type: "varchar(255)")
        }
    }

    changeSet(author: "temoc (generated)", id: "1523457255698-3") {
        dropColumn(columnName: "paysheet_receipt_uuid", tableName: "paysheet_employee")
    }
}
