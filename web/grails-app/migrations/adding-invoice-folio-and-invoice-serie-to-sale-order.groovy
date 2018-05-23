databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1526937041085-1") {
        addColumn(tableName: "sale_order") {
            column(name: "invoice_folio", type: "varchar(255)")
        }
    }

    changeSet(author: "temoc (generated)", id: "1526937041085-2") {
        addColumn(tableName: "sale_order") {
            column(name: "invoice_serie", type: "varchar(255)")
        }
    }
}
