databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1499276120101-2") {
        createTable(tableName: "paysheet_project") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "paysheet_projectPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "company_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "integration_factor", type: "DECIMAL(19, 6)") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "occupational_risk_rate", type: "DECIMAL(19, 6)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1499276120101-4") {
        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "paysheet_project", constraintName: "FKsd5mydsmyv1a5nmem2vlxjhrk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

}
