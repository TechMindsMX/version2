databaseChangeLog = {

    changeSet(author: "makingdevs (generated)", id: "1583860253511-1") {
        addColumn(tableName: "company") {
            column(name: "first_payment_day", type: "integer") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "makingdevs (generated)", id: "1583860253511-2") {
        addColumn(tableName: "company") {
            column(name: "second_payment_day", type: "integer") {
                constraints(nullable: "false")
            }
        }
    }
}
