databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1510444163710-1") {
        createTable(tableName: "final_transaction_result") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "final_transaction_resultPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "comment", type: "VARCHAR(255)")

            column(name: "company_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "execution_mode", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "transaction_date", type: "datetime") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1510444163710-3") {
        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "final_transaction_result", constraintName: "FK68i1nlrwrkeran04dftcr9gd0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

}
