databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1527608372499-1") {
        addColumn(tableName: "credit_note") {
            column(name: "comments", type: "varchar(255)")
        }
    }

    changeSet(author: "temoc (generated)", id: "1527608372499-2") {
        addColumn(tableName: "credit_note") {
            column(name: "reject_reason", type: "varchar(255)")
        }
    }
}
