databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1524579958889-1") {
        createTable(tableName: "user_employee") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "user_employeePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "business_entity_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "paysheet_project_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "user_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1524579958889-2") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_employee", constraintName: "FK935rscnqblbm9v9emac8a8h7t", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "temoc (generated)", id: "1524579958889-3") {
        addForeignKeyConstraint(baseColumnNames: "business_entity_id", baseTableName: "user_employee", constraintName: "FKqmj3qibpwxjr3axaoonmlm6i", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "business_entity")
    }

    changeSet(author: "temoc (generated)", id: "1524579958889-4") {
        addForeignKeyConstraint(baseColumnNames: "paysheet_project_id", baseTableName: "user_employee", constraintName: "FKt0d1hfak8dd71j2iv4rh0h9ik", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paysheet_project")
    }
}
