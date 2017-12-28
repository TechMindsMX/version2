databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1511909273239-1") {
        createTable(tableName: "unit_type") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "unit_typePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "company_id", type: "BIGINT") {
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

    changeSet(author: "temoc (generated)", id: "1511909273239-2") {
        addUniqueConstraint(columnNames: "company_id, unit_key", constraintName: "UK2291669e32b02bae732a24673a3c", tableName: "unit_type")
    }

    changeSet(author: "temoc (generated)", id: "1511909273239-3") {
        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "unit_type", constraintName: "FKpgdyblfk517hdk0g6pl3w88o3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

}
