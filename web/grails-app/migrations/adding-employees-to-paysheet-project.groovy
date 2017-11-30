databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1511747255736-1") {
        createTable(tableName: "paysheet_project_business_entity") {
            column(name: "paysheet_project_employees_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "business_entity_id", type: "BIGINT")
        }
    }

    changeSet(author: "temoc (generated)", id: "1511747255736-2") {
        addForeignKeyConstraint(baseColumnNames: "paysheet_project_employees_id", baseTableName: "paysheet_project_business_entity", constraintName: "FKi74tpcga13wpnvm1n43xa73s3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paysheet_project")
    }

    changeSet(author: "temoc (generated)", id: "1511747255736-3") {
        addForeignKeyConstraint(baseColumnNames: "business_entity_id", baseTableName: "paysheet_project_business_entity", constraintName: "FKonvbscfv7ap7v52hycqkw626x", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "business_entity")
    }
}
