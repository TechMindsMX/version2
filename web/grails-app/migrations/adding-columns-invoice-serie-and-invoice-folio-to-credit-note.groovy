databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1526996558765-1") {
        addColumn(tableName: "credit_note") {
            column(name: "invoice_folio", type: "varchar(255)")
        }
    }

    changeSet(author: "temoc (generated)", id: "1526996558765-2") {
        addColumn(tableName: "credit_note") {
            column(name: "invoice_serie", type: "varchar(255)")
        }
    }
}
