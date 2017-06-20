databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1497908910139-1") {
        createTable(tableName: "account_statement_bank_account") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "account_statement_bank_accountPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "bank_account_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "document_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "month", type: "datetime") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1497908910139-2") {
        addForeignKeyConstraint(baseColumnNames: "bank_account_id", baseTableName: "account_statement_bank_account", constraintName: "FKh15dp9ub23f53a0953t7adeeo", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "bank_account")
    }

    changeSet(author: "tim (generated)", id: "1497908910139-3") {
        addForeignKeyConstraint(baseColumnNames: "document_id", baseTableName: "account_statement_bank_account", constraintName: "FKmv7rlb2b4crwf2y1e7k34w1gd", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "s3asset")
    }

}
