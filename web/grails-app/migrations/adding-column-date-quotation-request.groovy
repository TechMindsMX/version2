databaseChangeLog = {

    changeSet(author: "brandonVergara (generated)", id: "1510199248170-3") {
        addColumn(tableName: "quotation_request") {
            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "brandonVergara (generated)", id: "1510199248170-4") {
        addColumn(tableName: "quotation_request") {
            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }
        }
    }

}
