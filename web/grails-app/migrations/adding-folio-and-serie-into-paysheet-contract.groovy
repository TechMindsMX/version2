databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1522095900643-1") {
        addColumn(tableName: "paysheet_contract") {
            column(name: "folio", type: "integer") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1522095900643-2") {
        addColumn(tableName: "paysheet_contract") {
            column(name: "serie", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }
}
