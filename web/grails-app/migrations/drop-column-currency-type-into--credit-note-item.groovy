databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1525901429977-1") {
        dropColumn(columnName: "currency_type", tableName: "credit_note_item")
    }
}
