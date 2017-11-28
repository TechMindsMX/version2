databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1511889915302-1") {
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

    changeSet(author: "temoc (generated)", id: "1511889915302-2") {
        addUniqueConstraint(columnNames: "unit_key", constraintName: "UC_UNIT_TYPEUNIT_KEY_COL", tableName: "unit_type")
    }

}
