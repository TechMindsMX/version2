databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1512633424518-1") {
        addColumn(tableName: "product") {
            column(name: "unit_type_id", type: "bigint")
        }
    }

    changeSet(author: "temoc (generated)", id: "1512633424518-2") {
        addForeignKeyConstraint(baseColumnNames: "unit_type_id", baseTableName: "product", constraintName: "FKlguklsq80079it2outhwekbo7", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "unit_type")
    }

    changeSet(author: "temoc (generated)", id: "1512633424518-9") {
        dropColumn(columnName: "unit_type", tableName: "product")
    }
}
