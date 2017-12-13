databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1513179542010-1") {
        createTable(tableName: "biller_paysheet_project") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "biller_paysheet_projectPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "company_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "payment_schema", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "paysheet_project_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1513179542010-3") {
        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "biller_paysheet_project", constraintName: "FKaxy5s86qj415xekd08m5nst97", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "temoc (generated)", id: "1513179542010-4") {
        addForeignKeyConstraint(baseColumnNames: "paysheet_project_id", baseTableName: "biller_paysheet_project", constraintName: "FKbi8t3r926ygxuye1yht15rkv2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paysheet_project")
    }

}
