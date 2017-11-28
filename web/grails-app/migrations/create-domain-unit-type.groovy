databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1511825075759-1") {
        createTable(tableName: "unit_type") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "unit_typePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "symbol", type: "VARCHAR(255)")

            column(name: "unit_key", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

}
