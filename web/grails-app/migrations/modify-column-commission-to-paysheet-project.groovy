databaseChangeLog = {

    changeSet(author: "luis (manual)", id: "increase-precision-in-commission-from-paysheet-project") {
        modifyDataType(tableName: "paysheet_project", columnName: "commission", newDataType: "decimal(19,6)")
    }

}
