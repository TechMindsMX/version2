databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1490629939468-11") {
        dropColumn(columnName: "timone_account", tableName: "cash_out_order")
    }

    changeSet(author: "tim (generated)", id: "1490629939468-12") {
        dropColumn(columnName: "timone_uuid", tableName: "cash_out_order")
    }

}
