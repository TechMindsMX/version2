databaseChangeLog = {

    changeSet(author: "brandonVergara (generated)", id: "1511495550483-1") {
        addColumn(tableName: "quotation_request") {
            column(name: "iva", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }


    changeSet(author: "brandonVergara (generated)", id: "1511495550483-3") {
        addColumn(tableName: "quotation_request") {
            column(name: "subtotal", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "brandonVergara (generated)", id: "1511495550483-4") {
        addColumn(tableName: "quotation_request") {
            column(name: "total", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "brandonVergara (manual)", id: "adding-columns-quotation-request") {
        grailsChange {
           change {
             sql.execute("update quotation_request set iva=16, subtotal=0, total=0")
          }
       }
    }
}
