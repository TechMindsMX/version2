databaseChangeLog = {

    changeSet(author: "brandonVergara (generated)", id: "1510201196636-1") {
        createTable(tableName: "quotation_payment_request") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "quotation_payment_requestPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "note", type: "VARCHAR(255)")

            column(name: "payment_way", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "quotation_contract_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }


    changeSet(author: "brandonVergara (generated)", id: "1510201196636-5") {
        addForeignKeyConstraint(baseColumnNames: "quotation_contract_id", baseTableName: "quotation_payment_request", constraintName: "FKdjsmvxu9l7l7rypfbvc0t5wn2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "quotation_contract")
    }

}
