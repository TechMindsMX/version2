databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1509382385568-1") {
        createTable(tableName: "payer_paysheet_project") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "payer_paysheet_projectPK")
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

            column(name: "paysheet_project_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "payment_schema", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1509382385568-2") {
        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "payer_paysheet_project", constraintName: "FK9nopukxuvddtm25tupttthd2c", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "temoc (generated)", id: "1509382385568-3") {
        addForeignKeyConstraint(baseColumnNames: "paysheet_project_id", baseTableName: "payer_paysheet_project", constraintName: "FKqb0jocdqt1mu51q8pb9th4nk8", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paysheet_project")
    }

}
